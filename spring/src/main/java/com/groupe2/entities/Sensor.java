package com.groupe2.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

@Entity
public class Sensor implements Serializable {
        @Id
    private Long sensorId;
    private String location;
    private String type;
    private Long frequency;
        @OneToMany(mappedBy="sensor", fetch=FetchType.LAZY)
    private Collection<Measure> measures;
        @ManyToMany(cascade = { CascadeType.ALL })
        @JoinTable(
            name = "sensor_user", 
            joinColumns = { @JoinColumn(name = "sensor") }, 
            inverseJoinColumns = { @JoinColumn(name = "app_user") }
        )
    private Collection<User> users;

    public Sensor() {
        super();
    }
    public Sensor(Long sensorId, String location, Long frequency, String type) {
        super();
        this.sensorId = sensorId;
        this.location = location;
        this.frequency = frequency;
        this.type = type;
    }

    public Long getSensorId() {
        return sensorId;
    }
    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Long getFrequency() {
        return frequency;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Collection<Measure> getMeasures() {
        return measures;
    }
    public void setMeasures(Collection<Measure> measures) {
        this.measures = measures;
    }
    public Collection<User> getUsers() {
        return users;
    }
    public void setUsers(Collection<User> users) {
        this.users = users;
    }
    public void addUser(User user) {
        this.users.add(user);
    }
    public void rmUser(User user) {
        this.users.remove(user);
    }
    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }
}