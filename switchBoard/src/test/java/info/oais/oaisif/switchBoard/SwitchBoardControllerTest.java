
package info.oais.oaisif.switchBoard;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("unused")
@WebMvcTest(SwitchBoardController.class)
public class SwitchBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SwitchBoardRepository switchBoardRepository;

    @InjectMocks
    private SwitchBoardController switchBoardController;

    private SwitchBoardEntry entry1;
    private SwitchBoardEntry entry2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        entry1 = new SwitchBoardEntry();
        entry1.setId(1L);
        entry1.setArchiveName("name1");
        entry1.setArchiveDescription("description1");
        entry1.setArchiveURL("url1");

        entry2 = new SwitchBoardEntry();
        entry2.setId(2L);
        entry2.setArchiveName("name2");
        entry2.setArchiveDescription("description2");
        entry2.setArchiveURL("url2");
    }

    @Test
    public void testGetByArchiveNameLikeByRequestParam() throws Exception {
        when(switchBoardRepository.findByArchiveNameLike("name1")).thenReturn(Arrays.asList(entry1));

        mockMvc.perform(get("/oaisif/v1/switchboard/sources/name1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBySBAll() throws Exception {
        when(switchBoardRepository.findAll()).thenReturn(Arrays.asList(entry1, entry2));

        mockMvc.perform(get("/oaisif/v1/switchboard/sources"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetByArchiveNameLikeByRequestParam_NotFound() throws Exception {
        when(switchBoardRepository.findByArchiveNameLike("name999")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/oaisif/v1/switchboard/sources/name999"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetBySBAll_Empty() throws Exception {
        when(switchBoardRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/oaisif/v1/switchboard/sources"))
                .andExpect(status().isOk());
    }
}
