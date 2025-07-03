
package info.oais.oaisif.rrori;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
@PropertySource("classpath:rrori.properties")
public class RroriService {
    private static final Logger log = LoggerFactory.getLogger(RroriService.class);

    @Autowired
    private RroriRepository rroriRepository;

    // Values from properties file
    @Value("${aip0}")
    private String aip0;
    @Value("${aip1}")
    private String aip1;
    @Value("${aip2}")
    private String aip2;

    // Method to initialize the repository with initial values
    @PostConstruct
    public void postConstruct() {
        log.info("Saving the initial values");
        rroriRepository.save(new RroriEntry(aip0));
        rroriRepository.save(new RroriEntry(aip1));
        rroriRepository.save(new RroriEntry(aip2));
    }
}
