package info.oais.oaisif.genericadapter;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class GenericAdapterServiceTest {

    @Mock
    private GenericAdapterRepository genericAdapterRepository;

    @InjectMocks
    private GenericAdapterService genericAdapterService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(genericAdapterService, "switchboard", "testSwitchboard");
        ReflectionTestUtils.setField(genericAdapterService, "genericadapterport", "testPort");
        ReflectionTestUtils.setField(genericAdapterService, "mydescription", "testDescription");
        ReflectionTestUtils.setField(genericAdapterService, "specificadapter", "testAdapter");
        ReflectionTestUtils.setField(genericAdapterService, "myauthenticationmethod", "testAuthMethod");
        ReflectionTestUtils.setField(genericAdapterService, "myserialisationmethod", "testSerialMethod");
        ReflectionTestUtils.setField(genericAdapterService, "mycommunicationmethod", "testCommMethod");
        ReflectionTestUtils.setField(genericAdapterService, "myquerymethod", "testQueryMethod");
    }

    @Test
    public void testPostConstruct() {
        genericAdapterService.postConstruct();

        verify(genericAdapterRepository, times(8)).save(any(GenericAdapterEntry.class));
    }
}