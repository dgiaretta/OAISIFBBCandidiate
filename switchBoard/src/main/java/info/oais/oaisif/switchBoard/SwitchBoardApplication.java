
package info.oais.oaisif.switchBoard;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Configuration
@PropertySource("classpath:switchBoard.properties")
public class SwitchBoardApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SwitchBoardApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8085"));
        app.run(args);
    }
}
