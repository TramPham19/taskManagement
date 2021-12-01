package com.example.taskManagement.model;

import com.sun.istack.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.NonNull;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class Task {
    @Id
    @GenericGenerator(name="id" , strategy="increment")
    @GeneratedValue(generator="id")
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;
    @Column(name = "description_task")
    private String description_task;
    @Column(name = "point_task")
    private Integer point_task;
    @Column(name = "assignee")
    private String assignee;
    @Column(name = "progress")
    private String progress;
    @Column(name = "parent_task")
    private Integer parent_task;
    @Column(name = "start_date")
    private Date start_date;
    @Column(name = "end_date")
    private Date end_date;


//    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

    public Task(Integer id,String description_task,Integer point_task, String assignee,String progress,Integer parent_task,Date start_date,Date end_date) {
        this.id= id;
        this.description_task= description_task;
        this.point_task=point_task;
        this.assignee=assignee;
        this.progress=progress;
        this.parent_task=parent_task;
        this.start_date=start_date;
        this.end_date=end_date;
    }

    public Task(Integer id,String description_task,Integer point_task, String assignee,String progress,Integer parent_task,Date start_date) {
        this.id= id;
        this.description_task= description_task;
        this.point_task=point_task;
        this.assignee=assignee;
        this.progress=progress;
        this.parent_task=parent_task;
        this.start_date=start_date;
    }

    public Task(Integer id,String description_task,Integer point_task, String assignee,String progress,Integer parent_task) {
        this.id= id;
        this.description_task= description_task;
        this.point_task=point_task;
        this.assignee=assignee;
        this.progress=progress;
        this.parent_task=parent_task;
    }

    public Task(String description_task,Integer point_task, String assignee,String progress,Integer parent_task) {
        this.description_task= description_task;
        this.point_task=point_task;
        this.assignee=assignee;
        this.progress=progress;
        this.parent_task=parent_task;
    }

    public Task() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription_task() {
        return description_task;
    }

    public void setDescription_task(String description_task) {
        this.description_task = description_task;
    }

    public Integer getPoint_task() {
        return point_task;
    }

    public void setPoint_task(Integer point_task) {
        this.point_task = point_task;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Integer getParent_task() {
        return parent_task;
    }

    public void setParent_task(Integer parent_task) {
        this.parent_task = parent_task;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description_task='" + description_task + '\'' +
                ", point_task=" + point_task +
                ", assignee='" + assignee + '\'' +
                ", progress='" + progress + '\'' +
                ", parent_task=" + parent_task +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                '}';
    }
}
