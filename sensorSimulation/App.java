import java.util.*;
import java.lang.*;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class App implements MqttCallback
{
	static Long freq= Long.valueOf(30000);
	static boolean ack=false;
	 
	public App() {
	}
    public static void main( String[] args )
    {
		String publisherId = UUID.randomUUID().toString();
		String topic = String.valueOf((int) (Math.random() * 100000));
		App capteur = new App();
		MqttClient client;
		Long ref = System.currentTimeMillis();
		Long time = ref;
		

		try{
			client = new MqttClient("tcp://mosquitto:1883",publisherId);
			capteur.sub(client,topic);
			while(true){
				if(time-ref>freq){
					if(ack==true)
						capteur.pubMeasure(client,topic);
					else
						capteur.pubDescription(client,topic);
					
						ref=time;
				}
				else{
					time=System.currentTimeMillis();
				}
			}
		}catch(MqttException me){
			System.out.println("msg "+me.getMessage());
		}
		catch(Exception e){
			System.out.println("msg"+e.getMessage());
		}


    }
    @Override
    public void messageArrived(String topic, MqttMessage msg){
		System.out.println(msg.toString());
		String ackMessage = new String(msg.getPayload());

		switch(ackMessage.indexOf(":")){
            case -1 : 
				if(ackMessage.equals("received")){
					ack=true;
				}
                break;
            default :
				String[] cutMessage = ackMessage.split(":");
				if((cutMessage[0]).equals("frequency")){
					try{
						freq = Long.valueOf(86400000)/Long.valueOf(cutMessage[1]);
					} catch (Exception ex){
						ex.printStackTrace();
					}
				}
        }
    }

    
    @Override
    public void connectionLost(Throwable cause) {

	}
	
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

	}
	
	public void pubMeasure(MqttClient pub, String topic) throws MqttException, Exception{
	    Capteurs capteurTest = new Capteurs(pub, topic);
		capteurTest.callMeasure();

	}

	public void pubDescription(MqttClient pub, String topic) throws MqttException, Exception{
	    Capteurs capteurTest = new Capteurs(pub, topic);
		capteurTest.callDescription();

	}

	public void sub(MqttClient sub, String topic) throws MqttException{
		MqttConnectOptions options = new MqttConnectOptions();
	    options.setAutomaticReconnect(true);
	    options.setCleanSession(true);
	    options.setConnectionTimeout(10);
	    sub.connect(options);
		sub.setCallback(this);
		sub.subscribe(topic);
	}
}
