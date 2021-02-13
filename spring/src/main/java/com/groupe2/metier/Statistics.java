package com.groupe2.metier;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.groupe2.entities.Sensor;

public class Statistics implements Serializable {
    private Sensor sensor;
    private Date d1;
    private Date d2;
    private Long period;
    private String unit;

    private Float min;
    private long nb;
    private Float max;
    private Float average;

    public Statistics() {
        super();
    }

    public long getNb() {
        return nb;
    }

    public void setNb(long nb) {
        this.nb = nb;
    }

    public Statistics(Sensor sensor, Date d1, Date d2) {
        this.sensor = sensor;
        this.d1 = d1;
        this.d2 = d2;
        setPeriod();
        setUnit();
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }


    public Date getD1() {
        return d1;
    }

    public void setD1(Date d1) {
        this.d1 = d1;
    }

    public Date getD2() {
        return d2;
    }

    public void setD2(Date d2) {
        this.d2 = d2;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod() {
        this.period = TimeUnit.SECONDS.convert(Math.abs(this.getD2().getTime() - this.getD1().getTime()), TimeUnit.MILLISECONDS);
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit() {
        switch(this.sensor.getType()) {
            case "temperature":
                this.unit = "°C";
                break;
            case "hygrometry":
                this.unit = "%";
                break;
            case "co2":
                this.unit = "%";
                break;
            case "luminosity":
                this.unit = "lm";
                break;
            default:
                this.unit = "";
          }
    }

    public Float getMin() {
        return min;
    }

    public void setMin(Float min) {
        this.min = min;
    }

    
    
    public Float getMax() {
        return max;
    }

    public void setMax(Float max) {
        this.max = max;
    }

    

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

	
}
