package com.vinsguru.geocrawler.dto;

import java.util.List;

public record SubRegion(int id,
                        int regionId,
                        String name,
                        List<Integer> countries) {
}
