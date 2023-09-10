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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@AutoConfigureMockMvc
public class AuctionControllerTest {

	public static final String GET_STATUS = "/getStatus";
	public static final String START_BIDDING = "/startBidding";

    @Autowired
    private MockMvc mockMvc;

    private RestTemplate restTemplate = new RestTemplate();

	@Test
	public void joinAuctionShouldChangeSniperStatus() throws Exception {

		givenTheSniperHasNotStartedBidding();
        whenARequestIsMadeToJoinTheAuction();
		thenTheSniperStatusIsChangedToBiddingStarted();
	}

	private void thenTheSniperStatusIsChangedToBiddingStarted() throws UnsupportedEncodingException, Exception {
        String finalStatus = mockMvc.perform(get(GET_STATUS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("intialStatus:" + finalStatus);

        assertEquals("current status:biddingStarted" , finalStatus);
	}

	private void whenARequestIsMadeToJoinTheAuction() throws UnsupportedEncodingException, Exception {
        String content = mockMvc.perform(get(START_BIDDING)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("Content:" + content);

        assertEquals("Received a message back from the auction, so take that to mean that I "
        		+ "have joined the auction." , content);
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

//	@Test
//	public void sniperJoinsAuctionUntilAuctionClose() throws UnsupportedEncodingException, Exception {
//		givenAuctionIsSellingItem();
//		whenARequestIsMadeToJoinTheAuction();
//		thenAuctionHasReceivedRequestFromSniper();
//		whenAuctionCloses(); // fails here - Auction can't connect back to the Sniper
//		thenSniperHasLostAuction();
//	}

	private void thenSniperHasLostAuction() throws UnsupportedEncodingException, Exception {
		sniperKnowsAuctionIsClosed();
		// TODO implement the following
//		sniperKnowsItDidNotWin();
	}

	private void sniperKnowsAuctionIsClosed() throws UnsupportedEncodingException, Exception {
        String finalStatus = mockMvc.perform(get(GET_STATUS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("intialStatus:" + finalStatus);

        assertEquals("current status:biddingStarted" , finalStatus);
	}

	private void whenAuctionCloses() {
		closeAuction();
	}

	private void thenAuctionHasReceivedRequestFromSniper() {
		ResponseEntity<String> response = getAuctionReceiveStatus();
		String actualResponse = response.getBody();
		String expectedResponse = "ReceiveStatus:true";
		assertEquals(expectedResponse, actualResponse);
	}

	private void givenAuctionIsSellingItem() {
		ResponseEntity<String> response = getAuctionStatus();
		String actualResponse = response.getBody();
		String expectedResponse = "current status:Open";
		assertEquals(expectedResponse, actualResponse);
	}

	private ResponseEntity<String> getAuctionStatus() throws HttpClientErrorException {
	    final String url = "http://localhost:8093/getStatus";
		return restTemplate.getForEntity(url, String.class);
	}

	private ResponseEntity<String> getAuctionReceiveStatus() throws HttpClientErrorException {
	    final String url = "http://localhost:8093/getReceiveStatus";
		return restTemplate.getForEntity(url, String.class);
	}

	private ResponseEntity<String> closeAuction() throws HttpClientErrorException {
	    final String url = "http://localhost:8093/closeAuction";
		return restTemplate.getForEntity(url, String.class);

		// Auction throws error when this method is called ..

//		notify clients that auction is closed
//		2023-09-09 18:06:08.565 ERROR 27549 --- [nio-8093-exec-5] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8092/receiveAuctionMessage": Connection refused (Connection refused); nested exception is java.net.ConnectException: Connection refused (Connection refused)] with root cause
//
//		java.net.ConnectException: Connection refused (Connection refused)
	}

}
