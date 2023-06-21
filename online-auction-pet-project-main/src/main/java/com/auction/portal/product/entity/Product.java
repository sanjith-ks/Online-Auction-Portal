package com.auction.portal.product.entity;

import com.auction.portal.common.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends CommonEntity {

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_image")
    private String productImageUri;

    private String category;

    private String description;
}
