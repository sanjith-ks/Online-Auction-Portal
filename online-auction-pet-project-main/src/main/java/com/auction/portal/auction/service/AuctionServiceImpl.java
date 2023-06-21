package com.auction.portal.auction.service;

import com.auction.portal.auction.dto.AuctionDto;
import com.auction.portal.auction.entity.Auction;
import com.auction.portal.auction.repository.AuctionRepository;
import com.auction.portal.bid.dto.BidDto;
import com.auction.portal.product.dto.ProductDto;
import com.auction.portal.product.service.ProductService;
import com.auction.portal.user.dto.UserDto;
import com.auction.portal.user.entity.User;
import com.auction.portal.user.repository.UserRepository;
import com.auction.portal.utils.CurrencyConverter;
import com.auction.portal.utils.ErrorCodes;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CurrencyConverter converter;

    @Override
    public AuctionDto saveAuction(AuctionDto auctionDto) {
        User loggerInUser = getLoggerInUser();
        String currencyCode = loggerInUser.getCountry().getCurrencyCode();
        log.info(loggerInUser.getEmail() + " - "
                + loggerInUser.getCountry().getCountry() + " - " + loggerInUser.getCountry().getCurrency());
        auctionDto.setUser(modelMapper.map(loggerInUser, UserDto.class));
        auctionDto.setCreatedAt(LocalDate.now());
        ProductDto productDto = auctionDto.getProduct();
        ProductDto savedProduct = productService.saveProduct(productDto);
        auctionDto.setProduct(savedProduct);
        if (!saveValidation(auctionDto)) {
            throw new DateTimeException(ErrorCodes.EXP_DATE_MIN_1DAY);
        }
        double basePrice = converter.convertIntoBase(currencyCode, auctionDto.getMinPrice());
        auctionDto.setMinPrice(basePrice);
        Auction auction = modelMapper.map(auctionDto, Auction.class);
        Auction savedAuction = auctionRepository.save(auction);
        double convertedPrice = converter.convertIntoUser(getLoggerInUser().getCountry().getCurrencyCode(),
                savedAuction.getMinPrice());
        savedAuction.setMinPrice(convertedPrice);
        AuctionDto savedAuctionDto = modelMapper.map(savedAuction, AuctionDto.class);
        savedAuctionDto.setRemainingTime(savedAuction.getExpiryDate().getDayOfMonth() - LocalDate.now().getDayOfMonth() + " days left.");
        List<BidDto> bidDtos = getBidDtosConverted(savedAuctionDto);
        savedAuctionDto.setBids(bidDtos);
        return savedAuctionDto;

    }

    private List<BidDto> getBidDtosConverted(AuctionDto savedAuctionDto) {
        List<BidDto> bidDtos = savedAuctionDto.getBids();
        if (bidDtos == null) return null;
        bidDtos.stream().map(bidDto -> {
            double userBidValue = converter.convertIntoUser(getLoggerInUser().getCountry().getCurrencyCode(),
                    bidDto.getBidValue());
            bidDto.setBidValue(userBidValue);
            return bidDto;
        }).collect(Collectors.toList());

        return bidDtos;
    }

    @Override
    public User getLoggerInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User loggerInUser = userRepository.findByEmail(email).get();
        return loggerInUser;
    }

    @Override
    public List<AuctionDto> getAllAuctionsSortedByExpiry() {
        User loggerInUser = getLoggerInUser();
        List<Auction> auctions1 = loggerInUser.getAuctions();
        List<Auction> auctions = auctionRepository.findAll();
        auctions.removeAll(auctions1);
        List<AuctionDto> auctionDtos = auctions.stream()
                .map(auction -> {
                    if (auction.isExpired()) return null;
                    if (!auction.isOpen()) auction.setBids(null);
                    double convertedPrice = converter.convertIntoUser(getLoggerInUser().getCountry().getCurrencyCode(),
                            auction.getMinPrice());
                    auction.setMinPrice(convertedPrice);
                    return modelMapper.map(auction, AuctionDto.class);
                }).collect(Collectors.toList());
        for (AuctionDto auctionDto : auctionDtos) {
            List<BidDto> bidDtos = getBidDtosConverted(auctionDto);
            auctionDto.setBids(bidDtos);
            int dayOfMonth = auctionDto.getExpiryDate().getDayOfMonth();
            auctionDto.setRemainingTime(dayOfMonth - LocalDate.now().getDayOfMonth() + " days left.");
        }
        Collections.sort(auctionDtos);
        return auctionDtos;
    }

    @Override
    public AuctionDto getAuctionById(int id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCodes.AUCTION_NOT_FOUND));
        Double convertedPrice = converter.convertIntoUser(getLoggerInUser().getCountry().getCurrencyCode(),
                auction.getMinPrice());
        auction.setMinPrice(convertedPrice);
        AuctionDto auctionDto = modelMapper.map(auction, AuctionDto.class);
        int dayOfMonth = auctionDto.getExpiryDate().getDayOfMonth();
        auctionDto.setRemainingTime(dayOfMonth - LocalDate.now().getDayOfMonth() + " days left.");
        List<BidDto> bidDtos = getBidDtosConverted(auctionDto);
        auctionDto.setBids(bidDtos);
        return auctionDto;
    }

    @Override
    public AuctionDto updateAuction(AuctionDto auctionDto) {
        auctionRepository.findById(auctionDto.getId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCodes.USER_NOT_FOUND));
        if (!saveValidation(auctionDto)) throw new DateTimeException(ErrorCodes.EXP_DATE_MIN_1DAY);
        Double basePrice = converter.convertIntoBase(getLoggerInUser().getCountry().getCurrencyCode(),
                auctionDto.getMinPrice());
        auctionDto.setMinPrice(basePrice);
        Auction savedAuction = auctionRepository.save(modelMapper.map(auctionDto, Auction.class));
        Double convertedPrice = converter.convertIntoUser(getLoggerInUser().getCountry().getCurrencyCode(),
                savedAuction.getMinPrice());
        savedAuction.setMinPrice(convertedPrice);
        AuctionDto auctionDto1 = modelMapper.map(savedAuction, AuctionDto.class);
        int dayOfMonth = auctionDto1.getExpiryDate().getDayOfMonth();
        auctionDto1.setRemainingTime(dayOfMonth - LocalDate.now().getDayOfMonth() + " days left.");
        List<BidDto> bidDtos = getBidDtosConverted(auctionDto1);
        auctionDto1.setBids(bidDtos);
        return auctionDto1;
    }

    @Override
    public String deleteAuctionById(int id) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCodes.USER_NOT_FOUND));
        auction.setDeletedAt(LocalDate.now());
        auctionRepository.save(auction);
        return "User with id : " + id + " has been deleted successfully.";
    }

    public Boolean saveValidation(AuctionDto auctionDto) {
        if (auctionDto.getExpiryDate().isBefore(auctionDto.getCreatedAt().plusDays(1))) {
            return false;
        }
        return true;
    }

    @Override
    public List<AuctionDto> getAuctionsByUser(int userId) {
        if (getAllAuctionsSortedByExpiry() == null) return null;
        return getAllAuctionsSortedByExpiry().stream()
                .filter(auctionDto -> auctionDto.getUser().getId() == userId)
                .collect(Collectors.toList());
    }
}
