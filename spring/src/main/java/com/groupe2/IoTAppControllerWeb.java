package com.groupe2;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.groupe2.entities.Measure;
import com.groupe2.entities.Sensor;
import com.groupe2.metier.IMetier;
import com.groupe2.metier.Statistics;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class IoTAppControllerWeb {
    @Autowired
	private IMetier metier;

	@GetMapping("/")
	public String home(Model model) {
		return "redirect:/sensors";
	}
	
	//Home page with sensor list
	@GetMapping("/sensors")
	public String home(Model model,
			Principal principal,
			Long sensorId,
			@RequestParam(name="page", defaultValue="0")int page) {
		try {
			//Liste of sensors
			Page<Sensor> pageSensor = metier.listeSensor(principal.getName(), page, 5);
			int[] sensorPages = new int[pageSensor.getTotalPages()];
			model.addAttribute("spage", page);	
			model.addAttribute("listSensor", pageSensor.getContent());		
			model.addAttribute("sensorPages", sensorPages);

			//check if sensorId isn't null
			if (sensorId == null) {
				return "redirect:/sensors?sensorId="+pageSensor.getContent().get(0).getSensorId();
			}

 			//Sensor config
			Sensor sensor = metier.getSensor(sensorId);
			model.addAttribute("sensorId", sensorId);
			model.addAttribute("sensor", sensor);
		} catch (Exception e) {
			model.addAttribute("exeption1", e);
		}
		return "home";
	}

	//add a known sensor to user list on home page
	@PostMapping("/addSensorToUser")
	public String addSensorToUser(Model model,
			Principal principal, 
			Long newSensorId) {
		try {
			metier.addUserSensor(newSensorId, principal.getName());
		} catch (Exception e) {
			model.addAttribute("exeption2", e);
		}
		return "redirect:/sensors";
	}

	//Consulte sensor'r Data
	@GetMapping("/data")
	public String data(Model model,
			Principal principal, 
			Long sensorId,
			@RequestParam("d1")@DateTimeFormat(pattern = "yyyy-MM-dd")Date d1,
			@RequestParam("d2")@DateTimeFormat(pattern = "yyyy-MM-dd")Date d2,
			@RequestParam(name="page", defaultValue="0")int page) {
		Sensor sensor = new Sensor();
		try {
			//return chosen dates to model
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			model.addAttribute("d1", sdf.format(d1));
			model.addAttribute("d2", sdf.format(d2));
			//Liste of measures
			sensor = metier.getSensor(sensorId);
			Page<Measure> pageMeasure = metier.listeMesure(sensorId, d1, d2, page, 10);
			int[] measurePages = new int[pageMeasure.getTotalPages()];
			model.addAttribute("sensor", sensor);
			model.addAttribute("sensorId", sensorId);
			model.addAttribute("page", page);
			model.addAttribute("listMeasure", pageMeasure.getContent());
			model.addAttribute("measurePages", measurePages);
			//Statistics and visualisation
			Page<Measure> pageMeasureall = metier.listeMesure(sensorId, d1, d2, 0, Integer.MAX_VALUE);
			String value = metier.listeValue(pageMeasureall.getContent());
			String date = metier.listeDate(pageMeasureall.getContent());
			Statistics stat = metier.statistic(pageMeasureall.getContent(),sensor,d1,d2);
			model.addAttribute("jsonvalue", value);
			model.addAttribute("jsondate", date);
			model.addAttribute("statistics", stat);
		} catch (Exception e) {
			model.addAttribute("exeption", e);
		}
		return "data";
	}

	//Change sensor's frequency
	@PostMapping("/changeFrequency")
	public String changeFrequency(Model model,
			Principal principal, 
			Long sensorId,
			long newFrequency) {
		try {
			metier.updateFrequency(sensorId,newFrequency); //update database
			metier.frequencyPublish(sensorId, newFrequency); //send new freq to sensor
		} catch (Exception e) {
			model.addAttribute("exeption3", e);
		}
		return "redirect:/sensors?sensorId="+sensorId;
	}

	//admin page
	@RequestMapping("/admin")
	public String admin(Model model) {
		return "admin";
	}

	//create new user on admin page
	@PostMapping("/newUser")
	public String newUser(Model model,
			Principal principal, 
			String newUserName,
			String newUserPassword,
			String newUserLogin) {
		
		try {
			metier.addUser(newUserLogin, newUserPassword, newUserName, "ROLE_USER", true);
		} catch (Exception e) {
			model.addAttribute("exeption", e);
		}
		return "/admin";
	}

	

}