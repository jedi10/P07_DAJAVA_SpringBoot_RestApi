package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

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

    @NotNull(message = "orderNumber is mandatory")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rating)) return false;
        Rating rating = (Rating) o;
        return Objects.equals(moodysRating, rating.moodysRating) &&
                Objects.equals(sandPRating, rating.sandPRating) &&
                Objects.equals(fitchRating, rating.fitchRating) &&
                Objects.equals(orderNumber, rating.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moodysRating, sandPRating, fitchRating, orderNumber);
    }

    @Override
    public String toString() {
        return "Rating{" +
                "moodysRating='" + moodysRating + '\'' +
                ", sandPRating='" + sandPRating + '\'' +
                ", fitchRating='" + fitchRating + '\'' +
                ", orderNumber=" + orderNumber +
                '}';
    }
}
