package demo;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import demo.model.EquipementInfo;

@RestController
public class Service {

    private final static Logger logger = LoggerFactory.getLogger(Service.class);

    private Map<Integer,EquipementInfo> equipementInfo = new TreeMap<Integer,EquipementInfo>();

    @Autowired
    private ViewUpdate viewUpdate;
    
    @GetMapping("/ping")
    public ResponseEntity<Serializable> ping(RequestEntity<Serializable> requestEntity) {
        logger.info(requestEntity.toString());
        return ResponseEntity.status(200).body(new LinkedHashMap<String,Serializable>(Map.of("msg", "HELLO")));
    }

    @PostMapping("/send-msg")
    public ResponseEntity<Serializable> sendMsg(RequestEntity<Serializable> requestEntity) throws JsonProcessingException {
        logger.info(requestEntity.getBody().toString());
        viewUpdate.upsertData(requestEntity.getBody(), "person", "state-table");
        viewUpdate.appendData(requestEntity.getBody(), "person", "log-table");
        return ResponseEntity.status(200).body(requestEntity.getBody());
    }

    @PostMapping("/update")
    public EquipementInfo update(@RequestBody EquipementInfo equipmentInfo) throws JsonProcessingException {
        equipementInfo.put(equipmentInfo.getId(), equipmentInfo);
        logger.info("updated : " + equipmentInfo.getId() + " " + equipmentInfo.getLastUpdate());
        viewUpdate.upsertData(equipmentInfo, "locationInfo", "state-table");
        return equipmentInfo;
    }

    @GetMapping("/getAll")
    public List<EquipementInfo> getAll() {
        return equipementInfo.values().stream().toList();
    }

}
