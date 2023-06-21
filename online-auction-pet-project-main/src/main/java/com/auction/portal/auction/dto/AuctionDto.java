package com.auction.portal.auction.dto;

import com.auction.portal.bid.dto.BidDto;
import com.auction.portal.common.dto.CommonDto;
import com.auction.portal.product.dto.ProductDto;
import com.auction.portal.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AuctionDto extends CommonDto implements Comparable<AuctionDto> {

    private LocalDate createdAt;

    private double minPrice;

    private LocalDate expiryDate;

    private boolean isOpen;

    private boolean isSold;

    private boolean isExpired;

    private ProductDto product;

    @JsonIgnoreProperties({"auctions","bids"})
    private UserDto user;

    @JsonIgnoreProperties({"auction"})
    private List<BidDto> bids;

    private String remainingTime;

    @Override
    public int compareTo(AuctionDto o) {
        if (this.getExpiryDate().isBefore(o.getExpiryDate())) return -1;
        else if (this.getExpiryDate().isAfter(o.getExpiryDate())) return 1;
        else return 0;
    }
}
