package bealby.tom.AuctionSniper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

// TODO does this class need refactoring to move business logic into a service?
// at the moment it handles both endpoint/interface logic and business logic.
@RestController
public class SniperController {
	
	private static final int BID_VALUE_WHEN_NOT_STARTED_BIDDING = -1;
	private static final String STATUS_WHEN_NOT_STARTED_BIDDING = "NotStartedBidding";

	private String status = STATUS_WHEN_NOT_STARTED_BIDDING;
	private int myLatestBid = BID_VALUE_WHEN_NOT_STARTED_BIDDING;

	private RestTemplate restTemplate = new RestTemplate();

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
		makeBidInNewThread(currentPrice, priceIncrement);
		return ResponseEntity.ok("Thanks, I got your price notification of current price " + currentPrice +
				" and price increment " + priceIncrement);
	}

	private void makeBidInNewThread(final Integer currentPrice, final Integer priceIncrement) {

		final Runnable task = new Runnable() {
			@Override
			public void run() {
				makeBid(currentPrice, priceIncrement);
			}
		};
		new Thread(task).start();
	}

	private void makeBid(final Integer currentPrice, final Integer priceIncrement) {
		final int bidThatINeedToMake = currentPrice + priceIncrement;
		final String url = "http://localhost:8093/receiveBid?bid=" + bidThatINeedToMake + "&bidderId=sniper";
		restTemplate.getForEntity(url, String.class);
		this.myLatestBid =  bidThatINeedToMake;
	}

	@RequestMapping("/getLatestBid")
	public ResponseEntity<String> getLatestBid() {
		System.out.println("Received a request to show my latest bid. My latest bid is:" + myLatestBid);
		return ResponseEntity.ok("My latest bid:" + myLatestBid);
	}

	@RequestMapping("/resetState")
	public ResponseEntity<String> resetMyState() {
		System.out.println("Received a request to reset my state to initial state");
		this.status = STATUS_WHEN_NOT_STARTED_BIDDING;
		this.myLatestBid = BID_VALUE_WHEN_NOT_STARTED_BIDDING;
		return ResponseEntity.ok("My state has been " + myLatestBid);
	}

}
