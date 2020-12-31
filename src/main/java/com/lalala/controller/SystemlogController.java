package com.lalala.controller;

import com.alibaba.fastjson.JSONObject;
import com.lalala.pojo.Systemlog;
import com.lalala.service.SystemlogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统日志的控制层
 */
@Controller
@RequestMapping("SystemlogModule")
public class SystemlogController {

    @Resource(name = "systemlogService")
    private SystemlogService systemlogService;

    /**
     * 访问利用公共页面的抽取页面
     * @return
     */
    @RequestMapping("listSystemlogHTML")
    public String showIndexPage(){
        return "systemlog/ListSystemlog.html";
    }

    /**
     * 进行一个多条件的分页 查找
     * @param reqMap
     * @return
     */
    @PostMapping("queryDynamic")
    @ResponseBody
    public String queryDynamic(@RequestBody(required = false) Map<String,Object> reqMap){
        int page = 0;
        int size = 3;
        if (reqMap != null) {
            if (reqMap.get("page").toString() != null) {
                page = Integer.parseInt(reqMap.get("page").toString());
            }
            if (reqMap.get("size").toString() != null) {
                size = Integer.parseInt(reqMap.get("size").toString());
            }
        }

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "operatetime"));
        //以它的操作时间做一个升序排列
        Page<Systemlog> pageinfo = systemlogService.queryDynamic(reqMap, PageRequest.of(page, size, Sort.by(orders)));
        List<Systemlog> systemlogs = pageinfo.getContent();
        JSONObject result = new JSONObject();//maven中配置alibaba的fastjson依赖
        //"rows"和"total"这两个属性是为前端列表插件"bootstrap-table"服务的
        result.put("rows", systemlogs);
        result.put("total", pageinfo.getTotalElements());
        return result.toJSONString();
    }
}
