package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;

public interface SysDicthinaryControllerApi {

    /**
     * 查询数据字典
     * @return
     */
    public SysDictionary getByType(String type);

}
