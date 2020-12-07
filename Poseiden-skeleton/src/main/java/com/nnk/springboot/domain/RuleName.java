package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Table(name = "rulename")
@NoArgsConstructor
public class RuleName {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String name;


    @Getter
    @Setter
    private String description;


    @Getter
    @Setter
    private String json;


    @Getter
    @Setter
    private String template;

    @Column(name = "sql_string")
    @Getter
    @Setter
    private String sqlStr;

    @Column(name = "sql_part")
    @Getter
    @Setter
    private String sqlPart;

    public RuleName(String name, String description, String json,
                    String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }
}
