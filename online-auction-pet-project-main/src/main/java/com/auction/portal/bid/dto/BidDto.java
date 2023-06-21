package com.auction.portal.bid.dto;

import com.auction.portal.auction.dto.AuctionDto;
import com.auction.portal.auction.entity.Auction;
import com.auction.portal.common.dto.CommonDto;
import com.auction.portal.user.dto.UserDto;
import com.auction.portal.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidDto extends CommonDto {

    private double bidValue;

    private boolean isWinner;

    @JsonIgnoreProperties({"auctions","bids"})
    private UserDto user;

    @JsonIgnoreProperties({"bids","user"})
    private AuctionDto auction;

}
