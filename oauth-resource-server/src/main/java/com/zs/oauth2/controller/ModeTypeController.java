package com.zs.oauth2.controller;

import com.zs.oauth2.model.vo.ModelTypeOptVO;
import com.zs.oauth2.service.ModelTypeService;
import com.zs.core.module.Result;
import com.zs.oauth2.constant.Constant;
import com.zs.oauth2.model.entity.ModelType;
import com.zs.oauth2.model.request.AddModelTypeReq;
import com.zs.oauth2.model.request.UpdateModelTypeReq;
import com.zs.oauth2.utils.PageInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 测试接口.
 */
@RestController
@RequestMapping("/modelType")
public class ModeTypeController {

    @Autowired
    private ModelTypeService modelTypeService;

    /**
     * 新建模块类型
     */
    @PostMapping("/add")
    public  Result<String> addModelType(@Valid @RequestBody AddModelTypeReq addModelTypeReq) {
        modelTypeService.addModelType(addModelTypeReq);
        return Result.succeed(null, "添加模块类型成功");

    }

    /**
     * 获取模块类型列表
     */
    @GetMapping("/list")
    public Result<PageInfoResult> getModelTypeList(@RequestParam(value = "pageSize") Integer pageSize,
                                   @RequestParam(value = "current") Integer current,
                                   @RequestParam(value = "typeName", required = false)String typeName) {

        if (current == null) {
            current = 1;
        }
        if (pageSize == null) {
            pageSize = Constant.COMMON_PAGE_SIZE;
        }
        PageInfoResult result =  modelTypeService.getModelTypeList(current, pageSize, typeName);
        return Result.succeed(result, "获取模块类型成功");
    }

    /**
     * 获取模块类型列表
     */
    @GetMapping("/list/options")
    public Result getSelectModeTypeList() {

        List<ModelTypeOptVO> list =  modelTypeService.getModeTypeOptList();
        return Result.succeed(list, "获取模块类型成功");
    }

    /**
     * 更新模块类型
     */
    @PutMapping("/update")
    public  Result<ModelType> updateModelType(@Valid @RequestBody UpdateModelTypeReq updateModelTypeReq) {

        ModelType modelType = modelTypeService.updateModelType(updateModelTypeReq);

        return Result.succeed(modelType, "更新模块类型成功");

    }


    /**
     * 根据id删除类型模块
     */
    @DeleteMapping("/delete")
    public Result<String> deleteById(@RequestParam String id) {
        modelTypeService.deleteById(id);
        return Result.succeed(null, "删除模块类型成功");

    }


}
