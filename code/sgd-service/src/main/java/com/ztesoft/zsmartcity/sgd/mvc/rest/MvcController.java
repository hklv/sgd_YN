package com.ztesoft.zsmartcity.sgd.mvc.rest;

import com.ztesoft.zsmartcity.sgd.mvc.domain.Demo;
import com.ztesoft.zsmartcity.sgd.mvc.exception.MvcException;
import com.ztesoft.zsmartcity.sgd.mvc.service.MvcService;
import io.swagger.annotations.*;
import lodsve.security.annotation.NeedLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/6/30 下午12:11
 */
@Api(value = "Mybatis的CRUD测试", description = "Mybatis的CRUD测试")
@RestController
@RequestMapping("/mvc")
public class MvcController {
    @Autowired
    private MvcService mvcService;

    @ApiOperation(value = "保存demo", notes = "保存demo")
    @ApiResponses({@ApiResponse(code = 200, message = "保存demo成功")})
    @RequestMapping(value = "/saveList", method = RequestMethod.POST)
    public boolean saveList(@ApiParam(name = "demoDto", value = "demos") @RequestBody Demo demos) {
        List<Demo> demoList = new ArrayList<>();
        demoList.add(demos);
        return mvcService.insertList(demoList) == 1;
    }

    @ApiOperation(value = "保存demo", notes = "保存demo")
    @ApiResponses({@ApiResponse(code = 200, message = "保存demo成功")})
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public boolean save(@ApiParam(name = "demoDto", value = "demo") @RequestBody Demo demo) {
        return mvcService.insert(demo) == 1;
    }

    @ApiOperation(value = "获取所有demo", notes = "获取所有demo")
    @ApiResponses({@ApiResponse(code = 200, message = "获取所有demo成功")})
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Page<Demo> list(@ApiParam(name = "pageable", value = "分页信息,传参方式：?page=0&size=10") @PageableDefault(page = 0, size = 10) Pageable pageable) {

        try {
            System.out.println(1/0);
        } catch (Exception e) {
            throw new MvcException(100, "error!");
        }

        return mvcService.list(pageable);
    }

    @ApiOperation(value = "获取demo", notes = "获取demo")
    @ApiResponses({@ApiResponse(code = 200, message = "获取demo成功")})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @NeedLogin
    public Demo load(@PathVariable("id") Long id) {
        return mvcService.select(id);
    }
}
