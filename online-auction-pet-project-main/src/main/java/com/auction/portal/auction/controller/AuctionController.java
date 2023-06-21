package com.auction.portal.auction.controller;

import com.auction.portal.auction.dto.AuctionDto;
import com.auction.portal.auction.service.AuctionService;
import com.auction.portal.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private AuctionService auctionService;


    @GetMapping("/")
    public List<AuctionDto> getAllAuctionsSortedByExpiry() {
        return auctionService.getAllAuctionsSortedByExpiry();
    }

    @PostMapping("/save" )
    public AuctionDto saveAuction(@RequestBody AuctionDto auctionDto) {
        return auctionService.saveAuction(auctionDto);
    }

    @GetMapping("/{id}")
    public AuctionDto getAuctionById(@PathVariable int id) {
        return auctionService.getAuctionById(id);
    }

    @PutMapping("/update")
    public AuctionDto updateAuction(@RequestBody AuctionDto auctionDto) {
        return auctionService.updateAuction(auctionDto);
    }

    @DeleteMapping("/{id}")
    public String deleteAuctionById(@PathVariable int id) {
        return auctionService.deleteAuctionById(id);
    }
}
