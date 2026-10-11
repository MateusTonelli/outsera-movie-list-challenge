package com.outsera.movielist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "app.csv-location=classpath:tied-movielist.csv",
        "app.datasource-name=tied-movielist"
})
class AwardIntervalTiedDatasetTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsEveryTiedGapAndUsesConsecutiveWins() throws Exception {
        mockMvc.perform(get("/producers/award-intervals").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.min", hasSize(3)))
                .andExpect(jsonPath("$.min[0].producer").value("Fast B"))
                .andExpect(jsonPath("$.min[0].interval").value(1))
                .andExpect(jsonPath("$.min[0].previousWin").value(2005))
                .andExpect(jsonPath("$.min[0].followingWin").value(2006))
                .andExpect(jsonPath("$.min[1].producer").value("Solo"))
                .andExpect(jsonPath("$.min[1].interval").value(1))
                .andExpect(jsonPath("$.min[1].previousWin").value(1999))
                .andExpect(jsonPath("$.min[1].followingWin").value(2000))
                .andExpect(jsonPath("$.min[2].producer").value("Triple"))
                .andExpect(jsonPath("$.min[2].interval").value(1))
                .andExpect(jsonPath("$.min[2].previousWin").value(1980))
                .andExpect(jsonPath("$.min[2].followingWin").value(1981))
                .andExpect(jsonPath("$.max", hasSize(2)))
                .andExpect(jsonPath("$.max[0].producer").value("Slow A"))
                .andExpect(jsonPath("$.max[0].interval").value(14))
                .andExpect(jsonPath("$.max[0].previousWin").value(1980))
                .andExpect(jsonPath("$.max[0].followingWin").value(1994))
                .andExpect(jsonPath("$.max[1].producer").value("Triple"))
                .andExpect(jsonPath("$.max[1].interval").value(14))
                .andExpect(jsonPath("$.max[1].previousWin").value(1981))
                .andExpect(jsonPath("$.max[1].followingWin").value(1995));
    }
}
