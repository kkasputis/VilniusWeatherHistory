package wf.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wf.entity.Weather;

@Repository
public interface WeatherRepositoryImpl extends JpaRepository<Weather, Long> {


	Optional<List<Weather>> findByDay(String date);
	Weather findOneByWeatherTime(LocalDateTime time);
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM weather WHERE day =?1", nativeQuery = true)
	void deleteAllDay(String date);
}
