package com.zs.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zs.oauth2.model.entity.CourseCategory;
import com.zs.oauth2.model.entity.CourseInfo;
import com.zs.oauth2.model.request.AddCourseReq;
import com.zs.oauth2.model.request.UpdateCourseReq;
import com.zs.oauth2.model.vo.CourseInfoOptVO;
import com.zs.oauth2.model.vo.CourseRelateCategoryVO;
import com.zs.oauth2.service.CourseCategoryService;
import com.zs.oauth2.service.CourseInfoLabelService;
import com.zs.oauth2.service.CourseInfoService;
import com.zs.oauth2.config.MinioConfig;
import com.zs.oauth2.enums.YesOrNo;
import com.zs.oauth2.mapper.CourseInfoMapper;
import com.zs.oauth2.model.vo.CourseInfoVO;
import com.zs.oauth2.utils.MinioUtil;
import com.zs.oauth2.utils.PageInfoResult;
import com.zs.oauth2.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CourseInfoServiceImpl extends ServiceImpl<CourseInfoMapper, CourseInfo>
        implements CourseInfoService {

    @Autowired
    private CourseInfoMapper courseInfoMapper;

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private CourseInfoLabelService courseInfoLabelService;
    
    @Autowired
    private CourseCategoryService courseCategoryService;




    @Override
    @Transactional
    public void addCourseInfo(AddCourseReq requestCourse) {
        CourseInfo course = new CourseInfo();
        CourseCategory courseCategory = courseCategoryService.getById(requestCourse.getCourseCategory());
        if (courseCategory == null) {
            throw new RuntimeException("课程类型错误,新增课程信息失败");
        }
        course.setCourseCategory(UUID.fromString(requestCourse.getCourseCategory()));
        UUID id = UUID.randomUUID();
        course.setId(id);

        //判断文件是否为空
        if (requestCourse.getFile() != null) {
            // 获取文件名
            String fileName = requestCourse.getFile().getOriginalFilename();
            //获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //判断上传的图片是否是jpg或者png类型  equalsIgnoreCase：不区分大小写
            if (".jpg".equalsIgnoreCase(suffixName)
                    || ".png".equalsIgnoreCase(suffixName)
                    || ".jpeg".equalsIgnoreCase(suffixName)) {
                MultipartFile file = requestCourse.getFile();
                String newFileName =  UUID.randomUUID().toString() + "." +
                        StringUtils.substringAfterLast(fileName, ".");
                //类型
                String contentType = file.getContentType();
                minioUtil.uploadFile(minioConfig.getBucketName(), file, newFileName, contentType);
                //TODO 判断课程封面违规
                //return Result.failed("课程封面违规!");

                course.setCoverPicUrl(String.format("%s/%s",minioConfig.getBucketName(), newFileName));
            } else {
               throw  new RuntimeException("文件格式不正确!");
            }
        }
        //处理课程大纲信息（录入课程信息大纲为空时候的处理）====>走的sql语句中没有将outline字段加上去，
        if (requestCourse.getOutLine() != null) {
            //获得原文件名字
            String fileName = System.currentTimeMillis() + requestCourse.getOutLine().getOriginalFilename();

            //获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //比较字符串时不区分大小写
            if (".pdf".equalsIgnoreCase(suffixName)) {

                MultipartFile file = requestCourse.getOutLine();
                String newFileName = UUID.randomUUID().toString() + "." +
                        StringUtils.substringAfterLast(fileName, ".");
                String contentType = file.getContentType();
                minioUtil.uploadFile(minioConfig.getBucketName(), file, newFileName, contentType);
                course.setCourseOutlineUrl(String.format("%s/%s",minioConfig.getBucketName(), newFileName));
            }
        }

        String courseNameTrim = requestCourse.getCourseName().trim(); //trim ：过滤前后空格
        course.setCourseName(courseNameTrim);
        course.setTarget(requestCourse.getTarget());
        course.setVersion(requestCourse.getVersion());
        course.setCourseDescribe(requestCourse.getCourseDescribe());
        if (StringUtils.isNoneBlank(requestCourse.getPreRequisiteId())) {
            course.setPreRequisiteId(UUID.fromString(requestCourse.getPreRequisiteId()));
        }
        course.setIsRecommend(requestCourse.getIsRecommend());
        course.setEnvironment(requestCourse.getEnvironment());
        course.setStatus(YesOrNo.YES.type);
        if (!isEnvValid(requestCourse.getEnvironment())) {
            throw  new RuntimeException("CodeServer,Jupyter和云桌面只能开启其中一个");
        }
        // 添加虚拟机组模板(CodeServer,Jupyter和云桌面开启其中一个)
        if (!requestCourse.getEnvironment().equals(3)) {
            course.setTemplateId(UUID.fromString(requestCourse.getTemplateId()));
            course.setClustersNum(requestCourse.getClustersNum());
        }
        // 添加标签列表
        if (!CollectionUtils.isEmpty(requestCourse.getLableList())) {
            for (String strLabelId : requestCourse.getLableList()) {
                courseInfoLabelService.addCourseInfoLabel(id.toString(), strLabelId);
            }
        }
        course.setCreateTime(new Date());
        course.setUpdateTime(new Date());
        course.setChapterNum(0);
        course.setClassHourNum(0);
        course.setCiteNum(0);
        course.setIsLatestVersion(false);
        courseInfoMapper.insert(course);
    }

    private boolean isEnvValid(Integer environment) {
        if (environment >= 0 && environment <= 3) {
            return true;
        }
        return false;
    }

    @Override
    public PageInfoResult getCourseInfoList(Integer current, Integer pageSize,
                                            String courseCategory, Integer target,
                                            String courseName, Integer status) {
        //开启分页功能
        PageHelper.startPage(current, pageSize);
        QueryWrapper<CourseInfo> query = new QueryWrapper<>();
        if (StringUtils.isNotBlank(courseName)) {
            query.like("course_name", courseName);
        }
        if (target != null) {
            query.eq("target", target);
        }
        if (StringUtils.isNotBlank(courseCategory)) {
            query.eq("course_category", UUID.fromString(courseCategory));
        }
        if (isStatusValid(status)) {
            query.eq("status", status);
        }
        List<CourseInfo> list = courseInfoMapper.selectList(query);
        PageInfoResult pageInfoResult = PageUtil.setPageInfoResult(list, current);
        if (!CollectionUtils.isEmpty(list)) {
            for (CourseInfo courseInfo : list) {
                CourseInfoVO courseInfoVO = new CourseInfoVO();
                BeanUtils.copyProperties(courseInfo, courseInfoVO);
                CourseCategory category = courseCategoryService.getById(String.valueOf(courseInfo.getCourseCategory()));
                courseInfoVO.setCourseCategoryName(category.getCategoryName());
                

            }

        }
    }

    @Override
    public List<CourseInfoOptVO> getCourseInfoOptList() {
        QueryWrapper<CourseInfo> query = new QueryWrapper<>();
        query.select("id", "course_name");

        List<CourseInfo> courseInfoList = courseInfoMapper.selectList(query);
        List<CourseInfoOptVO> res = new ArrayList<>();
        for (CourseInfo courseInfo : courseInfoList) {
            CourseInfoOptVO optVO = new CourseInfoOptVO();
            optVO.setId(courseInfo.getId());
            optVO.setCourseName(courseInfo.getCourseName());
            res.add(optVO);
        }
        return res;
    }

    @Override
    public CourseInfo getById(String courseInfoId) {
        QueryWrapper<CourseInfo> query = new QueryWrapper<>();
        query.eq("id", UUID.fromString(courseInfoId));
        return courseInfoMapper.selectOne(query);

    }

    @Override
    public void updateLatestVersion(String courseId) {
        CourseInfo exit = this.getById(courseId);
        if (exit == null) {
            throw new RuntimeException("课程信息不存在");
        }
        if (exit.getIsLatestVersion()) {
            throw new RuntimeException("该课程已经是最新版本");
        }
        QueryWrapper<CourseInfo> query = new QueryWrapper<>();
        query.eq("course_name", exit.getCourseName());
        query.eq("target", exit.getTarget());
        List<CourseInfo> courseInfoList = courseInfoMapper.selectList(query);
        // 同面向对象下并且同一个课程名称的课程的最新版本设置为不是最新版本
        if (!CollectionUtils.isEmpty(courseInfoList)) {
            for (CourseInfo courseInfo : courseInfoList) {
                if (courseInfo.getIsLatestVersion()) {
                    courseInfo.setIsLatestVersion(false);
                    this.updateByLatestVersion(courseInfo.getId(), courseInfo.getIsLatestVersion());
                }

            }
        }
        this.updateByLatestVersion(exit.getId(), true);
    }

    private void updateByLatestVersion(Object courseId, Boolean isLatestVersion) {
        UpdateWrapper<CourseInfo> query = new UpdateWrapper<>();
        query.eq("id", courseId)
                .set("is_latest_version", isLatestVersion)
                .set("update_time", new Date());
        courseInfoMapper.update(null, query);
    }

    @Override
    public void deleteById(String courseId) {
        CourseInfo courseInfo = this.getById(courseId);
        if (courseInfo == null) {
            throw new RuntimeException("课程不存在");
        }
        QueryWrapper<CourseInfo> query = new QueryWrapper<>();
        query.eq("id", UUID.fromString(courseId));
        courseInfoMapper.delete(query);
        // 删除course_info_label表的关联关系
        courseInfoLabelService.deleteByCourseInfoId(courseId);
    }

    @Override
    public CourseInfo updateCourseInfo(UpdateCourseReq updateCourseReq) {
        CourseInfo exit = this.getById(updateCourseReq.getId());
        if (exit == null) {
            throw new RuntimeException("课程不存在, 更新失败");
        }
        CourseCategory courseCategory = courseCategoryService.getById(updateCourseReq.getCourseCategory());
        if (courseCategory == null) {
            throw new RuntimeException("课程类型错误,新增课程信息失败");
        }
        //课程名称、版本号、面向对象相同,产生冲突
        CourseInfo other = this.getOne(new QueryWrapper<CourseInfo>()
                .eq("target", updateCourseReq.getTarget())
                .eq("version", updateCourseReq.getVersion())
                .eq("course_name", updateCourseReq.getCourseName()));
        if (other != null && !other.getId().equals(exit.getId())) {
           throw  new RuntimeException("已经存在课程名称、版本号、面向对象相同的课程，更新失败");
        }
        //判断文件是否为空
        if (updateCourseReq.getFile() != null) {
            // 获取文件名
            String fileName = updateCourseReq.getFile().getOriginalFilename();
            //获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //判断上传的图片是否是jpg或者png类型  equalsIgnoreCase：不区分大小写
            if (".jpg".equalsIgnoreCase(suffixName)
                    || ".png".equalsIgnoreCase(suffixName)
                    || ".jpeg".equalsIgnoreCase(suffixName)) {

                String coverPicUrl = exit.getCoverPicUrl();
                // 删除之前的图片
                if(StringUtils.isNotBlank(coverPicUrl)){
                    String oldFileName = coverPicUrl.substring(coverPicUrl.lastIndexOf("/" ) + 1);
                    minioUtil.removeFile(minioConfig.getBucketName(),oldFileName);
                }
                String newFileName =  UUID.randomUUID()+ "." +
                        StringUtils.substringAfterLast(fileName, ".");

                MultipartFile file = updateCourseReq.getFile();
                //类型
                String contentType = file.getContentType();
                minioUtil.uploadFile(minioConfig.getBucketName(), file, newFileName, contentType);
                //TODO 判断课程封面违规
                //return Result.failed("课程封面违规!");

                exit.setCoverPicUrl(String.format("%s/%s",minioConfig.getBucketName(), newFileName));
            } else {
                throw  new RuntimeException("文件格式不正确!");
            }
        }
        //处理课程大纲信息（录入课程信息大纲为空时候的处理）====>走的sql语句中没有将outline字段加上去，
        if (updateCourseReq.getOutLine() != null) {
            //获得原文件名字
            String fileName = updateCourseReq.getOutLine().getOriginalFilename();
            //获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            //比较字符串时不区分大小写
            if (".pdf".equalsIgnoreCase(suffixName)) {
                String courseOutlineUrl = exit.getCourseOutlineUrl();
                if (StringUtils.isNotBlank(courseOutlineUrl)) {
                    String oldFileName = courseOutlineUrl.substring(courseOutlineUrl.lastIndexOf("/" ) + 1);
                    minioUtil.removeFile(minioConfig.getBucketName(), oldFileName);
                }
                String newFileName =  UUID.randomUUID() + "." +
                        StringUtils.substringAfterLast(fileName, ".");
                MultipartFile file = updateCourseReq.getOutLine();
                minioUtil.uploadFile(minioConfig.getBucketName(), file, newFileName, file.getContentType());
                exit.setCourseOutlineUrl(String.format("%s/%s",minioConfig.getBucketName(), newFileName));
            }
        }

        String courseNameTrim = updateCourseReq.getCourseName().trim(); //trim ：过滤前后空格
        exit.setCourseName(courseNameTrim);
        exit.setTarget(updateCourseReq.getTarget());
        exit.setVersion(updateCourseReq.getVersion());
        exit.setCourseCategory(UUID.fromString(updateCourseReq.getCourseCategory()));
        exit.setCourseDescribe(updateCourseReq.getCourseDescribe());
        if (StringUtils.isNotBlank(updateCourseReq.getPreRequisiteId())) {
            exit.setPreRequisiteId(UUID.fromString(updateCourseReq.getPreRequisiteId()));
        }
        exit.setIsRecommend(updateCourseReq.getIsRecommend());
        if (!isEnvValid(updateCourseReq.getEnvironment())) {
            throw  new RuntimeException("CodeServer,Jupyter和云桌面只能开启其中一个");
        }
        // 添加虚拟机组模板(CodeServer,Jupyter和云桌面开启其中一个)
        if (!updateCourseReq.getEnvironment().equals(3)) {
            exit.setTemplateId(UUID.fromString(updateCourseReq.getTemplateId()));
            exit.setClustersNum(updateCourseReq.getClustersNum());
        }
        exit.setEnvironment(updateCourseReq.getEnvironment());

        // 删除之前的标签
        courseInfoLabelService.deleteByCourseInfoId(updateCourseReq.getId());
        // 添加新的标签列表
        if (!CollectionUtils.isEmpty(updateCourseReq.getLableList())) {
            for (String strLabelId : updateCourseReq.getLableList()) {
                courseInfoLabelService.addCourseInfoLabel(String.valueOf(exit.getId()), strLabelId);
            }
        }
        exit.setUpdateTime(new Date());
        courseInfoMapper.updateById(exit);

        return exit;
    }

    @Override
    public List<CourseRelateCategoryVO> getCourseRelateCategoryVOList() {

        List<CourseRelateCategoryVO> res = new ArrayList<>();
        List<CourseInfo> courseInfoList = courseInfoMapper.selectList(new QueryWrapper<CourseInfo>()
                .select("id", "course_name", "course_category"));
        if (CollectionUtils.isEmpty(courseInfoList)) {
            return res;
        }
        for (CourseInfo courseInfo : courseInfoList) {
            CourseCategory courseCategory = courseCategoryService.getById(String.valueOf(courseInfo.getCourseCategory()));
            CourseRelateCategoryVO courseRelateCategoryVO = new CourseRelateCategoryVO();
            courseRelateCategoryVO.setCourseTypeName(courseCategory.getCategoryName());
            courseRelateCategoryVO.setId(String.valueOf(courseInfo.getId()));
            courseRelateCategoryVO.setCourseName(courseInfo.getCourseName());
            res.add(courseRelateCategoryVO);
        }
        return res;

    }

    /**
     * 判断课程分类是否正确
     */
    private boolean isStatusValid(Integer status) {
        if (status == null) {
            return false;
        }
        return status.equals(0) || status.equals(1);
    }

    @Override
    public PageInfoResult getLatestVerCourseList(Integer current, Integer pageSize, String courseType, Integer target, Integer status) {
        return null;
    }
}
