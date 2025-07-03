
package info.oais.oaisif.switchBoard;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

public class SwitchBoardServiceTest {

    @Mock
    private SwitchBoardRepository switchBoardRepository;

    @InjectMocks
    private SwitchBoardService switchBoardService;

    @Value("${sb0}") 
    private String sb0 = "name0!description0!url0";
    @Value("${sb1}") 
    private String sb1 = "name1!description1!url1";
    @Value("${sb2}") 
    private String sb2 = "name2!description2!url2";
    @Value("${sb3}") 
    private String sb3 = "name3!description3!url3";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testPostConstruct() {
//        switchBoardService.postConstruct();
//        verify(switchBoardRepository, times(4)).save(new SwitchBoardEntry());
//    }
}
