package com.vinsguru.geocrawler.dto;

import java.util.List;

public record Country(int id,
                      int subRegionId,
                      String name,
                      List<Integer> states) {
}
