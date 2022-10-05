package com.douglas.contract.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class ContractJson {

    @JsonProperty("contract_id")
    private Integer id;

    @JsonProperty("renter_id")
    private Integer renterId;

    @JsonProperty("owner_id")
    private Integer ownerID;

    @JsonProperty("dog_id")
    private Integer dogId;

    @JsonProperty("contract_create_date")
    private Date createDate;

    @JsonProperty("contract_rent_date")
    private Date rentDate;

    @JsonProperty("contract_started")
    private Date contractStarted;

    @JsonProperty("contract_ended")
    private Date contractEnded;

    @JsonProperty("contract_confirmation")
    private Integer contractConfirmation;
}
