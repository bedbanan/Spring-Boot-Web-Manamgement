package com.lalala.controller;

import com.alibaba.fastjson.JSONObject;
import com.lalala.log.AOPLog;
import com.lalala.pojo.SysRole;
import com.lalala.pojo.SysUser;
import com.lalala.pojo.Ztree;
import com.lalala.service.SysAuthService;
import com.lalala.service.SysRoleService;
import com.lalala.service.SysUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作为前端界面用户、权限、角色的控制层
 */
@Controller
@RequestMapping("/security")
public class UserRoleAuthController {
    @Resource(name="sysUserService")
    private SysUserService sysUserService;
    @Resource(name="sysRoleService")
    private SysRoleService sysRoleService;
    @Resource(name="sysAuthService")
    private SysAuthService sysAuthService;

    /**
     * 查询所有角色然后跳转页面，利用Thymeleaf模板中的迭代器来进行页面数据的输出
     */
    @RequestMapping("/ListUserRoleAuthHTML")
    public String findAllrole(Model model){
        List<SysRole> list=sysRoleService.findAll();
        model.addAttribute("sysRoles",list);
        return "security/ListUserRoleAuth.html";
    }

    /**
     * 系统账号的分页查询
     */
    @PostMapping("/listuser")
    @ResponseBody
    public String queryDynamic(@RequestBody Map<String,Object> reqMap){
        //固定不变的两个分页参数
        int page=0;
        if(reqMap.get("page").toString()!=null){page= Integer.parseInt(reqMap.get("page").toString());}
        int size=3;
        if(reqMap.get("size").toString()!=null){size= Integer.parseInt(reqMap.get("size").toString());}

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"username"));

        Page<SysUser> pageinfo=sysUserService.queryDynamic(reqMap, PageRequest.of(page,size,Sort.by(orders)));
        List<SysUser> sysUsers =pageinfo.getContent();
        JSONObject result = new JSONObject();
        result.put("rows", sysUsers);
        result.put("total",pageinfo.getTotalElements());
        return result.toJSONString();
    }

    /*
     * 返回全部权限
     */
    @RequestMapping("listauth")
    @ResponseBody
    public List<Ztree> findAllToZtree(String roleid){
        return sysAuthService.findAllToZtree(roleid);
    }

    /**
     * 保存角色
     */
    @PostMapping("/saveRole")
    @ResponseBody
    @AOPLog(operatetype = "新增",operatedesc = "新增一个角色")
    public String save(SysRole sysRole){
        sysRoleService.save(sysRole);
        return "OK";
    }

    /**
     * 删除角色
     */
    @PostMapping("/deleteRole")
    @ResponseBody
    @AOPLog(operatetype = "删除角色",operatedesc = "删除了一个角色")
    public String deleteRole(String uuid){
        sysRoleService.deleteByuuid(uuid);
        sysRoleService.deleteMaptabByUuid(uuid);
        return "OK";
    }

    /*
    保存子节点（需要判断是否重复，如果没有重复，保存信息，id是当前选定节点的id，name是需要新增加节点的名称)
     */
    @PostMapping("/saveChildAuth")
    @ResponseBody
    public String saveChildAuth(@RequestParam int id,String name){
        return sysAuthService.saveChildAuth(id,name);
    }

    /**
     * 根据节点id删除及子节点
     */
    @PostMapping("/deleteByChild")
    @ResponseBody
    public String deleteByChild(@RequestParam int id){
        sysAuthService.deleteByChild(id);
        return "OK";
    }

    /**
     * 保存角色对应的权限信息,其中‘authinfo’是以$分割的节点id字符串
     */
    @PostMapping("/editRole")
    @ResponseBody
    public String editRole(@RequestParam String uuid,String authinfo){
        sysAuthService.editRole(uuid,authinfo);
        return "OK";
    }

    /**
     * 查询全部的角色(填充添加用户中的下拉列表)
     */
    @RequestMapping("/findAllRoles")
    @ResponseBody
    public List<SysRole> findAllRoles() {
        List<SysRole> list=sysRoleService.findAll();
        return list;
    }

    /**
     * 用户名唯一性验证(如果已经存在，返回false，否则返回true；返回json数据，格式为{"valid",true})
     */
    @PostMapping("/validateUsername")
    @ResponseBody
    public String validateUsername(@RequestParam String username)
    {
        boolean blStatus=sysUserService.validateUsername(username);
        JSONObject result = new JSONObject();
        result.put("valid", blStatus);
        return result.toJSONString();
    }
    //邮箱号唯一性验证(如果已经存在，返回false，否则返回true；返回json数据，格式为{"valid",true})
    @PostMapping("/validateEmail")
    @ResponseBody
    public String validateEmail(@RequestParam String useremail)
    {
        boolean blStatus=sysUserService.validateEmail(useremail);
        JSONObject result = new JSONObject();
        result.put("valid", blStatus);
        return result.toJSONString();
    }

    //手机号唯一性验证(如果已经存在，返回false，否则返回true；返回json数据，格式为{"valid",true})
    @PostMapping("/validateMobile")
    @ResponseBody
    public String validateMobile(@RequestParam String usermobile)
    {
        boolean blStatus=sysUserService.validateMobile(usermobile);
        JSONObject result = new JSONObject();
        result.put("valid", blStatus);
        return result.toJSONString();
    }

    //返回AddSysUser.html页面
    @RequestMapping("/ridirectAddSysUserHtml")
    public String ridirectAddSysUserHtml()
    {
        return "security/AddSysUser.html";
    }


    //保存系统账户
    @RequestMapping("/saveSysUser")
    @ResponseBody
    @AOPLog(operatetype = "新增",operatedesc = "新增了一个系统账号")
    public String saveSysUser(SysUser sysUser)
    {
        sysUserService.save(sysUser);
        return "OK";
    }


}
