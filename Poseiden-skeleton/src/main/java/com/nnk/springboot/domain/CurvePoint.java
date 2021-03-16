package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "curvepoint")
@NoArgsConstructor
@Getter
@Setter
public class CurvePoint {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "{CurvePoint.CurveId.mandatory}")
    @Min(value = 0, message = "{CurvePoint.CurveId.min}")
    @Column(name = "curve_id")
    private Integer curveId;

    @Column(name = "as_of_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime asOfDate;

    @DecimalMin(value= "0", inclusive = false, message = "{CurvePoint.Term.validity}")
    private Double term;

    @DecimalMin(value= "0", inclusive = false, message = "{CurvePoint.Value.validity}")
    private Double value;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    /**
     * <b>Constructor CurvePoint</b>
     * @param curveId curveID
     * @param term term
     * @param value value
     */
    public CurvePoint(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurvePoint)) return false;
        CurvePoint that = (CurvePoint) o;
        return Objects.equals(curveId, that.curveId) &&
                Objects.equals(term, that.term) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curveId, term, value);
    }

    @Override
    public String toString() {
        return "CurvePoint{" +
                "curveId=" + curveId +
                ", term=" + term +
                ", value=" + value +
                '}';
    }

    //https://www.baeldung.com/jpa-default-column-values
    //Hibernate annotation for timestamp https://thorben-janssen.com/persist-creation-update-timestamps-hibernate/
    //https://stackoverflow.com/questions/811845/setting-a-jpa-timestamp-column-to-be-generated-by-the-database

}
