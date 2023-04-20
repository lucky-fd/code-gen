package com.yushi.code.controller;


import com.yushi.code.common.core.web.controller.BaseController;
import com.yushi.code.common.core.web.domain.AjaxResult;
import com.yushi.code.domain.SysDictType;
import com.yushi.code.service.ISysDictDataService;
import com.yushi.code.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author lucky_fd
 * @since 2020.6.8
 */
@RestController
@RequestMapping("/dict/type")
public class SysDictTypeController extends BaseController
{

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return AjaxResult.success(dictTypes);
    }

}
