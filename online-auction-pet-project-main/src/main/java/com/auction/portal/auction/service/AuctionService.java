package com.auction.portal.auction.service;

import com.auction.portal.auction.dto.AuctionDto;
import com.auction.portal.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AuctionService {

    AuctionDto saveAuction(AuctionDto auctionDto);

    User getLoggerInUser();

    List<AuctionDto> getAllAuctionsSortedByExpiry();

    AuctionDto getAuctionById(int id);

    AuctionDto updateAuction(AuctionDto auctionDto);

    String deleteAuctionById(int id);

    List<AuctionDto> getAuctionsByUser(int userId);
}
