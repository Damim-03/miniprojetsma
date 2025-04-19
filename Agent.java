package com.retima.models;



public class Agent {
    private int id;
    private String name;
    private String phone;
    private String address;
    private AgentType type;
    private int age;
    private String maritalStatus;

    public enum AgentType {
        SELLER, BUYER
    }

    // الـ Constructor
    public Agent(int id, String name, String phone, String address, AgentType type) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.type = type;
    }

    // Constructor آخر للمشتري مع معلومات إضافية
    public Agent(int id, String name, String phone, String address, AgentType type, int age, String maritalStatus) {
        this(id, name, phone, address, type);
        this.age = age;
        this.maritalStatus = maritalStatus;
    }

    // getters و setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AgentType getType() {
        return type;
    }

    public void setType(AgentType type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", type=" + type +
                '}';
    }
}
