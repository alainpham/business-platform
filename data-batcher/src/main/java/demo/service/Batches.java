package demo.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import demo.model.EquipementInfo;
import jakarta.annotation.PostConstruct;

@Component
@RestController
public class Batches {
    private static final Logger logger = LoggerFactory.getLogger(Batches.class);

	private Map<Integer,EquipementInfo> equipementInfoMap = new TreeMap<Integer,EquipementInfo>();

    private RestTemplate restTemplate = new RestTemplate();

	@Value("${public.api.url}")
	private String publicApiUpdateUrl;

	@Autowired
	private ResourceLoader resourceLoader;

	private ObjectMapper mapper = new ObjectMapper();
	
	private Random r = new Random();
    
	private List<String> anomalyMessages = new ArrayList<>();

	private boolean forceFail = false;

	@PostConstruct
	public void init() throws IOException {
		String data = resourceLoader.getResource("classpath:data/data.json").getContentAsString(StandardCharsets.UTF_8);
		List<EquipementInfo> equipementInfoList = mapper.readValue(data, new TypeReference<List<EquipementInfo>>(){});
		for (EquipementInfo equipementInfo : equipementInfoList) {
			equipementInfoMap.put(equipementInfo.getId(), equipementInfo);
		} 
		logger.debug(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(equipementInfoMap));

		anomalyMessages.add("Error Code: E-501Z - An unexpected system reboot occurred. Please check power supply and hardware connections.");
        anomalyMessages.add("Alert: Unauthorized Access Attempt Detected - Multiple failed login attempts from IP address 192.168.1.10. Account has been temporarily locked.");
        anomalyMessages.add("System Alert: Data Integrity Compromised - Data discrepancy detected in Sector 7G. Immediate verification and backup required.");
        anomalyMessages.add("Warning: Temperature Threshold Exceeded - Server room temperature has risen above safe levels. Cooling system malfunction suspected.");
        anomalyMessages.add("Critical Error: Network Latency Spike - Latency exceeds 500ms in the primary network. Potential bottleneck or hardware failure identified.");

	}

	@GetMapping("/forcefail")
	public void forceFail() {
		forceFail = true;
	}


	@GetMapping("/forcenormal")
	public void forceNormal() { 
		forceFail = false;
	}


    @Scheduled(fixedRate = 60000*2)
	public void batchSchedule() throws IOException {
		logger.info("The time is now {}", new Date());
		// for (Map.Entry<Integer, EquipementInfo> entry : equipementInfoMap.entrySet()) {
		// 	logger.info("EquipementInfo: {}", entry.getValue().getName());
		// 	entry.getValue().setLastUpdate(new Date());
		// 	microBatch(entry.getValue());
		// }

		equipementInfoMap.entrySet().parallelStream().forEach(entry -> {
			entry.getValue().setLastUpdate(new Date());
			microBatch(entry.getValue());
		});
	}


	public void microBatch(EquipementInfo equipementInfo){

		/* determine failure range every forth equipment should fail*/
		Calendar currentCalendar = GregorianCalendar.getInstance();
		int currenHourOfDay = currentCalendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
		boolean shouldFail = currenHourOfDay >= 2 && currenHourOfDay <= 3 && equipementInfo.getId() % 4 == 0;
		logger.debug("current hour of day: {}, should fail: {}", currenHourOfDay,shouldFail);

		long start = System.currentTimeMillis();
		logger.info("batchprocess=\"{}\" state={} id={}", equipementInfo.getName(), "started",start);
		long duration = r.nextInt(100-50) + 50;
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		if (shouldFail || forceFail) {
			logger.error("batchprocess=\"{}\" error=\"{}\" id={}", equipementInfo.getName(), anomalyMessages.get(r.nextInt(anomalyMessages.size())),start);
			logger.error("batchprocess=\"{}\" error=\"{}\" id={}", equipementInfo.getName(), anomalyMessages.get(r.nextInt(anomalyMessages.size())),start);

			logger.error("batchprocess=\"{}\" state={} duration={} id={}", equipementInfo.getName(), "failed", end-start,start);
		}else {
			restTemplate.postForObject(publicApiUpdateUrl + "/update", equipementInfo, EquipementInfo.class);
			logger.info("batchprocess=\"{}\" state={} duration={} id={}", equipementInfo.getName(), "completed", end-start,start);
		}
	}
}
