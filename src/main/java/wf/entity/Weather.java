package wf.entity;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "weather")
public class Weather {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Transient
	private Map<String, String> temp;
	private float temperature;
	@Transient
	private Map<String, String> observation_time;
	private LocalDateTime weatherTime;
	private String day;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Map<String, String> getTemp() {
		return temp;
	}

	public void setTemp(Map<String, String> temp) {
		this.temp = temp;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public Map<String, String> getObservation_time() {
		return observation_time;
	}

	public void setObservation_time(Map<String, String> observation_time) {
		this.observation_time = observation_time;
	}

	public LocalDateTime getWeatherTime() {
		return weatherTime;
	}

	public void setWeatherTime(LocalDateTime weatherTime) {
		this.weatherTime = weatherTime;
	}

}
