package com.mercury.excelimport.model;

/**
 * Created by Fedake on 2015-08-21.
 */
public class System
{
    private int id;
    private String desc;
    private String name;
    private String supportGroup;

    public System()
    {

    }

    public System(int id, String desc, String name, String supportGroup)
    {
        this.id = id;
        this.desc = desc;
        this.name = name;
        this.supportGroup = supportGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupportGroup() {
        return supportGroup;
    }

    public void setSupportGroup(String supportGroup) {
        this.supportGroup = supportGroup;
    }
}
