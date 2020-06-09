package wf.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import wf.entity.Weather;
import wf.repository.WeatherRepositoryImpl;
import wf.service.WeatherService;

@Controller
public class IndexController {
	@Autowired
	WeatherService weatherService;
	@Autowired
	WeatherRepositoryImpl weatherRepository;

	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException {

		return "index";
	}

	@RequestMapping("/temperature")
	public String temperatureHistory(@RequestParam("date") String date, Model model, HttpServletRequest request) {
		List<Weather> weather = weatherRepository.findByDay(date).orElse(null);
		model.addAttribute("weather", weatherService.sortWeather(weather));
		return "history";

	}
}
