package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;

public interface CategoryControllerApi {

    /**
     * 查询分类
     * @return
     */
    public CategoryNode findList();
}
