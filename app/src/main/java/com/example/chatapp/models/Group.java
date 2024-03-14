package com.example.chatapp.models;

public class Group {

    String groupName, userId;

    public Group() {
    }

    public Group(String groupName, String userId) {
        this.groupName = groupName;
        this.userId = userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
