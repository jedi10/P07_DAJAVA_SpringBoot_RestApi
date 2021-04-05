package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "rating")
@NoArgsConstructor
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Size(min= 1, max = 255, message="{Rating.MoodyRating.validity}")
    @Column(name = "moodys_rating")
    private String moodysRating;

    @Size(min= 1, max = 255, message="{Rating.SandPRating.validity}")
    @Column(name = "sand_p_rating")
    private String sandPRating;

    @Size(min= 1, max = 255, message="{Rating.FitchRating.validity}")
    @Column(name = "fitch_rating")
    private String fitchRating;

    @NotNull(message = "{Rating.OrderNumber.mandatory}")
    @Column(name = "order_number")
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
