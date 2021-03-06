package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePicRepository extends JpaRepository<CoursePic,String> {

    /**
     * 当返回值大于0时，则表示删除成功的记录数
     */
    long deleteByCourseid(String courseId);
}
