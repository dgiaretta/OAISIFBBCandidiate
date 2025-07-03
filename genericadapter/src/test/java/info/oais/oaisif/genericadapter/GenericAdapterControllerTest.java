
package info.oais.oaisif.genericadapter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(GenericAdapterController.class)
public class GenericAdapterControllerTest {

    @MockBean
    private GenericAdapterRepository genericAdapterRepository;

    @InjectMocks
    private GenericAdapterController genericAdapterController;

    private MockMvc mockMvc;

    @Value("${SPECIFICADAPTER}")
    private String specificAdapterUrl;

    @Value("${SWITCHBOARD}")
    private String switchboardUrl;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(genericAdapterController).build();
    }

    @Test
    public void testGetJsonAllProperties() throws Exception {
        GenericAdapterEntry entry1 = new GenericAdapterEntry();
        entry1.setPropertyName("MYDESCRIPTION");
        entry1.setPropertyValue("Description");

        GenericAdapterEntry entry2 = new GenericAdapterEntry();
        entry2.setPropertyName("MYAUTHENTCATIONMETHOD");
        entry2.setPropertyValue("AuthMethod");

        when(genericAdapterRepository.findByPropertyName("MYDESCRIPTION")).thenReturn(Arrays.asList(entry1));
        when(genericAdapterRepository.findByPropertyName("MYAUTHENTCATIONMETHOD")).thenReturn(Arrays.asList(entry2));

        mockMvc.perform(get("/oaisif/v1/properties")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.InformationPackage.version").value("1.0.0"))
                .andExpect(jsonPath("$.InformationPackage.InformationObject.DataObject.EncodedObject.EncodedContent").exists());
    }
}