package com.douglas.rentDogWeb.model.database.repository;

import com.douglas.rentDogWeb.model.database.entity.Customer;
import com.douglas.rentDogWeb.model.database.entity.Doggo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepository  extends JpaRepository<Doggo, Integer> {

    List<Doggo> findDoggoByDogNameContainsAndDogActive(String dogName, Integer active);
    List<Doggo> findDoggoByDogBreedContainsAndDogActive(String dogBreed, Integer active);
    List<Doggo> findDoggoByDogSizeContainsAndDogActive(String dogSize, Integer active);
    List<Doggo> findDoggoByDogActiveAndCustomerIsNot(Integer active, Customer customer);
}
