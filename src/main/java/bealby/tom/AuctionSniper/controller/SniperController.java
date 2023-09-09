package bealby.tom.AuctionSniper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@RestController
public class SniperController {
	
	private String status = "NotStartedBidding";
	private RestTemplate restTemplate = new RestTemplate();

	@RequestMapping("/startBidding")
	public ResponseEntity<String> startBidding() {
		System.out.println("Starting to bid");
		status = "biddingStarted";
		// tell Action that I want to join it
		return sendRequest();
	}
	
	@RequestMapping("/receiveAuctionMessage")
	public ResponseEntity<String> receiveAuctionMessage() {
		System.out.println("Received a message from the auction");
		status = "auctionClosed";
		return ResponseEntity.ok("Received a message from the auction");
	}
	
	@RequestMapping("/getStatus")
	public ResponseEntity<String> getStatus() {
		System.out.println("Received a request to show my status");
		return ResponseEntity.ok("current status:" + status);
	}

	private ResponseEntity<String> sendRequest() throws HttpClientErrorException {
	    final String url = "http://localhost:8093/receiveJoinRequest";
		return restTemplate.getForEntity(url, String.class);
	}

}
