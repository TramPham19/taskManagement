package com.example.taskManagement.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Users {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private String id;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;

    public Users() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return first_name;
    }

    public void setName(String first_name) {
        this.first_name = first_name;
    }

    public String getTeamName() {
        return last_name;
    }

    public void setTeamName(String last_name) {
        this.last_name = last_name;
    }

}
