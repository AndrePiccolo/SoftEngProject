package com.douglas.rentDogWeb.model.database.repository;

import com.douglas.rentDogWeb.model.database.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findCustomerByCustomerEmailEquals(String email);
}
