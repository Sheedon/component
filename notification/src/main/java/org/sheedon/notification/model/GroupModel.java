package org.sheedon.notification.model;

/**
 * 组Model
 *
 * @Author: sheedon
 * @Email: sheedonsun@163.com
 * @Date: 2021/11/9 2:32 下午
 */
public class GroupModel {

    private String groupId;
    private String groupName;
    private String description;

    public GroupModel(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public GroupModel(String groupId, String groupName, String description) {
        this(groupId, groupName);
        this.description = description;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return description;
    }
}
