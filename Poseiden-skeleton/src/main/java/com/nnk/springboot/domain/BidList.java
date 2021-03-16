package com.nnk.springboot.domain;

import com.nnk.springboot.web.dto.validation.DigitalNumber;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bidlist")
@NoArgsConstructor
@Getter
@Setter
public class BidList {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Account is mandatory")
    private String account;

    private String type;

    //These validation can only filter String entry
    //@Pattern(regexp = "[1-9][0-9]*|0" , message = "Entry has to be a numerical number !!")//-?\\d+(\\.\\d+)?
    //@DigitalNumber(message = "Entry has to be a numerical number !!")
    @DecimalMin(value= "0", inclusive = false, message = "Quantity have to be above 0")
    @Column(name = "bid_quantity")
    private Double bidQuantity;

    @Column(name = "ask_quantity")
    private Double askQuantity;

    private Double bid;

    private Double ask;

    private String benchmark;

    @Column(name = "bid_list_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime bidListDate;

    private String commentary;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BidList)) return false;
        BidList bidList = (BidList) o;
        return Objects.equals(account, bidList.account) &&
                Objects.equals(type, bidList.type) &&
                Objects.equals(bidQuantity, bidList.bidQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, type, bidQuantity);
    }

    @Override
    public String toString() {
        return "BidList{" +
                "account='" + account + '\'' +
                ", type='" + type + '\'' +
                ", bidQuantity=" + bidQuantity +
                '}';
    }
}


//Custom validator https://mkyong.com/spring-boot/spring-rest-validation-example/
//check number https://www.baeldung.com/java-check-string-number
//https://asbnotebook.com/2020/04/11/spring-boot-thymeleaf-form-validation-example/
//https://stackoverflow.com/questions/15488990/validating-double-and-float-values-using-hibernate-validator-bean-validation
//validating nested object https://nullbeans.com/how-to-use-java-bean-validation-in-spring-boot/
//custom error response https://www.springboottutorial.com/spring-boot-validation-for-rest-services