package com.douglas.rentDogWeb.controller.entity;

import com.douglas.rentDogWeb.model.database.entity.Contract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class ContractResponse {
    private String ownerName;
    private String dogName;
    private Contract contract;
    private Double price;
}
