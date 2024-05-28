package demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;


@SpringBootApplication
@EnableWebSocket
@EnableScheduling
public class Application implements WebSocketConfigurer{
    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Autowired
	WebSocketTextHandler webSocketTextHandler;

    @Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		// Expose endpoint and add Handler. Wildcard allowed origins to support COORS
		// registry.addHandler(new WebsocketTextHandler(), "/websocket").setAllowedOrigins("*");
		registry.addHandler(webSocketTextHandler, "/websocket");
	}


	@Bean
	@ConditionalOnClass(name = "io.opentelemetry.javaagent.OpenTelemetryAgent")
	public MeterRegistry otelRegistry() {
		Optional<MeterRegistry> otelRegistry = Metrics.globalRegistry.getRegistries().stream()
				.filter(r -> r.getClass().getName().contains("OpenTelemetryMeterRegistry"))
				.findAny();
		otelRegistry.ifPresent(Metrics.globalRegistry::remove);
		return otelRegistry.orElse(null);
	} 

}