package com.groupe2.metier;

import java.util.Date;
import java.util.List;

import com.groupe2.entities.User;
import com.groupe2.entities.Sensor;
import com.groupe2.entities.Measure;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.data.domain.Page;

public interface IMetier {
    public User getUser(String login);
    public User addUser(String login, String password, String name, String role, boolean active);
    public void rmUser(String login);
    public Sensor getSensor(Long sensorId);
    public Sensor addSensor(Long sensorId, String location, String type, Long frequency);
    public void rmSensor(Long sensorId);
    public Measure getMeasure(Long measureId);
    public Measure addMeasure(Float value, Date dateTaken, Long sensorId);
    public void rmMeasure(Long measureId);
    public void addUserSensor(Long sensorId, String login);
    public void rmUserSensor(Long sensorId, String login);
    public Page<Sensor> listeSensor(String login, int page, int size);
    public Page<Measure> listeMesure(Long sensorId, Date d1, Date d2, int page, int size);
    public String listeValue(List<Measure> list);
    public String listeDate(List<Measure> list);
    public Statistics statistic(List<Measure> list,Sensor sensor,Date d1, Date d2);
    public void updateFrequency(long sensorId, long frequency);
    public void receiveMQTT(String data);
    public void ackPublish(MqttClient myClient, String receivedTopic);
    public void frequencyPublish(Long sensorId, long frequency);
}