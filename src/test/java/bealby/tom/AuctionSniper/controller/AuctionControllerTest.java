package bealby.tom.AuctionSniper.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuctionControllerTest {

	public static final String GET_STATUS = "/getStatus";
	public static final String START_BIDDING = "/startBidding";

    @Autowired
    private MockMvc mockMvc;

	@Test
	public void joinAuctionShouldChangeSniperStatus() throws Exception {

		givenTheSniperHasNotStartedBidding();
        whenARequestIsMadeToJoinTheAuction();
		thenTheSniperStatusIsChangedToBiddingStarted();
	}

	private void thenTheSniperStatusIsChangedToBiddingStarted() throws UnsupportedEncodingException, Exception {
		// THEN sniper status is changed to
        String finalStatus = mockMvc.perform(get(GET_STATUS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("intialStatus:" + finalStatus);

        assertEquals("current status:biddingStarted" , finalStatus);
	}

	private void whenARequestIsMadeToJoinTheAuction() throws UnsupportedEncodingException, Exception {
		//WHEN the sniper makes a request to join auction
        String content = mockMvc.perform(get(START_BIDDING)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("Content:" + content);

        assertEquals("Received a request to join the auction" , content);
	}

	private void givenTheSniperHasNotStartedBidding() throws UnsupportedEncodingException, Exception {
        String intialStatus = mockMvc.perform(get(GET_STATUS)
                .contentType(MediaType.APPLICATION_JSON))
//                .param(REQ_PARAM_REQUESTED_BY, "TEST_DATA_EXTRACT"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("intialStatus:" + intialStatus);

        assertEquals("current status:NotStartedBidding" , intialStatus);
	}

}
