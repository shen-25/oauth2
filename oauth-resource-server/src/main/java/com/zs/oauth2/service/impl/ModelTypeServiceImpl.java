package com.zs.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zs.oauth2.model.request.AddModelTypeReq;
import com.zs.oauth2.model.vo.ModelTypeOptVO;
import com.zs.oauth2.service.ModelTypeService;
import com.zs.oauth2.enums.YesOrNo;
import com.zs.oauth2.mapper.ModelTypeMapper;
import com.zs.oauth2.model.entity.ModelType;
import com.zs.oauth2.model.request.UpdateModelTypeReq;
import com.zs.oauth2.model.vo.ModelTypeVO;
import com.zs.oauth2.utils.PageInfoResult;
import com.zs.oauth2.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ModelTypeServiceImpl extends ServiceImpl<ModelTypeMapper, ModelType>
        implements ModelTypeService {

    @Autowired
    private ModelTypeMapper modelTypeMapper;

    @Override
    public PageInfoResult getModelTypeList(Integer current, Integer pageSize, String typeName) {

        //开启分页功能
        PageHelper.startPage(current, pageSize);
        QueryWrapper<ModelType> query = new QueryWrapper<>();
        if (StringUtils.isNotBlank(typeName)) {
            query.eq("name", typeName);
        }
        List<ModelType> list = modelTypeMapper.selectList(query);
        PageInfoResult result = PageUtil.setPageInfoResult(list, current);
        List<ModelTypeVO> res = new ArrayList<>();
        for (ModelType modelType : list) {
            ModelTypeVO modelTypeVO = new ModelTypeVO();
            BeanUtils.copyProperties(modelType, modelTypeVO);
            // TODO 数量查询
            modelTypeVO.setAmount(100);
            res.add(modelTypeVO);
        }
        result.setRecords(res);
        return result;
    }

    @Override
    public void addModelType(AddModelTypeReq modelTypeReq) {
        ModelType exit = this.getByTypeName(modelTypeReq.getTypeName());
        if (exit != null) {
            throw new RuntimeException("模块类型名称已存在, 添加失败");
        }
        // 状态为空或者不是启动，停用对应的状态码，设置默认为启动状态码
        if (!isStateValid(modelTypeReq.getStatus())) {
            modelTypeReq.setStatus(YesOrNo.YES.getType());
        }
        ModelType modelType = new ModelType();
        BeanUtils.copyProperties(modelTypeReq, modelType);
        modelType.setId(UUID.randomUUID());
        modelType.setName(modelTypeReq.getTypeName());
        modelType.setCreateTime(new Date());
        modelType.setUpdateTime(new Date());
        modelTypeMapper.insert(modelType);
    }

    @Override
    public ModelType getByTypeName(String typeName) {
        QueryWrapper<ModelType> query = new QueryWrapper<>();
        query.eq("name", typeName);
        return modelTypeMapper.selectOne(query);
    }

    @Override
    public void deleteById(String id) {
        ModelType modelType = this.getById(id);
        if (modelType == null) {
            throw new RuntimeException("模块类型不存在");
        }
        QueryWrapper<ModelType> query = new QueryWrapper<>();
        query.eq("id", UUID.fromString(id));
        modelTypeMapper.delete(query);
    }



    @Override
    public ModelType getById(String id) {
        QueryWrapper<ModelType> query = new QueryWrapper<>();
        query.eq("id", UUID.fromString(id));
        return modelTypeMapper.selectOne(query);
    }

    @Override
    public ModelType updateModelType(UpdateModelTypeReq modelTypeReq) {
        ModelType modelType = this.getById(modelTypeReq.getId());
        if (modelType == null) {
            throw new RuntimeException("模块类型不存在，更新失败");
        }
        // 1.1.1 判断模块类型名称是否需要修改
        if (StringUtils.isNotBlank(modelTypeReq.getTypeName())
        &&  !StringUtils.equals(modelType.getName(), modelTypeReq.getTypeName())) {
            ModelType exit = this.getByTypeName(modelTypeReq.getTypeName());
            if (exit != null) {
                throw new RuntimeException("模块类型名称已经存在");
            }
            modelType.setName(modelTypeReq.getTypeName());
        }
        if (isStateValid(modelTypeReq.getStatus())) {
            modelType.setStatus(modelTypeReq.getStatus());
        }
        if (modelTypeReq.getNote() != null) {
            modelType.setNote(modelTypeReq.getNote());
        }
        modelType.setUpdateTime(new Date());
        modelTypeMapper.updateById(modelType);
        return modelType;

    }

    @Override
    public List<ModelTypeOptVO> getModeTypeOptList() {
        QueryWrapper<ModelType> query = new QueryWrapper<>();
        query.select("id", "name");
        List<ModelTypeOptVO> res = new ArrayList<>();
        List<ModelType> modelTypeList = modelTypeMapper.selectList(query);
        for (ModelType modelType : modelTypeList) {
            ModelTypeOptVO selectModelTypeVO = new ModelTypeOptVO();
            selectModelTypeVO.setId(modelType.getId());
            selectModelTypeVO.setName(modelType.getName());
            res.add(selectModelTypeVO);
        }
        return res;
    }

    /**
     * 判断模块类型是否正确
     * @param state
     * @return
     */
    private boolean isStateValid(Integer state) {
        if (state == null) {
            return false;
        }
        return state.equals(0) || state.equals(1);
    }



}
