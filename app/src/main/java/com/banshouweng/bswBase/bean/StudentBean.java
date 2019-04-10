package com.banshouweng.bswBase.bean;

import com.banshouweng.bswBase.base.BaseBean;

/**
 * Created by leiming on 2017/10/10.
 */

public class StudentBean extends BaseBean{

    private String name;
    private int age;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
