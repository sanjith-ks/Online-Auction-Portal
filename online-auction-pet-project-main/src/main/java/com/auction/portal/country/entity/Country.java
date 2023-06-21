package com.auction.portal.country.entity;

import com.auction.portal.common.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "country")
@Getter
@Setter
public class Country extends CommonEntity {


    private String country;

    private String currency;

    @Column(name = "currency_code")
    private String currencyCode;
}
