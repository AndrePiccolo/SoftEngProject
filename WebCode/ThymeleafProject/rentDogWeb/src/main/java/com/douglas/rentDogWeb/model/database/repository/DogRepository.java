package com.douglas.rentDogWeb.model.database.repository;

import com.douglas.rentDogWeb.model.database.entity.Doggo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepository  extends JpaRepository<Doggo, Integer> {

    List<Doggo> findDoggoByDogNameContains(String dogName);
    List<Doggo> findDoggoByDogBreedContains(String dogBreed);
    List<Doggo> findDoggoByDogSizeContains(String dogSize);
}
