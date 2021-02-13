package com.groupe2.metier;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.DoubleSummaryStatistics;

import com.groupe2.entities.User;
import com.groupe2.dao.UserRepository;
import com.groupe2.entities.Sensor;
import com.groupe2.dao.SensorRepository;
import com.google.gson.JsonArray;
import com.groupe2.dao.MeasureRepository;
import com.groupe2.entities.Measure;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.*;

@Service
@Transactional
public class Metier implements IMetier {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private MeasureRepository measureRepository;

    @Override
    public User getUser(String login) {
        Optional<User> u = userRepository.findById(login);
        if (!u.isPresent()) throw new RuntimeException("User not found");
        return u.get();
    }

    @Override
    public User addUser(String login, String password, String name, String role, boolean active) {
        try {
            getUser(login); //test if user already exists
        } catch (RuntimeException e) {
            return userRepository.save(new User(login, new BCryptPasswordEncoder().encode(password), name, role, active));
        }
        throw new RuntimeException("User already exists"); //if user already exists
    }

    @Override
    public void rmUser(String login) {
        User user = getUser(login); //test if user already exists, throw RuntimeException if so
        userRepository.deleteById(user.getLogin());

    }

    @Override
    public Sensor getSensor(Long sensorId) {
        Optional<Sensor> s = sensorRepository.findById(sensorId);
        if(!s.isPresent()) throw new RuntimeException("Sensor not found");
        return s.get();
    }

    @Override
    public Sensor addSensor(Long sensorId, String location, String type, Long frequency) {
        try{
            Sensor s = getSensor(sensorId); //test if sensor already exists
            return s; //return if exists
        } catch(RuntimeException e){
            return sensorRepository.save(new Sensor(sensorId, location, frequency, type));
        }
    }

    @Override
    public void rmSensor(Long sensorId) {
        Sensor sensor = getSensor(sensorId); //test if sensor already exists, throw RuntimeException if so
        sensorRepository.deleteById(sensor.getSensorId());
    }

    @Override
    public void updateFrequency(long sensorId, long frequency) {
        Sensor sensor = getSensor(sensorId); //test if sensor already exists, throw RuntimeException if so
        sensor.setFrequency(frequency);
        sensorRepository.save(sensor);
    }

    @Override
    public Measure getMeasure(Long measureId) {
        Optional<Measure> m = measureRepository.findById(measureId);
        if(!m.isPresent()) throw new RuntimeException("Measure not found");
        return m.get();
    }

    @Override
    public Measure addMeasure(Float value, Date dateTaken, Long sensorId) {
        Sensor sensor = getSensor(sensorId);
        return measureRepository.save(new Measure(value, dateTaken, sensor));
    }
    
    @Override
    public void rmMeasure(Long measureId) {
        //todo?
    }

    //Add User - Sensor Association
    @Override
    public void addUserSensor(Long sensorId, String login) {
        Sensor sensor = getSensor(sensorId);  //test if sensor exists
        User user = getUser(login); //test if user exists
        
        //Check if association already exists
        List<Sensor> list = listeSensor(login, 0, Integer.MAX_VALUE).getContent();
        if(list.contains(getSensor(sensorId))) throw new RuntimeException("Sensor already in list");

        sensor.addUser(user);
        user.addSensor(sensor);
        userRepository.save(user);
        sensorRepository.save(sensor);
    }

    //Remove User - Sensor Association
    @Override
    public void rmUserSensor(Long sensorId, String login) {
        Sensor sensor = getSensor(sensorId);
        User user = getUser(login);
        sensor.rmUser(user);
        user.rmSensor(sensor);
        userRepository.save(user);
        sensorRepository.save(sensor);
    }

    //List sensors for a specific user
    @Override
    public Page<Sensor> listeSensor(String login, int page, int size) {
        return sensorRepository.listSensor(login, PageRequest.of(page, size, Sort.unsorted())); //query in repository
    }

