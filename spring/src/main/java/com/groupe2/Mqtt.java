package com.groupe2;


import com.groupe2.metier.Metier;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mqtt implements MqttCallback {
    @Autowired
    private static Metier metier = new Metier();

    Boolean RECEIVED = false;
    String data;

    String receivedTopic;
    MqttClient myClient;
	MqttConnectOptions connOpt;

	static final String BROKER = "tcp://mosquitto:1883";
	static final String DEVICE_ID = "spring";

	static final Boolean SUB = true; //pour l'instant il lit juste pour stocker, il ne publie rien

	/*connectionLost : pour vérifier s'il y a perte de connexion du broker*/
    @Override
	public void connectionLost(Throwable t) {
        System.out.println("Connection lost : "+t.getMessage());
        System.out.println("msg "+t.getMessage());
        System.out.println("loc "+t.getLocalizedMessage());
        System.out.println("cause "+t.getCause());
        System.out.println("excep "+t);
	}

    /*deliveryComplete*/
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
    }

    /*messageArrived : quand un message est reçue sur le topic d'écoute*/
    @Override
    public void messageArrived(java.lang.String mqttTopic, MqttMessage message) throws Exception {

        System.out.println("-------------------------------------------------");
        System.out.println("| Topic: " + mqttTopic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");

        data = mqttTopic + "::" + new String(message.getPayload());
        receivedTopic = mqttTopic;

        RECEIVED = true;
    }

        /*runClient : créé le client, connexion au broker, souscription au topic(s), déconnexion*/
        public void runClient(Boolean ack) throws InterruptedException{
            // Création du client
            String clientID = DEVICE_ID;
            connOpt = new MqttConnectOptions();
            connOpt.setCleanSession(true);
            connOpt.setAutomaticReconnect(true);
            connOpt.setKeepAliveInterval(60);
            connOpt.setConnectionTimeout(30);

            // Connexion au broker
            try {
                myClient = new MqttClient(BROKER, clientID);
                myClient.setCallback(this);
                myClient.connect(connOpt);
                System.out.println("Connected to " + BROKER);
            } catch (MqttException e) {
                System.out.println(e.getMessage());
                Thread.sleep(10000);
                runClient(ack);
                //System.exit(-1);
            }

            if(ack == true) { metier.ackPublish(myClient, receivedTopic); }

            // Setup du topic
            String stringTopic = "#";
            MqttTopic.validate(stringTopic, true); /*autorise l'écoute de tous les topics*/

            // Abonnement au topic
            if (SUB) {
                try {
                    int subQoS = 0;
                    myClient.subscribe(stringTopic, subQoS);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            // Déconnexion
            while(!RECEIVED){
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            try {
                myClient.disconnect();
                System.out.println("Client disconnected.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
}