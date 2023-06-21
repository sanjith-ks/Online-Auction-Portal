package com.auction.portal.bid.service;

import com.auction.portal.auction.dto.AuctionDto;
import com.auction.portal.auction.repository.AuctionRepository;
import com.auction.portal.auction.service.AuctionService;
import com.auction.portal.bid.dto.BidDto;
import com.auction.portal.bid.entity.Bid;
import com.auction.portal.bid.repository.BidRepository;
import com.auction.portal.user.dto.UserDto;
import com.auction.portal.user.entity.User;
import com.auction.portal.user.repository.UserRepository;
import com.auction.portal.utils.CurrencyConverter;
import com.auction.portal.utils.ErrorCodes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrencyConverter converter;

    @Autowired
    private AuctionService auctionService;


    @Override
    public BidDto saveBid(BidDto bidDto) {
        User loggerInUser = getLoggerInUser();
        bidDto.setUser(modelMapper.map(loggerInUser, UserDto.class));
        if (!validateBid(bidDto)) {
            throw new IllegalArgumentException(ErrorCodes.BID_INVALID);
        }
        double basePrice = converter.convertIntoBase(loggerInUser.getCountry().getCurrencyCode(), bidDto.getBidValue());
        bidDto.setBidValue(basePrice);
        Bid bid = modelMapper.map(bidDto, Bid.class);
        Bid savedBid = bidRepository.save(bid);
        double userPrice = converter.convertIntoUser(getLoggerInUser().getCountry().getCurrencyCode(), savedBid.getBidValue());
        savedBid.setBidValue(userPrice);
        return modelMapper.map(savedBid, BidDto.class);
    }

    @Override
    public List<BidDto> getAllBidsSortedByValue() {
        List<Bid> bids = bidRepository.findAll();
        List<BidDto> bidDtos = bids.stream().map(bid -> {
            double userPrice = converter.convertIntoUser(getLoggerInUser().getCountry().getCurrencyCode(), bid.getBidValue());
            bid.setBidValue(userPrice);
            return modelMapper.map(bid, BidDto.class);
        }).collect(Collectors.toList());
        Collections.sort(bidDtos, (o1, o2) -> (int) (o2.getBidValue() - o1.getBidValue()));
        return bidDtos;
    }

    @Override
    public BidDto getBidById(int id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCodes.BID_NOT_FOUND));
        BidDto bidDto = modelMapper.map(bid, BidDto.class);
        double userPrice = converter.convertIntoUser(getLoggerInUser().getCountry().getCurrencyCode(), bidDto.getBidValue());
        bidDto.setBidValue(userPrice);
        return bidDto;
    }

    @Override
    public BidDto updateBid(BidDto bidDto) {
        bidRepository.findById(bidDto.getId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCodes.BID_NOT_FOUND));
        double userPrice = converter.convertIntoUser(getLoggerInUser().getCountry().getCurrencyCode(), bidDto.getBidValue());
        bidDto.setBidValue(userPrice);
        return saveBid(bidDto);
    }

    @Override
    public String deleteBidById(int id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCodes.BID_NOT_FOUND));
        bid.setDeletedAt(LocalDate.now());
        bidRepository.save(bid);
        return "Bid with Id : " + id + " has been deleted successfully!";
    }

    public boolean validateBid(BidDto bidDto) {
        AuctionDto auctionDto = auctionService.getAuctionById(bidDto.getAuction().getId());
        double minPrice = auctionDto.getMinPrice();
        BidDto highestBidForAuction = getHighestBidForAuction(auctionDto);
        double highestBidValue = converter.convertIntoUser(getLoggerInUser().getCountry().getCurrencyCode(), highestBidForAuction.getBidValue());
        if (bidDto.getBidValue() <= minPrice)
            throw new IllegalArgumentException(ErrorCodes.BID_MINVALUE_REQUIRED + " is " + minPrice);
        if (highestBidValue > bidDto.getBidValue() && auctionDto.isOpen())
            throw new IllegalArgumentException(ErrorCodes.BID_LESS_THAN_CURRENT_BID);
        if (highestBidValue == bidDto.getBidValue() && auctionDto.isOpen())
            throw new IllegalArgumentException(ErrorCodes.PRICE_ALREADY_BID);
        return true;
    }

    public BidDto getHighestBidForAuction(AuctionDto auctionDto) {
        List<BidDto> bids = auctionDto.getBids();
        if (!bids.isEmpty()) {
            Collections.sort(bids, (o1, o2) -> (int) (o1.getBidValue() - o2.getBidValue()));
            return bids.get(0);
        }
        return new BidDto();
    }

    @Override
    public User getLoggerInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User loggerInUser = userRepository.findByEmail(email).get();
        return loggerInUser;
    }

    @Override
    public List<BidDto> getAllBidsByUser(int userId) {
        if (getAllBidsSortedByValue() == null) return null;
        return getAllBidsSortedByValue().stream()
                .filter(bidDto -> bidDto.getUser().getId() == userId)
                .collect(Collectors.toList());
    }
}
