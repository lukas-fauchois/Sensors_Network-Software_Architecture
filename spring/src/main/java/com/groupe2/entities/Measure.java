package com.groupe2.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
public class Measure implements Serializable {
        @Id @GeneratedValue
    private Long measureId;
    private String type;
    private Float value;
    private Date dateTaken;
        @ManyToOne
        @JoinColumn(name="sensor")
    private Sensor sensor;

    public Measure() {
        super();
    }
    public Measure(Float value, Date dateTaken, Sensor sensor) {
        super();
        this.type = sensor.getType();
        this.value = value;
        this.dateTaken = dateTaken;
        this.sensor = sensor;
    }

    public Long getMeasureId() {
        return measureId;
    }
    public void setMeasureId(Long measureId) {
        this.measureId = measureId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Float getValue() {
        return value;
    }
    public void setValue(Float value) {
        this.value = value;
    }
    public Date getDateTaken() {
        return dateTaken;
    }
    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }
    public Sensor getSensor() {
        return sensor;
    }
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}