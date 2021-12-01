package com.example.taskManagement.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class HistoryTask {
    @Id
    @GenericGenerator(name="id" , strategy="increment")
    @GeneratedValue(generator="id")
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;
    @Column(name = "taskId")
    private Integer taskId;
    @Column(name = "action_history")
    private String action_history;
    @Column(name = "date_time")
    private Date date_time;

    public HistoryTask(Integer taskId, String action_history, Date date_time) {
        this.taskId = taskId;
        this.action_history = action_history;
        this.date_time = date_time;
    }

    public HistoryTask(){}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setAction_history(String action_history) {
        this.action_history = action_history;
    }

    public String getAction_history() {
        return action_history;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public Date getDate_time() {
        return date_time;
    }

}
