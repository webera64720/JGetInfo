package com.itweber.jgetinfo;

public class SystemVariable {

    String name;
    String value;

    public SystemVariable() {

    }

    public SystemVariable(String Name) {
        this.name = Name;
    }

    public SystemVariable(String Name, String Value) {
        this.name = Name;
        this.value = Value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public void setValue(String Value) {
        this.value = Value;
    }

}
