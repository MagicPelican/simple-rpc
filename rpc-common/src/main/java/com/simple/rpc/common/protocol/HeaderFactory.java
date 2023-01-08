package com.simple.rpc.common.protocol;

import com.simple.rpc.common.constants.Constants;

import java.util.UUID;

/**
 * @Author: zhenghao
 * @Date: 2023/1/2
 */
public class HeaderFactory {

    public static Header create(){
        // todo 修改id逻辑
        String uuid = UUID.randomUUID().toString();
        return new Header(uuid, Constants.MAGIC ,Constants.VERSION_1);
    }


}
