package com.assess.enums;

public enum RoleEnum {

    SUPER_ADMIN("SUPER_ADMIN", "超级管理员"),
    REGISTER("REGISTER", "注册用户"),
    WORKER("WORKER", "业务员");

    private String code;
    private String desc;

    RoleEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
