package com.lalala.log;

import com.alibaba.fastjson.JSON;
import com.lalala.pojo.SysUser;
import com.lalala.pojo.Systemlog;
import com.lalala.service.SystemlogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * 切面类，用于向数据库保存操作日志
 */
@Aspect
@Component
public class SystemLogAspect {
    @Autowired
    private SystemlogService systemlogService;

    //给切点定义新的属性
    @Pointcut("@annotation(AOPLog)")
    public void logAspect(){};

    //获取自定义的注解属性
    public String[] ObtainCustomAttribute(ProceedingJoinPoint point) throws  Exception{
        //获取连接点目标类名
        String connectname=point.getTarget().getClass().getName();
        //获取连接点签名的方法名
        String methodname=point.getSignature().getName();
        //获取连接点的参数
        Object[] objects=point.getArgs();
        //根据连接点类的名字获取指定类
        Class targetClass=Class.forName(connectname);
        //获取指定类里面的方法
        Method[] methods=targetClass.getMethods();
        String[] arrInfo=new String[2];  //该数组是为了返回注解中的两个属性
        for (Method method:methods) {
            if(method.getName().equals(methodname)){  //如果和连接点签名中的方法名相等
                Class[] classes=method.getParameterTypes();
                if(classes.length==objects.length){  //连接点类中方法的参数和获取的方法中的参数一致
                    arrInfo[0]=method.getAnnotation(AOPLog.class).operatetype();  //获取操作类型
                    arrInfo[1]=method.getAnnotation(AOPLog.class).operatedesc(); //获取操作简介
                    break;
                }
            }
        }
        return arrInfo;
    }

    /***
     * 获取连接点的传入、返回信息，创建日志并写入数据库
     */
    @Around("logAspect()")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable
    {
        Systemlog systemlog = new Systemlog();

        //获取到当前线程绑定的请求对象，得到session
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysUser userInfo=(SysUser)request.getSession().getAttribute("userinfo");
        systemlog.setUsername(userInfo.getUsername());

        //获取方法执行时间
        Date date=new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));//输出CST（北京时间）在东8区
        systemlog.setOperatetime(format.format(date));

        systemlog.setIp(request.getRemoteAddr());//获取用户IP

        //利用UserAgent工具类进行User-Agent解析,得到操作系统类型和浏览器类型
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        systemlog.setBrowstype(userAgent.getBrowser().toString());//浏览器名称
        systemlog.setOstype(userAgent.getOperatingSystem().toString());//操作系统名称

        String[] arrInfo=ObtainCustomAttribute(joinPoint);
        systemlog.setOperatetype(arrInfo[0]);//注解描述的操作类型
        systemlog.setOperatedesc(arrInfo[1]);//注解描述的操作简述

        //******这里取目标方法的还回值******
        Object proceed=joinPoint.proceed();//目标方法本身的还回值
        String strInfo=(String)proceed;//转换成字符串（JSON格式的字符串）

        //根据切点的args来获取目标对象（方法）收到的参数值,转换为json方式，这就是操作详情
        //另外根据操作类型，如果是查询、登录、新增(保存、添加)、其它，则详情对应的是传入的参数；如果是删除和修改，则详情是方法的返回值
        Object[] args = joinPoint.getArgs();
        String strOperatetype=systemlog.getOperatetype();
        if((strOperatetype.indexOf("查询")>=0||(strOperatetype.indexOf("其它")>=0)))
        {
            systemlog.setOperatedetail(JSON.toJSONString(args[0]));
            systemlogService.save(systemlog);
        }

        if((strOperatetype.indexOf("登录")>=0))
        {
            systemlog.setOperatedetail("");
            systemlogService.save(systemlog);
        }

        if(strOperatetype.indexOf("新增")>=0||strOperatetype.indexOf("保存")>=0||strOperatetype.indexOf("添加")>=0)
        {
            Map map = UtilFilterPureEntity.getKeyAndValue(args[0]);//过滤实体中的非基本属性（如对象、集合等等）
            systemlog.setOperatedetail(JSON.toJSONString(map));
            systemlogService.save(systemlog);
        }

        if((strOperatetype.indexOf("删除")>-1)||(strOperatetype.indexOf("修改")>-1))//这里的详细记录是方法返回值（字符串格式的json数据）
        {
            systemlog.setOperatedetail(strInfo);
            systemlogService.save(systemlog);
        }
        return proceed;

    }
}
