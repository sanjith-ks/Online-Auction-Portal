package com.auction.portal.bid.entity;

import com.auction.portal.auction.entity.Auction;
import com.auction.portal.common.entity.CommonEntity;
import com.auction.portal.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "bid")
@Getter
@Setter
@Where(clause = "deleted_at is null")
public class Bid extends CommonEntity {

    @Column(name = "bid_value")
    private double bidValue;

    @Column(name = "is_winner")
    private boolean isWinner;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "auction_id",referencedColumnName = "id")
    private Auction auction;
}
