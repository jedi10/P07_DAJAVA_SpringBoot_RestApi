package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "bidlist")
@NoArgsConstructor
public class BidList {

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

    @Column(name = "bid_quantity")
    @Getter
    @Setter
    private Double bidQuantity;

    @Column(name = "ask_quantity")
    @Getter
    @Setter
    private Double askQuantity;

    @Getter
    @Setter
    private Double bid;

    @Getter
    @Setter
    private Double ask;

    @Getter
    @Setter
    private String benchmark;

    @Column(name = "bid_list_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime bidListDate;

    @Getter
    @Setter
    private String commentary;

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

    /**
     * <b>Constructor BidList</b>
     * @param account bid account name
     * @param type bid type
     * @param bidQuantity bid quantity
     */
    public BidList(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}
