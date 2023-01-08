
## Simple RPC

### 简介
简单的RPC组件，只依赖 Zookeeper，只要有Zookeeper即可使用，兼容Spring，使用非常方便简单。


### 如何使用
- 如何提供服务：
  1、在启动类上添加 @EnableSimpleRpc 注解
  2、在提供服务的接口实现类上，添加 @SimpleRpcService 注解
  
- 如何调用服务：
  只需在属性或set方法上添加 @SimpleRpcResource 注解即可，类似 spring 的 @Autowired


### 更新日志
  2023/1/8：目前处于开发阶段，实现了基本的rpc功能

