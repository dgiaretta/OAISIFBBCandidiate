
package info.oais.oaisif.rrori;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RroriApplication {
    public static void main(String[] args) {
        // Create a SpringApplication instance and set the default server port to 8083
        SpringApplication app = new SpringApplication(RroriApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
        app.run(args); // Run the application
    }
}
