package backend.map.service;

import backend.map.entity.City;
import backend.map.entity.dto.GuMapInfoDto;
import backend.map.entity.dto.SpotInfoDto;
import backend.map.entity.Gu;
import backend.map.entity.Spot;
import backend.map.repository.CityRepository;
import backend.map.repository.GuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class GuServiceImpl implements GuService{

    private final GuRepository guRepository;
    private final CityRepository cityRepository;

    @Override
    public List<GuMapInfoDto> getGuMap(String cityCode) {

        Optional<City> city = cityRepository.findByCityCode(cityCode);
        List<Gu> guList = guRepository.findGuByCity(city.get());
        List<GuMapInfoDto> guMapInfoDtoList = new ArrayList<>();
        for(int i=0; i<guList.size(); i++){
            Gu gu = guList.get(i);

            GuMapInfoDto guMapInfoDto = new GuMapInfoDto(gu.getGuCode(), gu.getGuName(), gu.getVector(), gu.getCity().getGrade());
            guMapInfoDtoList.add(guMapInfoDto);
        }
        log.info("CityServiceImpl/getCityMap/guMapInfoDtoList = {}", guMapInfoDtoList);

        return guMapInfoDtoList;

    }

    @Override
    public List<SpotInfoDto> getSpotList(String guCode) {

        Optional<Gu> gu = guRepository.findByGuCode(guCode);
        List<Spot> spotlist = gu.get().getSpotlist();
        log.info("CityServiceImpl/getSpotList/spotlist = {}", spotlist);
        List<SpotInfoDto> spotInfoDtoList = new ArrayList<>();
        for(int i=0; i<spotlist.size(); i++){
            Spot spot = spotlist.get(i);
            SpotInfoDto spotInfoDto = new SpotInfoDto(spot.getSpotName(), spot.getComment());
            spotInfoDtoList.add(spotInfoDto);
        }
        log.info("CityServiceImpl/getSpotList/spotInfoDtoList = {}", spotInfoDtoList);

        return spotInfoDtoList;
    }


}
