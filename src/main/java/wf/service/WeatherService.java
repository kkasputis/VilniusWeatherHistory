package wf.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import wf.entity.Weather;
import wf.repository.WeatherRepositoryImpl;

@Service
public class WeatherService {
	@Autowired
	WeatherRepositoryImpl weatherRepository;

	@Async
	public void saveWeatherDay(String date) throws JsonParseException, JsonMappingException, IOException {
		if ((weatherRepository.findByDay(date).orElse(null) == null) || (weatherRepository.findByDay(date).get().size() < 47)) {
		DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(format2)
				.parseDefaulting(ChronoField.HOUR_OF_DAY, 0).parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
				.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0).toFormatter();

		LocalDateTime dateToGet = LocalDateTime.parse(date, formatter);
		URL url = new URL("https://api.climacell.co/v3/weather/historical/station?start_time="
				+ dateToGet.minusHours(3).format(format1) + "&end_time=" + dateToGet.plusDays(1).minusHours(3).format(format1)
				+ "&lat=54.6872&lon=25.2797&unit_system=si&fields=temp&apikey=0DrZNIIpuH0Uc0w2w0NmndYP2CWZWgy2");
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

		List<Weather> weatherList = new Gson().fromJson(in, new TypeToken<List<Weather>>() {
		}.getType());
		weatherRepository.deleteAllDay(date);
		for (Weather weather : weatherList) {
			weather.setTemperature(Float.valueOf(weather.getTemp().get("value")));
			weather.setWeatherTime(
					LocalDateTime.parse(weather.getObservation_time().get("value"), format1).plusHours(3));
			weather.setDay(weather.getWeatherTime().format(format2));
			weatherRepository.save(weather);
		}
		}
	}

	@PostConstruct
	@Async
	public void LastMonthInfo() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		for (long x = 0; x <= 31; x++) {
			try {
				saveWeatherDay(LocalDateTime.now().minusDays(x).format(formatter));
			} catch (JsonParseException e) {
				System.out.println("Json'o klaida: " + e);
				e.printStackTrace();
			} catch (JsonMappingException e) {
				System.out.println("Json'o klaida: " + e);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Klaida: " + e);
				e.printStackTrace();
			}
		}
	}

	public void saveWeatherHour() throws JsonParseException, JsonMappingException, IOException {
		DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
		DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		Instant now = Instant.now() ;
		ZoneId z = ZoneId.of( "Europe/London" ) ;  
		ZonedDateTime dateToGet = now.atZone( z ) ;
		URL url = new URL("https://api.climacell.co/v3/weather/historical/climacell?start_time="
				+ dateToGet.minusHours(2).format(format1) + "&end_time=" + dateToGet.minusHours(1).format(format1)
				+ "&lat=54.6872&lon=25.2797&timestep=30&unit_system=si&fields=temp&apikey=0DrZNIIpuH0Uc0w2w0NmndYP2CWZWgy2");
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

		List<Weather> weatherList = new Gson().fromJson(in, new TypeToken<List<Weather>>() {
		}.getType());
	
		for (Weather weather : weatherList) {
			weather.setTemperature(Float.valueOf(weather.getTemp().get("value")));
			weather.setWeatherTime(
					LocalDateTime.parse(weather.getObservation_time().get("value"), format1).plusHours(3));
			weather.setDay(weather.getWeatherTime().format(format2));
			if (weatherRepository.findOneByWeatherTime(weather.getWeatherTime()) == null) { weatherRepository.save(weather); }
			
		}

	}
	public List<Weather> sortWeather(List<Weather> weather) {
		if (weather != null) {
		Collections.sort(weather, new Comparator<Weather>() {
			  @Override
			  public int compare(Weather u1, Weather u2) {
			    return u1.getWeatherTime().compareTo(u2.getWeatherTime());
			  }
			});
		return  weather;
		}
		else { return null; }
	}

	@Async
	@Scheduled(cron = "0 20 * * * *")
	public void hourlyUpdate() throws JsonParseException, JsonMappingException, IOException {
		saveWeatherHour();
	}

}
