package backend.map.repository;

import backend.map.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findCityByCityName(String cityName);
}