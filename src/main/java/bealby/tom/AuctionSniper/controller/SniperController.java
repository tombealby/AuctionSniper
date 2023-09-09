package bealby.tom.AuctionSniper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SniperController {
	
	private String status = "NotStartedBidding";
	
	@RequestMapping("/startBidding")
	public ResponseEntity<String> startBidding() {
		// TODO make request of Auction to start bidding
		System.out.println("Starting to bid");
		status = "biddingStarted";
		return ResponseEntity.ok("Started bidding");
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

}
