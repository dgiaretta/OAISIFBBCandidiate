package info.oais.oaisif.genericadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//import info.oais.oaisif.switchBoard.SwitchBoardEntry;



//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.web.bind.annotation.Controller;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SpringBootApplication
@Component
// @RestController
@Configuration
@PropertySource("classpath:genericadapter.properties")
@JsonIgnoreProperties(ignoreUnknown = true)

/**
 * The Generic Adapter application for Spring Boot
 */
public class GenericadapterApplication {
	
	public static void main(String[] args) {
/**
 * Server port is set in application.properties  server.port=8765
 */
		SpringApplication app = new SpringApplication(GenericadapterApplication.class);
        app.run(args);
        
	}


	

	


    
    
}
