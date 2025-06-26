package com.vinsguru.geocrawler.repository;

import com.vinsguru.geocrawler.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer> {

}
