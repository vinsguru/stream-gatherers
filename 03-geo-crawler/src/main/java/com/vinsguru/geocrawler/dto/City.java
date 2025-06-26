package com.vinsguru.geocrawler.dto;

public record City(int id,
                   String name,
                   String state,
                   String country,
                   double latitude,
                   double longitude) {
}
