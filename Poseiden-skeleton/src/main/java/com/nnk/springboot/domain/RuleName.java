package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "rulename")
@NoArgsConstructor
@Getter
@Setter
public class RuleName {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "{RuleName.Name.mandatory}")
    @Size(min= 1, max = 255, message="{RuleName.Name.size}")
    private String name;

    @Size(min= 1, max = 255, message="{RuleName.Description.size}")
    private String description;

    @Size(min= 1, max = 255, message="{RuleName.Json.size}")
    private String json;

    @Size(min= 1, max = 255, message="{RuleName.Template.size}")
    private String template;

    @Size(min= 1, max = 255, message="{RuleName.SqlStr.size}")
    @Column(name = "sql_string")
    private String sqlStr;

    @Size(min= 1, max = 255, message="{RuleName.SqlPart.size}")
    @Column(name = "sql_part")
    private String sqlPart;

    /**
     * <b>Constructor RuleName</b>
     * @param name name
     * @param description description
     * @param json json
     * @param template template
     * @param sqlStr sql Str
     * @param sqlPart sql Part
     */
    public RuleName(String name, String description, String json,
                    String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuleName)) return false;
        RuleName ruleName = (RuleName) o;
        return Objects.equals(name, ruleName.name) &&
                Objects.equals(description, ruleName.description) &&
                Objects.equals(json, ruleName.json) &&
                Objects.equals(template, ruleName.template) &&
                Objects.equals(sqlStr, ruleName.sqlStr) &&
                Objects.equals(sqlPart, ruleName.sqlPart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, json, template, sqlStr, sqlPart);
    }

    @Override
    public String toString() {
        return "RuleName{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", json='" + json + '\'' +
                ", template='" + template + '\'' +
                ", sqlStr='" + sqlStr + '\'' +
                ", sqlPart='" + sqlPart + '\'' +
                '}';
    }
}
