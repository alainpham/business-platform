package com.codersandbox.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String SMS_URL = System.getenv().getOrDefault("SMS_URL", "http://sms:8080");
    private static final String EMAIL_URL = System.getenv().getOrDefault("EMAIL_URL", "http://email:8080");

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

    @PostMapping("/dispatch-notification")
    public ResponseEntity<Map<String, Object>> dispatchNotification(@RequestBody Map<String, Object> booking) {
        logger.info("Received notification dispatch request : {}", booking);

        // Send SMS notification
        Map<String, Object> smsResponse = restTemplate.postForObject(SMS_URL + "/send-sms",booking, Map.class);
        logger.info("Received sms response: {}", smsResponse);
        
        // Send Email notification
        Map<String, Object> emailResponse = restTemplate.postForObject(EMAIL_URL + "/send-email", booking, Map.class);
        logger.info("Received email response: {}", emailResponse);

        booking.put("status", "notificationdispatched");
        return ResponseEntity.ok(booking);
    }


}
