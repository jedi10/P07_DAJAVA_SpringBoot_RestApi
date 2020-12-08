package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "rating")
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;

    @Column(name = "moodys_rating")
    @Getter
    @Setter
    private String moodysRating;

    @Column(name = "sand_p_rating")
    @Getter
    @Setter
    private String sandPRating;

    @Column(name = "fitch_rating")
    @Getter
    @Setter
    private String fitchRating;

    @Column(name = "order_number")
    @Getter
    @Setter
    private Integer orderNumber;

    /**
     * <b>Constructor Rating</b>
     * @param moodysRating moodys Rating
     * @param sandPRating sand P Rating
     * @param fitchRating fitch Rating
     * @param orderNumber order Number
     */
    public Rating(String moodysRating, String sandPRating,
                  String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }
}
