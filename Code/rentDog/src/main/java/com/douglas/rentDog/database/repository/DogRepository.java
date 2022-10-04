package com.douglas.rentDog.database.repository;

import com.douglas.rentDog.database.entity.Doggo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository  extends JpaRepository<Doggo, Integer> {
}
