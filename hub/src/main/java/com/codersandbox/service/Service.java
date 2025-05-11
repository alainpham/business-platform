package com.codersandbox.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.codersandbox.model.Person;
import com.codersandbox.helper.View;

@RestController
public class Service {

    private final static Logger logger = LoggerFactory.getLogger(Service.class);

    private static final String AVAILABILITY_CALCULATOR_URL = System.getenv().getOrDefault("AVAILABILITY_CALCULATOR_URL", "http://availability-calculator:8080");
    private static final String NOTIFICATION_DISPATCHER_URL = System.getenv().getOrDefault("NOTIFICATION_DISPATCHER_URL", "http://notification-dispatcher:8080");

    private RestTemplate restTemplate = new RestTemplate();


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
    
    /* example of a booking input output
    {
        "id": 0,
        "customerName": "alain",
        "bookingStartDate": "2023-12-10",
        "bookingEndDate": "2023-12-15",
        "hotel": "accor",
        "room": "3",
        "status": "requested"
      }
    */

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> makeReservation(@RequestBody Map<String, Object> booking) {
        
        //generate booking id
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        String dt = String.format("%02d%02d%02d", now.getYear() % 100, now.getMonthValue(),now.getDayOfMonth());
        String bookingId = dt+java.util.UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10).toUpperCase();
        booking.put("id", bookingId);
        
        logger.info("Received booking details: {}", booking);

        Map<String, Object> availResponse = restTemplate.postForObject(AVAILABILITY_CALCULATOR_URL + "/check-availability",booking, Map.class);
        logger.info("Received availability response: {}", availResponse);
        
        if (availResponse.get("status").equals("available")) {
            logger.info("Booking is available, calling notifications dispatch ...");
            Map<String, Object> dispatchResponse = restTemplate.postForObject(NOTIFICATION_DISPATCHER_URL + "/dispatch-notification",booking, Map.class);
            logger.info("dispatched notification: {}", dispatchResponse);
        }

        return ResponseEntity.status(201).body(availResponse);
    }

}
