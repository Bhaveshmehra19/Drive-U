package com.examly.springapp.controller;

import com.examly.springapp.model.DriverRequest;
import com.examly.springapp.service.DriverRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/driverRequest")
@CrossOrigin(origins = "*")
public class DriverRequestController {

    @Autowired
    private DriverRequestService driverRequestService;

    // POST /api/driverRequest  (Customer)  -> 201 | 401/409
    @PostMapping
    public ResponseEntity<?> addDriverRequest(@RequestBody DriverRequest driverRequest) {
        try {
            DriverRequest saved = driverRequestService.addDriverRequest(driverRequest);
            if (saved == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 201
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409
        }
    }

    // GET /api/driverRequest/{driverRequestId}  (Admin, Customer) -> 200 | 204 | 404
    @GetMapping("/{driverRequestId}")
    public ResponseEntity<?> getDriverRequestById(@PathVariable Long driverRequestId) {
        Optional<DriverRequest> req = driverRequestService.getDriverRequestById(driverRequestId);
        if (req.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(req.get()); // 200
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
    }

    // GET /api/driverRequest/user/{userId}  (Customer) -> 200 | 404
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getDriverRequestsByUserId(@PathVariable Long userId) {
        try {
            List<DriverRequest> list = driverRequestService.findDriverRequestsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK).body(list); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // GET /api/driverRequest  (Admin) -> 200 | 204 | 400
    @GetMapping
    public ResponseEntity<?> getAllDriverRequests() {
        try {
            List<DriverRequest> list = driverRequestService.getAllDriverRequests();
            if (list == null || list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204
            }
            return ResponseEntity.status(HttpStatus.OK).body(list); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400
        }
    }

    // PUT /api/driverRequest/{driverRequestId}  (Admin, Customer) -> 200 | 404
    @PutMapping("/{driverRequestId}")
    public ResponseEntity<?> updateDriverRequest(@PathVariable Long driverRequestId,
                                                 @RequestBody DriverRequest driverRequest) {
        DriverRequest updated =
                driverRequestService.updateDriverRequest(driverRequestId, driverRequest);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
        return ResponseEntity.status(HttpStatus.OK).body(updated); // 200
    }

    // GET /api/driverRequest/driver/{driverId}  (Admin) -> 200 | 404
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<?> getDriverRequestsByDriverId(@PathVariable Long driverId) {
        try {
            List<DriverRequest> list = driverRequestService.findDriverRequestsByDriverId(driverId);
            return ResponseEntity.status(HttpStatus.OK).body(list); // 200
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    // DELETE /api/driverRequest/{driverRequestId}  (Customer) -> 200 | 404
    @DeleteMapping("/{driverRequestId}")
    public ResponseEntity<?> deleteDriverRequest(@PathVariable Long driverRequestId) {
        DriverRequest deleted = driverRequestService.deleteDriverRequest(driverRequestId);
        if (deleted == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
        return ResponseEntity.status(HttpStatus.OK).body(deleted); // 200
    }
}