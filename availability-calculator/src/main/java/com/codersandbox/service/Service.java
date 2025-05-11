package com.codersandbox.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.codersandbox.model.Person;
import com.codersandbox.helper.View;

@RestController
public class Service {

    private final static Logger logger = LoggerFactory.getLogger(Service.class);
    private Random generator = new Random();

    private static final int ROOM_AVAILABLE_RATE = Integer.parseInt(System.getenv().getOrDefault("ROOM_AVAILABLE_RATE", "80"));
    private static final int DBCONNECTION_FAILURE_RATE = Integer.parseInt(System.getenv().getOrDefault("DBCONNECTION_FAILURE_RATE", "5"));
    private static final int STORAGELIMIT_FAILURE_RATE = Integer.parseInt(System.getenv().getOrDefault("STORAGELIMIT_FAILURE_RATE", "2"));
    private static final int OK_RESPONSE_TIME_MS = Integer.parseInt(System.getenv().getOrDefault("OK_RESPONSE_TIME_MS", "20"));
    private static final int OK_RESPONSE_TIME_STDEV_MS = Integer.parseInt(System.getenv().getOrDefault("OK_RESPONSE_TIME_STDEV_MS", "8"));

    private static final int ERROR_RESPONSE_TIME_MS = Integer.parseInt(System.getenv().getOrDefault("ERROR_RESPONSE_TIME_MS", "100"));
    private static final int ERROR_RESPONSE_TIME_STDEV_MS = Integer.parseInt(System.getenv().getOrDefault("ERROR_RESPONSE_TIME_STDEV_MS", "25"));

    @Autowired
    private View view;
    
    @GetMapping("/ping")
    public ResponseEntity<Serializable> ping(RequestEntity<Serializable> requestEntity) {
        logger.info(requestEntity.toString());
        return ResponseEntity.status(200).body(new LinkedHashMap<String,Serializable>(Map.of("msg", "HELLO")));
    }

    @PostMapping("/send-msg")
    public Person sendMsg(@RequestBody Person person) throws JsonProcessingException {
        logger.info(person.toString());
        List<Object> listObjects = List.of(person);
        view.update("person", listObjects);
        return person;
    }

    @PostMapping("/check-availability")
    public ResponseEntity<Map<String, Object>> makeReservation(@RequestBody Map<String, Object> booking) throws InterruptedException {

        logger.info("Received availability request : {}", booking);
        
        long start = System.currentTimeMillis();
        
        //do the thing
        long randomExecTimeMs = Double.valueOf(Math.round(generator.nextGaussian()*OK_RESPONSE_TIME_STDEV_MS+OK_RESPONSE_TIME_MS)).longValue() ;
        long randomErrorExecTimeMs = Double.valueOf(Math.round(generator.nextGaussian()*ERROR_RESPONSE_TIME_STDEV_MS+ERROR_RESPONSE_TIME_MS)).longValue() ;
        
        if (randomExecTimeMs <= 0) {
            randomExecTimeMs = 1;
        }
        
        if (randomErrorExecTimeMs <= 0) {
            randomErrorExecTimeMs = 1;
        }

        Thread.sleep(randomExecTimeMs);
        // end to the thing
        long end;

        boolean roomAvailable = (generator.nextInt(100) + 1) < ROOM_AVAILABLE_RATE;
        boolean dbConnectionFailed = (generator.nextInt(100) + 1) < DBCONNECTION_FAILURE_RATE;
        boolean storageLimitFailed = (generator.nextInt(100) + 1) < STORAGELIMIT_FAILURE_RATE;

        if (dbConnectionFailed) {
            Thread.sleep(randomErrorExecTimeMs);
            logger.error("DB connection failed for booking id: {}", booking.get("id"));
            end = System.currentTimeMillis();
            logger.info("Booking availability calculations completed for {} in {}", booking.get("id") , end - start);
            return ResponseEntity.status(500).body(Map.of("error", "DB connection failed"));
        }

        if (storageLimitFailed) {
            Thread.sleep(randomErrorExecTimeMs);
            logger.error("Storage limit exceeded for booking id: {}", booking.get("id"));
            end = System.currentTimeMillis();
            logger.info("Booking availability calculations completed for {} in {}", booking.get("id") , end - start);
            return ResponseEntity.status(500).body(Map.of("error", "Storage limit exceeded"));
        }

        end = System.currentTimeMillis();
        logger.info("Booking availability calculations completed for {} in {}", booking.get("id") , end - start);

        if (roomAvailable) {
            // room is available
            logger.info("Room is available for booking id: {}", booking.get("id"));
            booking.put("status", "available");

            return ResponseEntity.status(200).body(booking);
        } else {
            // room is not available
            logger.info("Room is busy for booking id: {}", booking.get("id"));
            booking.put("status", "notavailable");
            return ResponseEntity.status(200).body(booking);
        }

    }
}
