package com.lalala.log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 过滤实体中的非基本属性（如list,mangtomany等），得到只含基本属性的map对象
 */
public class UtilFilterPureEntity {
    static String[] types={"class java.lang.Integer", "class java.lang.Double",
            "class java.lang.Float", "class java.lang.Long", "class java.lang.Short",
            "class java.lang.Byte", "class java.lang.Boolean", "class java.lang.Char",
            "class java.lang.String", "int", "double", "long", "short", "byte",
            "boolean", "char", "float"};

    /**
     * 根据类名称获取类的属性和属性值
     */
    public static Map<String,Object> getKeyAndValue(Object obj){
        Map<String,Object> map=new HashMap<>();
        Class userClass=(Class) obj.getClass(); //得到类对象
        Field[] fs=userClass.getDeclaredFields(); //得到类中的所有属性的集合
        for(int i=0;i<fs.length;i++){
            Field f=fs[i];
            f.setAccessible(true); //设置属性为可以访问的
            //判断属性是否是基础类型（防止配置的映射关系造成的大数据量,如list，manytomany）
            String typename=f.getGenericType().toString();
            boolean basictype=false; //属性是否为基础类型
            for (String s:types) {
                if(s.equals(typename)){
                    basictype=true;
                    break;
                }
            }
            if(!basictype){ //如果不是基础类就略过
                continue;
            }
            Object val = new Object();
            try {
                val = f.get(obj);// 得到此属性的值
                map.put(f.getName(), val);// 设置键值
            }catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return  map;
    }
}
