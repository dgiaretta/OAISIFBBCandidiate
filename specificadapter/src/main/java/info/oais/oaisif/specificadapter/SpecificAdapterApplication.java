package info.oais.oaisif.specificadapter;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpecificAdapterApplication {

	public static void main(String[] args) {
		// SpringApplication.run(SwitchBoardApplication.class, args);
		SpringApplication app = new SpringApplication(SpecificAdapterApplication.class);
        app.setDefaultProperties(Collections
          .singletonMap("server.port", "8510"));
        app.run(args);
	}

}
