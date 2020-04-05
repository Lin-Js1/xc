package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator.
 */
@Mapper
@Repository
public interface CourseMapper {
   CourseBase findCourseBaseById(String id);

   Page<CourseBase> findCourseList();

   Page<CourseInfo> findCourseListPage(CourseListRequest courseListRequest);

}
