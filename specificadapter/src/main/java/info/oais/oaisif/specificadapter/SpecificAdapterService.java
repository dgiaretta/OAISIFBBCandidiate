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
	 * Get the JSON for 10 (TEN) AIPs
	 */
	@Value("${aip0}") 
	private String aip0;
	@Value("${aip1}") 
	private String aip1;
	@Value("${aip2}") 
	private String aip2;
	@Value("${aip3}") 
	private String aip3;
	@Value("${aip4}") 
	private String aip4;
	@Value("${aip5}") 
	private String aip5;
	@Value("${aip6}") 
	private String aip6;
	@Value("${aip7}") 
	private String aip7;
	@Value("${aip8}") 
	private String aip8;
	@Value("${aip9}") 
	private String aip9;

	@PostConstruct
	public void postConstruct() {
	
        log.info("Saving the initial values");
        
    	System.out.println("aip0:"+ aip0);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip0));
        System.out.println("aip1:"+ aip1);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip1));
        System.out.println("aip2:"+ aip2);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip2));
    	System.out.println("aip3:"+ aip3);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip3));
        System.out.println("aip4:"+ aip4);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip4));
        System.out.println("aip5:"+ aip5);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip5));
    	System.out.println("aip6:"+ aip6);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip6));
        System.out.println("aip7:"+ aip7);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip7));
        System.out.println("aip8:"+ aip8);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip8));
        System.out.println("aip9:"+ aip9);
        specificAdapterRepository.save(new SpecificAdapterEntry(aip9));

        //System.out.println("specificAdapterRepository is:" + specificAdapterRepository);
        //System.out.println("Retrieve all records");
        //System.out.println("Entries: " + specificAdapterRepository.findAll());
	}

	
}
