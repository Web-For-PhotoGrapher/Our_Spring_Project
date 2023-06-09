package backend.map.service;

import backend.map.api.ParticulateMatter;
import backend.map.api.Weather;
import backend.map.entity.dto.CityMapInfoDto;
import backend.map.entity.City;
import backend.map.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService{

    private final CityRepository cityRepository;
    private final ParticulateMatter particulateMatter;
    private final Weather weather;

    @Override
    @Transactional
    public List<CityMapInfoDto> getCityMap() {

        // api를 활용해 grade를 판단하는 로직은 다른 thread 에서 하루에 한번씩 데이터가 들어가도록 설정 예정

        List<City> cityList = cityRepository.findAll();
        List<CityMapInfoDto> cityMapInfoDtoList = new ArrayList<>();
        for(int i=0; i<cityList.size(); i++){
            City city = cityList.get(i);
            CityMapInfoDto cityMapInfoDto = new CityMapInfoDto(city.getCityCode(), city.getCityName(), city.getGrade(), city.getVector());
            cityMapInfoDtoList.add(cityMapInfoDto);
        }

        return cityMapInfoDtoList;
    }

    @Transactional
    @Scheduled(cron = "0 0 9-22 * * *" ) //매일 오전 9시부터 오후 10시까지 진행.
//    @Scheduled(cron = "30 * * * * *" ) //30초 반복 -> test용으로 사용한다.
    public void updateMapGrade() throws IOException, ParseException {

        log.info("CityServiceImpl/updateMapGrade/start");

        List<City> cities = cityRepository.findAll();

        int updateGrade;
        int pGrade;
        int wGrade;

        for(int i=0; i<cities.size(); i++){
            City city = cities.get(i);
            String cityName = city.getCityName();
            String xCode = city.getXCode();
            String yCode = city.getYCode();

            pGrade = particulateMatter.extractParticulateCityGrade(cityName);
            wGrade = weather.extractWeatherCityGrade(xCode, yCode);//서울에 해당하는 코드

            updateGrade = (pGrade + wGrade) / 2;
            //grade 변경
            city.updateGrade(updateGrade);
        }

        log.info("CityServiceImpl/updateMapGrade/done");

    }

}
