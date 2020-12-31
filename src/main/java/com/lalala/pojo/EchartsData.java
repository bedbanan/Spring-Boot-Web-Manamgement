package com.lalala.pojo;

//图表类
//list集合中的数据格式较为统一，为了统一，定义一个实体(不与数据库表对应)，该实体包括两个属性：name、value。
public class EchartsData {
    private String name;
    private float value;

    public EchartsData(String name, float value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
