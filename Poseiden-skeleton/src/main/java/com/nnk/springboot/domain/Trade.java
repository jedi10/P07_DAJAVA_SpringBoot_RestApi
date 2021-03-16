package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "trade")
@NoArgsConstructor
@Getter
@Setter
public class Trade {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "{Trade.Account.mandatory}")
    @Size(min = 1, max = 255, message = "{Trade.Account.size}")
    private String account;

    @NotBlank(message = "{Trade.Type.mandatory}")
    @Size(min = 1, max = 255, message = "{Trade.Type.size}")
    private String type;

    @DecimalMin(value= "0", inclusive = false, message = "{Trade.BuyQuantity.validity}")
    @Column(name = "buy_quantity")
    private Double buyQuantity;

    @Column(name = "sell_quantity")
    private Double sellQuantity;

    @Column(name = "buy_price")
    private Double buyPrice;

    @Column(name = "sell_price")
    private Double sellPrice;

    private String benchmark;

    @Column(name = "trade_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime tradeDate;

    private String security;

    private String status;

    private String trader;

    private String book;

    @Column(name= "creation_name")
    private String creationName;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @Column(name = "revision_name")
    private String revisionName;

    @Column(name = "revision_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime revisionDate;

    @Column(name = "deal_name")
    private String dealName;

    @Column(name = "deal_type")
    private String dealType;

    @Column(name = "source_list_id")
    private String sourceListId;

    private String side;

    /**
     * <b>Constructor Trade</b>
     * @param account account
     * @param type type
     */
    public Trade(String account, String type) {
        this.account = account;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trade)) return false;
        Trade trade = (Trade) o;
        return Objects.equals(account, trade.account) &&
                Objects.equals(type, trade.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, type);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "account='" + account + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
