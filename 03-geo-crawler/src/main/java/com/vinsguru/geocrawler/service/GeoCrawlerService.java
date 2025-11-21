package com.vinsguru.geocrawler.service;

import com.vinsguru.geocrawler.client.GeoDataClient;
import com.vinsguru.geocrawler.dto.City;
import com.vinsguru.geocrawler.entity.CityEntity;
import com.vinsguru.geocrawler.repository.CityRepository;
import com.vinsguru.geocrawler.util.GatherersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.stream.Gatherers;

@Service
public class GeoCrawlerService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(GeoCrawlerService.class);

    private final GeoDataClient geoDataClient;
    private final CityRepository cityRepository;

    public GeoCrawlerService(GeoDataClient geoDataClient, CityRepository cityRepository) {
        this.geoDataClient = geoDataClient;
        this.cityRepository = cityRepository;
    }

    @Override
    public void run(String... args) {
        log.info("records count before: {}", this.cityRepository.count());
        this.geoDataClient.getRegions()
                          .stream()
                          .flatMap(region -> region.subRegions().stream())
                          .gather(GatherersUtil.executeConcurrent(this.geoDataClient::getSubRegion))
                          .flatMap(subRegion -> subRegion.countries().stream())
                          .gather(GatherersUtil.executeConcurrent(this.geoDataClient::getCountry))
                          .flatMap(country -> country.states().stream())
                          .gather(GatherersUtil.executeConcurrent(this.geoDataClient::getState))
                          .flatMap(state -> state.cities().stream())
                          .gather(GatherersUtil.executeConcurrent(this.geoDataClient::getCity))
                          .map(this::toEntity)
                          .gather(Gatherers.windowFixed(1000))
                          .forEach(this.cityRepository::saveAll);
        log.info("records count after: {}", this.cityRepository.count());
    }

    /*
     * Is it ok to use external id as primary key?
     * It depends! In this case, It is ok assuming the external id will be stable and unique!
     * Or generate your own id and we can introduce one more column in our table - external_id or geo_city_id - to map city.id()
     * */
    private CityEntity toEntity(City city) {
        var entity = new CityEntity();
        entity.setId(city.id());
        entity.setName(city.name());
        entity.setCountry(city.country());
        entity.setState(city.state());
        entity.setLatitude(city.latitude());
        entity.setLongitude(city.longitude());
        return entity;
    }

}
