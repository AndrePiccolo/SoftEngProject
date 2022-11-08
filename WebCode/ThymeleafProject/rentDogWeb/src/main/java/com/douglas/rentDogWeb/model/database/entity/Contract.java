package com.douglas.rentDogWeb.model.database.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "contract_started")
    private String contractStarted;

    @Column(name = "contract_ended")
    private String contractEnded;

    @Column(name = "confirm_contract")
    private Integer contractConfirmation;
}