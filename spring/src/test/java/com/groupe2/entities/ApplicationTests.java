package com.groupe2.entities;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.groupe2.metier.IMetier;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	/*@Test
	void contextLoads() {
	}*/
	@Test
	void testGetSensor(){
		Sensor sensor2 = new Sensor(Long.valueOf(11), "Lille", Long.valueOf(24), "temperature");
		assertNotNull(sensor2.getFrequency());
		assertNotNull(sensor2.getLocation());
		assertNotNull(sensor2.getSensorId());
		assertNotNull(sensor2.getType());	
	}
	@Test
	void testSetSensor(){
		User user = new User("pifou", "glopglop", "pifou le fou", "ROLE_ADMIN", true);
		Sensor sensor1 = new Sensor();
		sensor1.setFrequency(Long.valueOf(24));
		sensor1.setLocation("Lyon");
		sensor1.setSensorId(Long.valueOf(11));
		sensor1.setType("test");
		assertNull(sensor1.getUsers());
		assertNull(sensor1.getMeasures());
		sensor1.setUsers(new ArrayList<User>());
		sensor1.setMeasures(new ArrayList<Measure>());
		sensor1.addUser(user);
		assertNotNull(sensor1.getUsers());
		sensor1.rmUser(user);
		assertTrue(sensor1.getUsers().equals(new ArrayList<User>()));
		assertTrue(sensor1.getMeasures().equals(new ArrayList<Measure>()));

		assertNotNull(sensor1.getFrequency());
		assertNotNull(sensor1.getLocation());
		assertNotNull(sensor1.getSensorId());
		assertNotNull(sensor1.getType());	
	}
	@Test
	void testGetMeasure(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -2);
		c.set(Calendar.HOUR_OF_DAY, 2);
		Date d = c.getTime();
		Sensor sensor = new Sensor(Long.valueOf(11), "Lille", Long.valueOf(24), "temperature");

		Measure measure2 = new Measure((float) ((Math.random() * ((25 - 10) + 1)) + 10), d, sensor);
		assertNotNull(measure2.getSensor());
		assertNotNull(measure2.getDateTaken());
		assertNotNull(measure2.getValue());
		assertNotNull(measure2.getType());		
	}

	@Test
	void testSetMeasure(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -2);
		c.set(Calendar.HOUR_OF_DAY, 2);

		Date d = c.getTime();
		Sensor sensor = new Sensor(Long.valueOf(11), "Lille", Long.valueOf(24), "temperature");
		Measure measure1 = new Measure();
		
		measure1.setValue((float) ((Math.random() * ((25 - 10) + 1)) + 10));
		measure1.setDateTaken(d);;
		measure1.setSensor(sensor);
		measure1.setType(sensor.getType());
		measure1.setMeasureId(Long.valueOf(5));
		assertNotNull(measure1.getMeasureId());
		assertNotNull(measure1.getValue());
		assertNotNull(measure1.getDateTaken());
		assertNotNull(measure1.getType());
		
	}

	@Test
	void testGetUser(){
		User user2 = new User("pifou", "glopglop", "pifou le fou", "ROLE_ADMIN", true);
		assertNotNull(user2.getName());
		assertNotNull(user2.getActive());
		assertNotNull(user2.getPassword());
		assertNotNull(user2.getRole());
		assertNotNull(user2.getLogin());
		assertNull(user2.getSensors());
	}

	@Test
	void testSetUser(){
		User user = new User();
		Sensor sensor2 = new Sensor(Long.valueOf(11), "Lille", Long.valueOf(24), "temperature");
		
		user.setActive(true);
		user.setLogin("toto");
		user.setName("yo mec");
		user.setPassword("easy");
		user.setRole("ROLE_ADMIN");
		assertNotNull(user.getActive());
		assertNotNull(user.getLogin());
		assertNotNull(user.getName());
		assertNotNull(user.getPassword());
		assertNotNull(user.getRole());

		user.setSensors(new ArrayList<Sensor>() );
		user.addSensor(sensor2);
		assertNotNull(user.getSensors());
		user.rmSensor(sensor2);	
		
	}

}
