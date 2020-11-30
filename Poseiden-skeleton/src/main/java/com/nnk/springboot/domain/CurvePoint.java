package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "curvepoint")
@NoArgsConstructor
public class CurvePoint {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;

    @Column(name = "curve_id")
    @Getter
    @Setter
    private Integer curveId;

    @Column(name = "as_of_date", columnDefinition = "TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime asOfDate;

    @Getter
    @Setter
    private Double term;

    @Getter
    @Setter
    private Double value;

    @Column(name = "creation_date", columnDefinition = "TIMESTAMP")
    @Getter
    @Setter
    private LocalDateTime creationDate;


}
