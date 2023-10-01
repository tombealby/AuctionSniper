package bealby.tom.AuctionSniper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@RestController
public class SniperController {
	
	private String status = "NotStartedBidding";
	private RestTemplate restTemplate = new RestTemplate();
	private int myLatestBid;

	@RequestMapping("/startBidding")
	public ResponseEntity<String> startBidding() {

		System.out.println("Starting to bid");
		final ResponseEntity<String> responseFromAuction = requestToJoinTheAuction();
		if (responseFromAuction.getStatusCode().equals(HttpStatus.OK)) {
			status = "biddingStarted";
			final String responseMessage = "Received an ok response from my request to join the auction, "
					+ "so take that to mean that I have joined the auction.";
			System.out.println(responseMessage);
			return ResponseEntity.ok(responseMessage);
		} else {
			return new ResponseEntity<String>(responseFromAuction.getBody(), responseFromAuction.getStatusCode());
		}
	}
	
	@RequestMapping("/receiveAuctionMessage")
	public ResponseEntity<String> receiveAuctionMessage() {
		System.out.println("Received a message from the auction. I take that to mean that the auction has"
				+ " closed and that I have lost.");
		status = "Lost";
		return ResponseEntity.ok("Received a message from the auction");
	}
	
	@RequestMapping("/getStatus")
	public ResponseEntity<String> getStatus() {
		System.out.println("Received a request to show my status. My current status is:" + status);
		return ResponseEntity.ok("current status:" + status);
	}

	private ResponseEntity<String> requestToJoinTheAuction() throws HttpClientErrorException {
	    final String url = "http://localhost:8093/receiveJoinRequest";
		return restTemplate.getForEntity(url, String.class);
	}

	@RequestMapping("/priceNotification")
	public ResponseEntity<String> receivePriceNotification(@RequestParam("currentPrice") Integer currentPrice,
			@RequestParam("priceIncrement") Integer priceIncrement, @RequestParam("winningBidder") String winningBidder) {
		System.out.println("Received a price notification from the auction. Current price:" + currentPrice
				+ ", price increment:" + priceIncrement + " with winning bidder \"" + winningBidder + "\"");
		final int bidThatINeedToMake = currentPrice + priceIncrement;
		makeBid(bidThatINeedToMake);
		return ResponseEntity.ok("");
	}

	private void makeBid(int bidThatINeedToMake) {
		final String url = "http://localhost:8093/receiveBid?bid=" + bidThatINeedToMake;
		restTemplate.getForEntity(url, String.class);
		this.myLatestBid =  bidThatINeedToMake;
	}

	@RequestMapping("/getLatestBid")
	public ResponseEntity<String> getLatestBid() {
		System.out.println("Received a request to show my latest bid. My latest bid is:" + myLatestBid);
		return ResponseEntity.ok("My latest bid:" + myLatestBid);
	}

}
