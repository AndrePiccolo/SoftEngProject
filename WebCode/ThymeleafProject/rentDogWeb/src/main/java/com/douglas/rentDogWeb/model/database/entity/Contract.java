package com.douglas.rentDogWeb.model.database.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id", nullable = false)
    private Integer id;

    @Column(name = "renter_id", nullable = false)
    private Integer renterId;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerID;

    @Column(name = "doggo_id", nullable = false)
    private Integer dogId;

    @Temporal(TemporalType.DATE)
    @Column(name = "contract_create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "contract_rent_date", nullable = false)
    private Date rentDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "contract_started")
    private Date contractStarted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "contract_ended")
    private Date contractEnded;

    @Column(name = "confirm_contract")
    private Integer contractConfirmation;
}