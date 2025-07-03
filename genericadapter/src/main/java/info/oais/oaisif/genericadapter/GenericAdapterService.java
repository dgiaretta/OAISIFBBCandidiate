
package info.oais.oaisif.genericadapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

/**
 * Service class for handling generic adapter operations.
 */
@Service
@PropertySource("classpath:genericadapter.properties")
public class GenericAdapterService {
    private static final Logger log = LoggerFactory.getLogger(GenericAdapterService.class);

    @Autowired
    private GenericAdapterRepository genericAdapterRepository;

    @Value("${SWITCHBOARD}")
    private String switchboard;

    @Value("${GENERICADAPTERPORT}")
    private String genericadapterport;

    @Value("${MYDESCRIPTION}")
    private String mydescription;

    @Value("${SPECIFICADAPTER}")
    private String specificadapter;

    @Value("${MYAUTHENTCATIONMETHOD}")
    private String myauthenticationmethod;

    @Value("${MYSERIALISATIONMETHOD}")
    private String myserialisationmethod;

    @Value("${MYCOMMUNICATIONMETHOD}")
    private String mycommunicationmethod;

    @Value("${MYQUERYMETHOD}")
    private String myquerymethod;

    /**
     * Initialize the service by saving configuration properties to the repository.
     */
    @PostConstruct
    public void postConstruct() {
        String[] myKeys = new String[]{"SWITCHBOARD", "GENERICADAPTERPORT", "MYDESCRIPTION", "SPECIFICADAPTER", "MYAUTHENTCATIONMETHOD", "MYSERIALISATIONMETHOD", "MYCOMMUNICATIONMETHOD", "MYQUERYMETHOD"};
        String[] propValues = new String[]{switchboard, genericadapterport, mydescription, specificadapter, myauthenticationmethod, myserialisationmethod, mycommunicationmethod, myquerymethod};

        for (int i = 0; i < myKeys.length; i++) {
            GenericAdapterEntry sbe = new GenericAdapterEntry();
            sbe.setId(System.currentTimeMillis());
            sbe.setPropertyName(myKeys[i]);
            sbe.setPropertyValue(propValues[i]);
            genericAdapterRepository.save(sbe);
        }
        log.info("Entries: {}", genericAdapterRepository.findAll());
    }
}
