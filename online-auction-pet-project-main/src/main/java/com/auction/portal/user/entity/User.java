package com.auction.portal.user.entity;

import com.auction.portal.auction.entity.Auction;
import com.auction.portal.bid.entity.Bid;
import com.auction.portal.common.entity.CommonEntity;
import com.auction.portal.country.entity.Country;
import com.auction.portal.utils.ErrorCodes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@Where(clause = "deleted_at is null")
public class User extends CommonEntity {

    @Column(unique = true)
    @NotNull(message = ErrorCodes.EMAIL_NOT_NULL)
    private String email;

    @Column(name = "user_name")
    @NotNull(message = ErrorCodes.USER_NAME_NOT_NULL)
    private String userName;

    private String password;

    private String role;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "country_id" , referencedColumnName = "id")
    private Country country;

    @JsonIgnoreProperties("user")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Auction> auctions;

    @JsonIgnoreProperties("user")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Bid> bids;




}
