
package info.oais.oaisif.switchBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@PropertySource("classpath:switchBoard.properties")
public class SwitchBoardService {

    @Autowired
    private SwitchBoardRepository switchBoardRepository;

    @Value("${sb0}") 
    private String sb0;
    @Value("${sb1}") 
    private String sb1;
    @Value("${sb2}") 
    private String sb2;
    @Value("${sb3}") 
    private String sb3;

    @PostConstruct
    public void postConstruct() {
        String[] myArray = new String[]{sb0, sb1, sb2, sb3};

        for (String propValue : myArray) {
            SwitchBoardEntry sbe = new SwitchBoardEntry();
            String[] lines = propValue.split("!");

            sbe.setId(System.currentTimeMillis());
            sbe.setArchiveName(lines[0]);
            sbe.setArchiveDescription(lines[1]);
            sbe.setArchiveURL(lines[2]);

            switchBoardRepository.save(sbe);
        }
    }
}
