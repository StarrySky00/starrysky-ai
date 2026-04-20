package com.starrysky.ai.tools;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.starrysky.ai.entity.po.Course;
import com.starrysky.ai.entity.po.CourseReservation;
import com.starrysky.ai.entity.po.School;
import com.starrysky.ai.entity.query.CourseQuery;
import com.starrysky.ai.service.ICourseReservationService;
import com.starrysky.ai.service.ICourseService;
import com.starrysky.ai.service.ISchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author StarrySky
 * &#064;date  2026/4/20 11:29 星期一
 */

@Component
@RequiredArgsConstructor
public class CourseTools {
    private final ICourseService courseService;
    private final ICourseReservationService courseReservationService;
    private final ISchoolService schoolService;

    //根据query条件查询课程列表
    //returnDirect = false  结果返回给前端or大模型。默认false返回给大模型
    @Tool(description = "根据条件查询课程列表", returnDirect = false)
    public List<Course> queryCourse(@ToolParam(description = "查询的条件", required = false) CourseQuery query) {
        if (query == null) {
            return List.of();
        }
        QueryChainWrapper<Course> wrapper = courseService.query()
                .eq(query.getType() != null, "type", query.getType())
                .le(query.getEdu() != null, "edu", query.getEdu());
        if (query.getSorts() != null && !query.getSorts().isEmpty()) {
            for (CourseQuery.Sort sort : query.getSorts()) {
                wrapper.orderBy(true, sort.getAsc(), sort.getField());
            }
        }
        return wrapper.list();
    }

    // 查询校区
    @Tool(description = "根据条件查询所有校区")
    public List<School> querySchool(@ToolParam(description = "校区所在城市集合", required = false) List<School> cities) {
        return schoolService.lambdaQuery()
                .in(cities != null && cities.isEmpty(), School::getCity, cities)
                .list();
    }

    //预约课程和对应校区
    @Tool(description = "生成预约单,返回预约单号预提示信息")
    public String reserveCourse(@ToolParam(description = "预约的课程") String course,
                                @ToolParam(description = "预约的校区") String school,
                                @ToolParam(description = "预约学生姓名") String studentName,
                                @ToolParam(description = "预约学生联系方式") String contactInfo,
                                @ToolParam(description = "备注", required = false) String remark
    ) {
        CourseReservation cr = new CourseReservation();
        cr.setCourse(course);
        cr.setSchool(school);
        cr.setStudentName(studentName);
        cr.setContactInfo(contactInfo);
        cr.setRemark(remark);
        return courseReservationService.save(cr)
                ? String.format("预约成功【预约单号：%s】", cr.getId()) : "预约失败";
    }


}
