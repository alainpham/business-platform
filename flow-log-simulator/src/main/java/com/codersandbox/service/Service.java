package com.codersandbox.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.codersandbox.model.Person;
import com.codersandbox.bpm.VendorCancellation;
import com.codersandbox.helper.TSGenerator;
import com.codersandbox.helper.View;

@RestController
public class Service {

    private final static Logger logger = LoggerFactory.getLogger(Service.class);

    @Autowired
    private View view;

    @GetMapping("/ping")
    public ResponseEntity<Serializable> ping(RequestEntity<Serializable> requestEntity) {
        logger.info(requestEntity.toString());
        return ResponseEntity.status(200).body(new LinkedHashMap<String, Serializable>(Map.of("msg", "HELLO")));
    }

    @PostMapping("/send-msg")
    public Person sendMsg(@RequestBody Person person) throws JsonProcessingException {
        logger.info(person.toString());
        List<Object> listObjects = List.of(person);
        view.update("person", listObjects);
        return person;
    }



    // running at a fixed delay of 1 second between each cycle
    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void runsim() throws Exception {

        VendorCancellation vendorCancellation = new VendorCancellation();

        


        // generate timestamps for a period of 3 hours
        
        TSGenerator tsGenerator = new TSGenerator();
        tsGenerator.generateTimestamps();
        int i = 0;
        for (LocalDateTime timestamp : tsGenerator.getTimestampsList()) {
            try {
                if (LocalDateTime.now().isBefore(timestamp)) {
                    long sleepTime = java.time.Duration.between(LocalDateTime.now(), timestamp).toMillis();
                    if (sleepTime > 0) {

                        Thread.sleep(sleepTime);

                    }
                    String instanceId = java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 10);
                    vendorCancellation.execute(instanceId, new HashMap<String, String>());
                }
            } catch (InterruptedException e) {
                logger.error("interrupted during sleep stopping..");
                break;
            }

            // do the thing here
            i++;
        }

    }
}