    //List measures for a specific sensor
    @Override
    public Page<Measure> listeMesure(Long sensorId, Date d1, Date d2, int page, int size) {
        //if negative dates
        if (d1.compareTo(d2) > 0) {
            d1 = d2;
        }
        Calendar c = Calendar.getInstance();
        //set d1 time to 00:00:00.0
        c.setTime(d1);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,30);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        d1 = c.getTime();
        //set d2 time to 23:59:59.0
        c.setTime(d2);
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);
        c.set(Calendar.MILLISECOND,0);
        d2 = c.getTime();
        return measureRepository.listMeasure(sensorId, d1, d2, PageRequest.of(page, size, Sort.unsorted())); //query in repository
    }


    //Statistics

    //convert measure value list to JSON
    @Override
    public String listeValue(List<Measure> list) { 
        JsonArray json= new JsonArray();
        for(Measure L : list){
            json.add(L.getValue());
        }
        return json.toString();
    }

    //convert date list to JSON
    @Override
    public String listeDate(List<Measure> list){
        JsonArray json= new JsonArray();
        for(Measure L : list){
            json.add(L.getDateTaken().toString());
        }
        return json.toString();
    }

    //calculate statistic object for a specific list of measures
    @Override
    public Statistics statistic(List<Measure> list,Sensor sensor,Date d1, Date d2){
        Statistics stat = new Statistics(sensor,d1,d2);
        DoubleSummaryStatistics dstat = new DoubleSummaryStatistics();
        for(Measure L : list){
            dstat.accept((double)L.getValue());
        }
        stat.setMax((float)dstat.getMax());
        stat.setMin((float)dstat.getMin());
        stat.setAverage((float)dstat.getAverage());
        stat.setNb(dstat.getCount());
        return stat;
    }

    //MQTT

    public void receiveMQTT(String data){
        String[] parts = data.split("::");
		String topic = parts[0];
        String message = parts[1];
        
        Long sensorId = Long.valueOf(topic);
        
        switch(StringUtils.countOccurrencesOf(message, ";")){
            case 2 :
                String[] cutMessage = message.split(";");
                try{
                    addSensor(sensorId, cutMessage[1], cutMessage[0], Long.valueOf(cutMessage[2]));
                } catch (Exception ex){
                    ex.printStackTrace();
                }
                break;
            case 0 : 
                try{
                    getSensor(sensorId);
                    addMeasure(Float.valueOf(message), new Date(), sensorId);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    throw new RuntimeException(ex.getMessage());
                }
                break;
            default :
                System.out.println("Error : wrong MQTT payload format.");
                throw new RuntimeException("Error : wrong MQTT payload format.");
        }
    }

    public void ackPublish(MqttClient myClient, String receivedTopic){
        String pubMsg = "received";
        int pubQoS = 0;
        MqttTopic topic = myClient.getTopic(receivedTopic);

        MqttMessage message = new MqttMessage(pubMsg.getBytes());
        message.setQos(pubQoS);
        message.setRetained(false);

        // Publish the message
        System.out.println("Publishing ack to topic \"" + receivedTopic + "\" qos " + pubQoS);
        MqttDeliveryToken token = null;
        try {
            // publish message to broker
            token = topic.publish(message);
            // Wait until the message has been delivered to the broker
            token.waitForCompletion();
            Thread.sleep(100);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void frequencyPublish(Long sensorId, long frequency){
        String BROKER = "tcp://mosquitto:1883";
        String DEVICE_ID = "spring_freq";
        int pubQoS = 0;
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(true);
        connOpt.setAutomaticReconnect(true);
        connOpt.setKeepAliveInterval(60);
        connOpt.setConnectionTimeout(30);
        
        System.out.println("Publishing ack to topic \"" + sensorId + "\" qos " + pubQoS);
        MqttDeliveryToken token = null;
        try{
            MqttClient myClient = new MqttClient(BROKER, DEVICE_ID);
            myClient.connect(connOpt);
            String pubMsg = "frequency:" + String.valueOf(frequency);
            System.out.println(pubMsg);
            
            MqttTopic topic = myClient.getTopic(String.valueOf(sensorId));
            
            MqttMessage message = new MqttMessage(pubMsg.getBytes());
            message.setQos(pubQoS);
            message.setRetained(false);

            // publish message to broker
            token = topic.publish(message);
            // Wait until the message has been delivered to the broker
            token.waitForCompletion();

            Thread.sleep(100);
            myClient.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}