package com.simple.rpc.provider.framewrok;

import com.simple.rpc.common.annotation.EnableSimpleRpc;
import com.simple.rpc.common.exception.SimpleRpcException;
import com.simple.rpc.common.exception.SimpleRpcResponseEnum;
import com.simple.rpc.provider.annotation.SimpleRpcService;
import com.simple.rpc.provider.bean.ServiceBeanInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
@Slf4j
@Configuration
public class RpcProviderBeanRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, EnvironmentAware, BeanFactoryAware {

    private Environment environment;
    private ResourceLoader resourceLoader;
    private BeanFactory beanFactory;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
                                        BeanDefinitionRegistry registry) {
        if (!annotationMetadata.hasAnnotation(EnableSimpleRpc.class.getName())) {
            return;
        }
        Map<String, Object> annotationAttributesMap = annotationMetadata.getAnnotationAttributes(EnableSimpleRpc.class.getName());
        AnnotationAttributes annotationAttributes = Optional.ofNullable(AnnotationAttributes.fromMap(annotationAttributesMap)).orElseGet(AnnotationAttributes::new);
        // 获取需要扫描的包
        String[] packages = retrievePackagesName(annotationMetadata, annotationAttributes);
        // useDefaultFilters = false,即第二个参数 表示不扫描 @Component、@ManagedBean、@Named 注解标注的类
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false, environment, resourceLoader);
        // 添加我们自定义注解的扫描
        scanner.addIncludeFilter(new AnnotationTypeFilter(SimpleRpcService.class));
        // 扫描包
        for (String needScanPackage : packages) {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(needScanPackage);
            try {
                registerCandidateComponents(registry, candidateComponents);
            } catch (ClassNotFoundException e) {
                log.error("simple rpc provider register spring bean fail", e);
                throw new SimpleRpcException(SimpleRpcResponseEnum.providerRegisterBeanFail, e);
            }
        }
    }

    /**
     * 获取需要扫描的包
     */
    private String[] retrievePackagesName(AnnotationMetadata annotationMetadata, AnnotationAttributes annotationAttributes) {
        String[] packages = annotationAttributes.getStringArray("packages");
        if (packages.length > 0) {
            return packages;
        }

        String className = annotationMetadata.getClassName();
        int lastDot = className.lastIndexOf('.');
        return new String[]{className.substring(0, lastDot)};
    }

    /**
     * 注册 BeanDefinition
     */
    private void registerCandidateComponents(BeanDefinitionRegistry registry, Set<BeanDefinition> candidateComponents) throws ClassNotFoundException {
        for (BeanDefinition candidateComponent : candidateComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {
                AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();
                Map<String, Object> customImportAnnotationAttributesMap = annotationMetadata.getAnnotationAttributes(SimpleRpcService.class.getName());
                AnnotationAttributes customImportAnnotationAttributes = Optional.ofNullable(AnnotationAttributes.fromMap(customImportAnnotationAttributesMap)).orElseGet(AnnotationAttributes::new);
                String beanName = customImportAnnotationAttributes.getString("beanName");
                // 默认 bean name
                if (StringUtils.isEmpty(beanName)) {
                    String className = candidateComponent.getBeanClassName();
                    if (StringUtils.isEmpty(className)) {
                        throw new RuntimeException("CustomResource 注解异常，无法获取类信息，" + candidateComponent.getResourceDescription());
                    }
                    beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                }

                // bean
                String className = annotationMetadata.getClassName();
                Class<?> clazz = Class.forName(className);
                Object bean = ServiceBeanInit.getAndInitBean(clazz);

                ((DefaultListableBeanFactory) this.beanFactory).registerSingleton(beanName, bean);
            }
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
