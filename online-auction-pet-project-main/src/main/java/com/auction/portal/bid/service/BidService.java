package com.auction.portal.bid.service;

import com.auction.portal.bid.dto.BidDto;
import com.auction.portal.user.entity.User;

import java.util.List;

public interface BidService {

    BidDto saveBid(BidDto bidDto);

    List<BidDto> getAllBidsSortedByValue();

    BidDto getBidById(int id);

    BidDto updateBid(BidDto BidDto);

    String deleteBidById(int id);

    User getLoggerInUser();

    List<BidDto> getAllBidsByUser(int userId);
}
