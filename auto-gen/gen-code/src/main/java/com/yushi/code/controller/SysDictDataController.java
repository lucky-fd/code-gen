package com.yushi.code.controller;


import com.yushi.code.common.core.utils.StringUtils;
import com.yushi.code.common.core.web.controller.BaseController;
import com.yushi.code.common.core.web.domain.AjaxResult;
import com.yushi.code.domain.SysDictData;
import com.yushi.code.service.ISysDictDataService;
import com.yushi.code.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author lucky_fd
 * @since 2020.6.8
 */
@RestController
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController
{
    @Autowired
    private ISysDictDataService dictDataService;
    
    @Autowired
    private ISysDictTypeService dictTypeService;

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<SysDictData>();
        }
        return AjaxResult.success(data);
    }


}
