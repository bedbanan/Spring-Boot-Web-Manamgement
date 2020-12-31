package com.lalala.controller;

import com.alibaba.fastjson.JSONObject;
import com.lalala.log.AOPLog;
import com.lalala.pojo.Company;
import com.lalala.pojo.EchartsData;
import com.lalala.service.CompanyService;
import com.lalala.utils.ReceiveUploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司的控制层
 */
@Controller //直接返回Json数据且可以返回页面
@RequestMapping("/CompanyModule") //简化和其他Controller之间的重复url
public class CompanyController {

    @Resource(name = "companyService") //通过byname的方式去查找相对应的实现类
    private CompanyService companyService;

    //注入文件接收类
    @Autowired
    private ReceiveUploadFile receiveUploadFile;

    @PostMapping("save")
    @ResponseBody
    @AOPLog(operatetype = "新增",operatedesc = "新增了一个公司的信息")
    public void save(Company company){  //保存公司信息
        // 通过javaBean对象完成参数的交互
        companyService.save(company);
    }
    @GetMapping("/delete")
    @ResponseBody
    public void delete(@RequestParam String uuid){  //删除功能,根据id删除
        companyService.delete(uuid);
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public List<Company> finAll(){
        //查询所有信息
        return companyService.findAll();
    }

    @PostMapping("/findAllSimplePage")
    @ResponseBody
    public Page<Company> findAllSimplePage(@RequestParam(name="page",required=false,defaultValue = "1") int page, @RequestParam(name="size",required=false,defaultValue = "2") int size) {
        //多条件排序分页查询
        List<Sort.Order> orders=new ArrayList<>();
       orders.add(new Sort.Order(Sort.Direction.DESC,"comname"));
       orders.add(new Sort.Order(Sort.Direction.ASC,"comaddress"));
       return companyService.findAllSimplePage(PageRequest.of(page,size,Sort.by(orders)));
    }


    @PostMapping("/findAllSimplePageMap")
    @ResponseBody
    public String findAllSimplePageMap(@RequestBody(required = false) Map<String,Object> reqMap){
        //多条件简单的分页查询
        int page=0;
        int size=3;
        if(reqMap!=null) {
            if(reqMap.get("page").toString()!=null){page= Integer.parseInt(reqMap.get("page").toString());}
            if(reqMap.get("size").toString()!=null){size= Integer.parseInt(reqMap.get("size").toString());}
        }

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"comname"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"comaddress"));

        Page<Company> pageinfo=companyService.findAllSimplePage(PageRequest.of(page,size,Sort.by(orders)));
        List<Company> companies =pageinfo.getContent();
        JSONObject result = new JSONObject();//maven中配置alibaba的fastjson依赖
        //"rows"和"total"这两个属性是为前端列表插件"bootstrap-table"服务的
        result.put("rows", companies);  //获取总行数
        result.put("total",pageinfo.getTotalElements()); //获取总页数
        return result.toJSONString();  //输出一个json的字符串给前端
    }

    @PostMapping("queryDynamic")
    @ResponseBody
    public String queryDynamic(@RequestBody(required = false) Map<String,Object> reqMap)
    {
        /**
         * 多条件排序及多条件分页查询
         */
        int page=0;
        int size=3;
        if(reqMap!=null)
        {
            if(reqMap.get("page").toString()!=null){page= Integer.parseInt(reqMap.get("page").toString());}
            if(reqMap.get("size").toString()!=null){size= Integer.parseInt(reqMap.get("size").toString());}
        }

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"comname"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"comaddress"));

        Page<Company> pageinfo=companyService.queryDynamic(reqMap,PageRequest.of(page,size,Sort.by(orders)));
        List<Company> companies =pageinfo.getContent();
        JSONObject result = new JSONObject();//maven中配置alibaba的fastjson依赖
        //"rows"和"total"这两个属性是为前端列表插件"bootstrap-table"服务的
        result.put("rows", companies);
        result.put("total",pageinfo.getTotalElements());
        return result.toJSONString();
    }

    @RequestMapping("findByNativeSQL")
    @ResponseBody
    public List<Company> findByNativeSQL(@RequestParam String companyname) {
        return companyService.findByNativeSQl(companyname);
    }


    @RequestMapping("/validateEmail")
    @ResponseBody
    public String validateEmail(@RequestParam String contactemail){
        //验证邮箱的唯一性
        boolean isTrue=companyService.validateEmail(contactemail); //验证邮箱
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("valid",isTrue);
        return jsonObject.toJSONString();
    }

    @PostMapping("upload")
    @ResponseBody
    public String upload(@RequestParam MultipartFile file)
    {
        /**
         * 接收上传文件，反馈回服务器端的文件保存路径（全路径）
         */
        return receiveUploadFile.receiveFile(file,"myfile");
    }

    /*
     * 为Echarts图表的展示准备数据，这里仅仅对员工数量和营业收入做集合
     */
    @PostMapping("chart")
    @ResponseBody
    public Map<String,List<EchartsData>> chart(){  //以map的格式返回数据给前端
        Map<String,List<EchartsData>> map=new HashMap<>();
        List<EchartsData> listEmployeenumber=new ArrayList<>();  //载入员工数量
        List<EchartsData> listTotaloutput=new ArrayList<>();  //载入总产值
        List<Company> list=companyService.findAll(); //查询所有的信息

        for (Company company:list) {  //把数据查出来的数据进行组合
            listEmployeenumber.add(new EchartsData(company.getComname(),company.getEmployeenumber()));
            //将员工总数量加入
            listTotaloutput.add(new EchartsData(company.getComname(),company.getTotaloutput()));
            //加入总产值
        }
        map.put("listperson",listEmployeenumber);
        map.put("listoutput",listTotaloutput);
        return map;
    }
    /*****************************************************
     * 通过控制层访问访问页面，分安全文件夹templates、公共文件夹public两种情况
     ****************************************************/
    @RequestMapping("listCompany")
    public String listCompany() {
        /**
         * 返回客户列表页面
         */
        return "company/ListCompany.html";
    }

    @RequestMapping("addCompanyHtml")
    public String addCompanyHtml(){
        /**
         * 返回添加页面
         */
        return "company/AddCompany.html";
    }
    
    @RequestMapping("chartHtml")
    public String chartHtml(){
        /**
         * 返回图表展示列表
         */
        return "company/ChartCompany.html";
    }

    /*****************************************************
     * Restful设计风格
     ****************************************************/
    @GetMapping("/company")
    @ResponseBody
    public List<Company> findAllRF() {
        /**
         * 查询数据，等于@RequestMapping(value="/company",method = RequestMethod.GET)
         */
        return companyService.findAll();
    }

    @GetMapping("/company/{comname}")
    @ResponseBody
    public List<Company> findByNativeSQLRT(@PathVariable String comname) {
        /**
         * 单参数查询
         */
        return companyService.findByNativeSQl(comname);
    }

    @PutMapping("/company/{comaddress}/{comname}")
    @ResponseBody
    public void updateByNameRT(@PathVariable String comaddress,@PathVariable String comname) {
        /**
         * 多参数，更新数据
         */
        System.out.println(comaddress);
        System.out.println(comname);
        companyService.updateByName(comaddress,comname);
    }
}
