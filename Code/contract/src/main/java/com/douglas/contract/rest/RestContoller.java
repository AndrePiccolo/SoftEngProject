package com.douglas.contract.rest;

import com.douglas.contract.database.entity.Contract;
import com.douglas.contract.database.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(RestContoller.URL_MAPPING)
@RequiredArgsConstructor
public class RestContoller {

    public static final String URL_MAPPING = "/rentdog/v1";

    @Autowired
    ContractRepository contractRepository;

    @GetMapping("/contract")
    public ResponseEntity<ContractJson> sayHello(@RequestHeader(value="Authorization", required = true) String auth){
        Optional<Contract> contract = contractRepository.findById(1);
        ContractJson response = ContractJson.builder().id(contract.get().getId())
                .renterId(contract.get().getRenterId())
                .ownerID(contract.get().getOwnerID())
                .dogId(contract.get().getDogId())
                .createDate(contract.get().getCreateDate())
                .rentDate(contract.get().getRentDate())
                .contractStarted(contract.get().getContractStarted())
                .contractEnded(contract.get().getContractEnded())
                .contractConfirmation(contract.get().getContractConfirmation()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
