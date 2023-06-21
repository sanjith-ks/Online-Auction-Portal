package com.auction.portal.auction.entity;

import com.auction.portal.bid.entity.Bid;
import com.auction.portal.common.entity.CommonEntity;
import com.auction.portal.product.entity.Product;
import com.auction.portal.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "auction")
@Getter
@Setter
public class Auction extends CommonEntity {

    @CreatedDate
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "min_price")
    private double minPrice;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "is_open")
    private boolean isOpen = true;

    @Column(name = "is_sold")
    private boolean isSold;

    @Column(name = "is_expired")
    private boolean isExpired;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER,targetEntity = Bid.class ,mappedBy = "auction")
    private List<Bid> bids;

}
