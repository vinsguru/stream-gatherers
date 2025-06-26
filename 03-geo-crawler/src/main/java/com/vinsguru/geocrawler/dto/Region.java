package com.vinsguru.geocrawler.dto;

import java.util.List;

public record Region(int id,
                     String name,
                     List<Integer> subRegions) {
}
