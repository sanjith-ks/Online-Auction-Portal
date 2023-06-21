package com.auction.portal.bid.controller;

import com.auction.portal.bid.dto.BidDto;
import com.auction.portal.bid.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    BidService bidService;
    @GetMapping("/")
    public List<BidDto> getAllBidsSortedByValue() {
        return bidService.getAllBidsSortedByValue();
    }

    @PostMapping("/save")
    public BidDto saveBid(@RequestBody BidDto bidDto){
        return bidService.saveBid(bidDto);
    }

    @GetMapping("/{id}")
    public BidDto getBidById(@PathVariable int id){
        return bidService.getBidById(id);
    }

    @PutMapping("/update")
    public BidDto updateBid(@RequestBody BidDto bidDto){
        return bidService.updateBid(bidDto);
    }

    @DeleteMapping("/{id}")
    public String deleteBidById(@PathVariable int id){
        return bidService.deleteBidById(id);
    }
}
