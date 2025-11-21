package com.vinsguru.geocrawler.client;

import com.vinsguru.geocrawler.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class GeoDataClient {

    private static final String REGIONS = "/regions";
    private static final String SUBREGION_BY_ID = "/subregions/%d";
    private static final String COUNTRY_BY_ID = "/countries/%d";
    private static final String STATE_BY_ID = "/states/%d";
    private static final String CITY_BY_ID = "/cities/%d";

    private final RestClient restClient;

    public GeoDataClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Region> getRegions() {
        return this.restClient.get()
                              .uri(REGIONS)
                              .retrieve()
                              .body(new ParameterizedTypeReference<>() {
                              });
    }

    public SubRegion getSubRegion(int id) {
        return this.get(SUBREGION_BY_ID.formatted(id), SubRegion.class);
    }

    public Country getCountry(int id) {
        return this.get(COUNTRY_BY_ID.formatted(id), Country.class);
    }

    public State getState(int id) {
        return this.get(STATE_BY_ID.formatted(id), State.class);
    }

    public City getCity(int id) {
        return this.get(CITY_BY_ID.formatted(id), City.class);
    }

    private <T> T get(String uri, Class<T> type) {
        return this.restClient.get()
                              .uri(uri)
                              .retrieve()
                              .body(type);
    }
}
