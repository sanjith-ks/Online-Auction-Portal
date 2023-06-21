package com.auction.portal.scheduler;

import com.auction.portal.auction.dto.AuctionDto;
import com.auction.portal.auction.entity.Auction;
import com.auction.portal.auction.repository.AuctionRepository;
import com.auction.portal.bid.dto.BidDto;
import com.auction.portal.bid.entity.Bid;
import com.auction.portal.bid.repository.BidRepository;
import com.auction.portal.bid.service.BidService;
import com.auction.portal.user.dto.UserDto;
import com.auction.portal.user.entity.User;
import com.auction.portal.utils.EmailService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Log4j2
public class AuctionExpiryScheduler {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private BidService bidService;


    @Scheduled(cron = "0 0 0 * * *")
    public void checkExpiryOfAuctions() {
        log.debug("Scheduled task Executed!");
        List<Auction> auctions = auctionRepository.findAll();
        auctions.forEach(auction -> {
            boolean isExpired = setExpired(auction);
            auction.setExpired(isExpired);
            if (auction.isExpired()) {
                boolean isSold = setSold(auction);
                auction.setSold(isSold);
                setWinner(auction);
                auctionRepository.save(auction);
            }
        });

    }

    private void setWinner(Auction auction) {
        List<Bid> bids = auction.getBids();
        Collections.sort(bids, (o1, o2) -> (int) (o2.getBidValue() - o1.getBidValue()));
        if (bids.size() != 0) {
            Bid bid = bids.get(0);
            bid.setWinner(true);
            User winner = bid.getUser();
            bidRepository.save(bid);
            emailService.normalMail(winner.getEmail(),
                    "Congratulations!! You have won the bid for Auction with Id : " + auction.getId(),
                    "Winning the Bid");
        }
    }

    private boolean setSold(Auction auction) {
        if (!auction.getBids().isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean setExpired(Auction auction) {
        if (auction.getExpiryDate().isEqual(LocalDate.now())) {
            return true;
        }
        return false;
    }
}


