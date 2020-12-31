package com.lalala.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * 上传文件用的工具类
 */
@Component  //工具类需要的注解
public class ReceiveUploadFile {
    /**
     * 接收上传的文件，文件接收成功后，反馈回文件的保存路径,fileprefix:保存文件的前缀
     */
    public String receiveFile(MultipartFile file, String fileprefix)
    {   //该方法的整体逻辑是先避免上传文件不为空且不能是特定的几种文件，同时在上传的文件后面添加时间后追方便管理
        JSONObject res=new JSONObject();
        if(file.isEmpty()){  //如果不为空
            res.put("code",0);
            res.put("msg","文件为空");
            return  res.toJSONString();
        }
        String filename=file.getOriginalFilename(); //获取文件名字
        String lattername=filename.substring(filename.lastIndexOf(".")).toLowerCase(); //截取文件后缀并转为小写
        if(lattername.equals(".exe")||lattername.equals(".dll")||lattername.equals(".bat")||lattername.equals(".com")){
            //不允许使用这几种后缀名的文件上传
            res.put("code",0);
            res.put("msg","文件格式错误");
            return res.toJSONString();
        }
        //获取时间作为文件夹名称
        SimpleDateFormat format =new SimpleDateFormat("yyyyMM");
        String str=format.format(new Date());

        //判断文件夹是否存在
        if(isOSLinux()){  //linux环境下
            str="/home/uploadfile/"+str+"/";
        }else{ //win下
            str="D:\\uploadfile\\"+str+"\\";
        }
        File dir=new File(str);
        if(!dir.exists()){ //判断文件是否存在
            dir.mkdirs();
        }
        //获取当前的时间（到毫秒，作为保存文件的名称）
        SimpleDateFormat format1=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String strSave=format1.format(new Date())+lattername;

        try{  //保存文件
            String strFileFullPath=str+fileprefix+strSave;  //路径总名
            File dest=new File(strFileFullPath);
            file.transferTo(dest);
            res.put("code",1);
            res.put("msg",strFileFullPath);
            return res.toJSONString();
        }catch (Exception e){
            e.printStackTrace();
        }
        res.put("code",0);
        res.put("msg","上传失败");
        return res.toJSONString();
    }

    /**
     * 判断当前的环境是否是linux，如是是linux则为true，不是（是windows）则为false
     */
    private boolean isOSLinux()
    {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().indexOf("linux") > -1){return true;}
        else {return false;}
    }
}

