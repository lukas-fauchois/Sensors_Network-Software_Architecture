package com.groupe2.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
@Table(name="app_user")
public class User implements Serializable {
        @Id
    private String login;
    private String password;
    private String name;
    private String role;
    private boolean active;
        @ManyToMany(mappedBy="users")
    private Collection<Sensor> sensors;

    public User() {
        super();
    }
    public User(String login, String password, String name, String role, boolean active) {
        super();
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
        this.active = active;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean getActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public Collection<Sensor> getSensors() {
        return sensors;
    }
    public void setSensors(Collection<Sensor> sensors) {
        this.sensors = sensors;
    }
    public void addSensor(Sensor sensor) {
        this.sensors.add(sensor);
    }
    public void rmSensor(Sensor sensor) {
        this.sensors.remove(sensor);
    }
}
