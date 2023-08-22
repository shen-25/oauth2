package com.zs.oauth2.service;

import com.zs.oauth2.model.vo.ModelTypeOptVO;
import com.zs.oauth2.model.entity.ModelType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.oauth2.model.request.AddModelTypeReq;
import com.zs.oauth2.model.request.UpdateModelTypeReq;
import com.zs.oauth2.utils.PageInfoResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zengshen
 * @since 2023-08-14
 */
public interface ModelTypeService extends IService<ModelType> {

    /**
     * 查询模块类型列表
     */
    PageInfoResult getModelTypeList(Integer current, Integer pageSize, String typeName);

    /**
     * 添加模块类型
     * @param addModelTypeReq
     */
    void addModelType(AddModelTypeReq addModelTypeReq);

    /**
     * 删除模块类型
     * @param id
     */
    void deleteById(String id);

    ModelType getById(String id);

    /**
     * 更新模块类型
     * @param updateModelTypeReq
     */
    ModelType updateModelType(UpdateModelTypeReq updateModelTypeReq);

    /**
     * 根据模块类型名称查询
     */
    ModelType getByTypeName(String typeName);

    /**
     * 查询模块类型的id和name
     * @return
     */
    List<ModelTypeOptVO> getModeTypeOptList();
}
