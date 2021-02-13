package com.groupe2;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.groupe2.entities.User;
import com.groupe2.entities.Sensor;
import com.groupe2.entities.Measure;
import com.groupe2.metier.IMetier;

@SpringBootApplication
public class IoTApp implements CommandLineRunner {
	@Autowired
	private IMetier metier;

	public static void main(String[] args) {
		SpringApplication.run(IoTApp.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		//Setting some values for tests
		User u1 = metier.addUser("pifou", "glopglop", "pifou le fou", "ROLE_ADMIN", true);
		Sensor s1 = metier.addSensor(Long.valueOf(11), "Lille", "temperature", Long.valueOf(24));
		metier.addUserSensor(s1.getSensorId(), u1.getLogin());
		// Generate random values
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 23; j++) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, -i);
				c.set(Calendar.HOUR_OF_DAY, j);
				Date d = c.getTime();
				metier.addMeasure((float) ((Math.random() * ((25 - 10) + 1)) + 10), d, s1.getSensorId());
			}
		}
		metier.addMeasure((float) (-5), new Date(), s1.getSensorId());

		
		//connect to MQTT brocker and listen
		Mqtt client = new Mqtt();

        while(true){
			Boolean ack = false;
            if(client.RECEIVED == true){
                try {
					metier.receiveMQTT(client.data);
					ack = true;
                } catch (Exception e) {
					System.out.println(e.getMessage());
					ack = false;
                }
                client.RECEIVED = false;
            }
			client.runClient(ack);
        }
	}
}		