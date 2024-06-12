package com.example.fetch_rewards_task;

public class DataEntity {
    String id;
    String name;
    public DataEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public DataEntity() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
