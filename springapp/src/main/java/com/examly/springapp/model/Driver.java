package com.examly.springapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
@Entity
public class Driver {
    // day 1
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long driverId;
    private String driverName;
    private String licenseNumber;
    private int experienceYears;
    private String contactNumber;
    private String availabilityStatus; // allowed values -- Active , Inactive On Leave
    private String address;
    private String vehicleType; // sedan suv bike
    private Double hourlyRate;
    private boolean isDeleted=false;

    // Marked with @Lob and stored as a LONGBLOB
    // taken from the copilot
    // Nullable:true
    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = true) // Base-64 encoded image fo the driver
    private String image;

}
