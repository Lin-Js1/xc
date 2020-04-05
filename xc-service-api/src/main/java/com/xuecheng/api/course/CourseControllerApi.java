package com.xuecheng.api.course;


import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

public interface CourseControllerApi {
    /**
     * 课程计划查询
     */
    public TeachplanNode findTeachplanList(String courseId);

    /**
     * 添加课程计划
     */
    public ResponseResult addTeachplan(Teachplan teachplan);

    /**
     * 添加课程图片
     */
    public ResponseResult addCoursePic(String courseId,String pic);

    /**
     * 查询课程图片
     */
    public CoursePic findCoursePic(String courseId);

    /**
     * 删除课程图片
     */
    public ResponseResult deleteCoursePic(String courseId);

    /**
     * 查询课程列表
     */
    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);

    /**
     * 添加课程基础信息
     */
    public AddCourseResult addCourseBase(CourseBase courseBase);

    /**
     * 获取课程基础信息
     */
    public CourseBase getCourseBaseById(String courseId);

    /**
     * 更新课程基础信息
     */
    public ResponseResult updateCourseBase(String id,CourseBase courseBase);


    /**
     * 获取课程营销信息
     */
    public CourseMarket getCourseMarketById(String courseId);

    /**
     * 更新课程营销信息
     */
    public ResponseResult updateCourseMarket(String id,CourseMarket courseMarket);

    /**
     * 课程视图查询
     */
    public CourseView courseView(String id);

    /**
     * 课程预览
     */
    public CoursePublishResult preview(String id);

    /**
     * 课程发布
     * @param id
     * @return
     */
    public CoursePublishResult publish(String id);

    @ApiOperation("保存媒资信息")
    public ResponseResult savemedia(TeachplanMedia teachplanMedia);

}
