package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "trade")
@NoArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String account;

    @Getter
    @Setter
    private String type;

    @Column(name = "buy_quantity")
    @Getter
    @Setter
    private Double buyQuantity;

    @Column(name = "sell_quantity")
    @Getter
    @Setter
    private Double sellQuantity;

    @Column(name = "buy_price")
    @Getter
    @Setter
    private Double buyPrice;

    @Column(name = "sell_price")
    @Getter
    @Setter
    private Double sellPrice;

    @Getter
    @Setter
    private String benchmark;

    @Column(name = "trade_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime tradeDate;

    @Getter
    @Setter
    private String security;

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private String trader;

    @Getter
    @Setter
    private String book;

    @Column(name= "creation_name")
    @Getter
    @Setter
    private String creationName;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime creationDate;

    @Column(name = "revision_name")
    @Getter
    @Setter
    private String revisionName;

    @Column(name = "revision_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime revisionDate;

    @Column(name = "deal_name")
    @Getter
    @Setter
    private String dealName;

    @Column(name = "deal_type")
    @Getter
    @Setter
    private String dealType;

    @Column(name = "source_list_id")
    @Getter
    @Setter
    private String sourceListId;

    @Getter
    @Setter
    private String side;

    public Trade(String account, String type) {
        this.account = account;
        this.type = type;
    }
}
