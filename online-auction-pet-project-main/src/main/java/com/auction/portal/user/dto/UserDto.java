package com.auction.portal.user.dto;

import com.auction.portal.auction.dto.AuctionDto;
import com.auction.portal.auction.entity.Auction;
import com.auction.portal.bid.dto.BidDto;
import com.auction.portal.bid.entity.Bid;
import com.auction.portal.common.dto.CommonDto;
import com.auction.portal.country.dto.CountryDto;
import com.auction.portal.country.entity.Country;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class UserDto extends CommonDto {
    @Email
    private String email;

    private String userName;

    private String password;

    private String role;

    private CountryDto country;

    @JsonIgnoreProperties("user")
    private List<AuctionDto> auctions;

    private List<BidDto> bids;
}
