package demo;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

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

import demo.model.EquipementInfo;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MultiGauge;

@RestController
public class Service {

    private final static Logger logger = LoggerFactory.getLogger(Service.class);

    private Map<Integer, EquipementInfo> equipementInfo = new TreeMap<Integer, EquipementInfo>();

    @Autowired
    private ViewUpdate viewUpdate;

    private AtomicLong worstFreshnessGauge;
    private AtomicLong bestFreshnessGauge;
    
    private MeterRegistry meterRegistry;

    public Service(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/ping")
    public ResponseEntity<Serializable> ping(RequestEntity<Serializable> requestEntity) {
        logger.info(requestEntity.toString());
        return ResponseEntity.status(200).body(new LinkedHashMap<String, Serializable>(Map.of("msg", "HELLO")));
    }

    @PostMapping("/send-msg")
    public ResponseEntity<Serializable> sendMsg(RequestEntity<Serializable> requestEntity)
            throws JsonProcessingException {
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
        long now = System.currentTimeMillis();
        equipementInfo.values().forEach(info -> info.setFreshnessSeconds((now - info.getLastUpdate().getTime()) / 1000));
        return equipementInfo.values().stream().toList();
    }

    @Scheduled(fixedRate = 3000)
    public void updateFreshness() {
        logger.debug("Updating freshness...");
        long now = System.currentTimeMillis();
        long worst = 0;
        long best = Long.MAX_VALUE;

        if (equipementInfo.isEmpty()) {
            return;
        }

        for (EquipementInfo info : equipementInfo.values()) {
            long freshness = now - info.getLastUpdate().getTime();
            if (freshness > worst) {
                worst = freshness;
            }
            if (freshness < best) {
                best = freshness;
            }
        }

        if (worstFreshnessGauge == null || bestFreshnessGauge == null) {
            worstFreshnessGauge = meterRegistry.gauge("freshness_worst_seconds", new AtomicLong(worst / 1000));
            bestFreshnessGauge = meterRegistry.gauge("freshness_best_seconds", new AtomicLong(best / 1000));
        } else {
            worstFreshnessGauge.set(worst / 1000);
            bestFreshnessGauge.set(best / 1000);
        }
    }
}
