package com.auction.portal.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "deleted_at")
    private LocalDate deletedAt = null;
}
