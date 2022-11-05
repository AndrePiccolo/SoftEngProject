package com.douglas.rentDogWeb.model.database.repository;

import com.douglas.rentDogWeb.model.database.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    Optional<Contract> findContractByDogIdAndRentDate(Integer dogId, Date rentDate);
    Optional<List<Contract>> findContractsByRenterId(Integer renterId);
}
