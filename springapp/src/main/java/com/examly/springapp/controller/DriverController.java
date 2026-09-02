package com.examly.springapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.Driver;
import com.examly.springapp.service.DriverService;

@RestController
@RequestMapping("/api")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/driver")
    public ResponseEntity<Driver> addDriver(@RequestBody Driver driver) {
        try {
            Driver createdDriver = driverService.addDriver(driver);
            return new ResponseEntity<>(createdDriver, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long driverId) {

        Optional<Driver> driver = driverService.getDriverById(driverId);

        if (driver.isPresent()) {
            return new ResponseEntity<>(driver.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/driver")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        try {
            List<Driver> drivers = driverService.getAllDrivers();

            if (drivers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(drivers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/driver/{driverId}")
    public ResponseEntity<Driver> updateDriver(@PathVariable Long driverId,
                                               @RequestBody Driver driver) {

        Driver updatedDriver = driverService.updateDriver(driverId, driver);

        if (updatedDriver != null) {
            return new ResponseEntity<>(updatedDriver, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/driver/{driverId}")
    public ResponseEntity<Driver> deleteDriver(@PathVariable Long driverId) {

        Driver deletedDriver = driverService.deleteDriver(driverId);

        if (deletedDriver != null) {
            return new ResponseEntity<>(deletedDriver, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}