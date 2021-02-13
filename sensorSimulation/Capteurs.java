//package org.exemple.demo;
import java.util.*;
import java.util.concurrent.ExecutorService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class Capteurs {

    private MqttClient client;
    private String topic;
    boolean ack;

    public Capteurs(MqttClient client, String topic) {
        this.client = client;
        this.topic = topic;
    }

    
    public Void callMeasure() throws Exception {        
        if ( !client.isConnected()) {
            return null;
        }           
        MqttMessage msg = readCapteur();
        msg.setQos(0);
        //msg.setRetained(true);
        client.publish(this.topic,msg); 
        System.out.println("Message envoyé :"+new String(msg.getPayload()));      
        return null;        
    }

    public Void callDescription() throws Exception{
        if ( !client.isConnected()) {
            return null;
        }           
        MqttMessage msg = description();
        msg.setQos(0);
        //msg.setRetained(true);
        client.publish(this.topic,msg);
        System.out.println("Message envoyé :"+new String(msg.getPayload()));         
        return null;        
    }

    private MqttMessage readCapteur() {
	Random rnd = new Random();	    
        double temp =  80 + rnd.nextDouble() * 20.0;        
        byte[] payload = String.format("%04.2f",temp)
          .getBytes();        
        return new MqttMessage(payload);           
    }

    private MqttMessage description() {	   
            String str = new String("temperature;Lille;24");     
            byte[] payload = str.getBytes();        
            return new MqttMessage(payload);           
        }
}
