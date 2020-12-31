package com.lalala.report;

import com.lalala.service.CompanyService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 报表的控制层类
 */
@Controller
@RequestMapping("CompanyReport")
public class ReportController {

    @Resource(name = "companyService")
    private CompanyService companyService;

    /**
     * 用来跳转到报表页面
     * @return
     */
    @RequestMapping("showHtml")
    public String showIndexPage(){
        return "report/CompanyReport.html";
    }

    /**
     * 转换成html的格式输出
     * @param reqMap
     * @param request
     * @param response
     * @throws IOException
     * @throws JRException
     */
    @RequestMapping("/html")
    public void reportHtml(@RequestBody(required = false) Map<String,Object> reqMap, HttpServletRequest request, HttpServletResponse response) throws IOException, JRException {
        JRDataSource dataSource=new JRBeanCollectionDataSource(companyService.findAll());
        UtilJasperExport.exportTOHtml("static/ReportTemplates/company5.jasper", reqMap, dataSource, request, response);
    }

    /**
     * 报表以Excel的方式进行输出
     */
    @RequestMapping("/excel")
    public void reportExcel(@RequestBody(required = false) Map<String,Object> reqMap,HttpServletRequest request,HttpServletResponse response) throws IOException, JRException
    {
        JRDataSource datasource=new JRBeanCollectionDataSource(companyService.findAll());
        UtilJasperExport.exportToXls("static/ReportTemplates/company5.jasper","公司列表", reqMap, datasource, response);
    }
}
