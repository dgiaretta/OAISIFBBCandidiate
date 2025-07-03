package info.oais.oaisif.specificadapter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@PropertySource("classpath:specificadapter.properties")
public class SpecificAdapterService {
	private static final Logger log = LoggerFactory.getLogger(SpecificAdapterService.class);
	
	@Autowired
	SpecificAdapterRepository specificAdapterRepository;
	
	@SuppressWarnings("null")
	/**
	 * Get the JSON for 3 (THREE) AIPs
	 */
	@Value("${aip0}") 
	private String aip0;
	@Value("${aip1}") 
	private String aip1;
	@Value("${aip2}") 
	private String aip2;
	@PostConstruct
	public void postConstruct() {
	
        log.info("Saving the initial values");
        
    	System.out.println("aip0:"+ aip0);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip0));
        System.out.println("aip1:"+ aip1);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip1));
        System.out.println("aip2:"+ aip2);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip2));

        System.out.println("specificAdapterRepository is:" + specificAdapterRepository);
        System.out.println("Retrieve all records");
        System.out.println("Entries: " + specificAdapterRepository.findAll());
	}

	
}
