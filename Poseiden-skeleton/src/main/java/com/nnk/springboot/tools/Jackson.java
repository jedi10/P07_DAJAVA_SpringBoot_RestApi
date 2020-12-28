package com.nnk.springboot.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;

import static java.time.temporal.ChronoField.*;

/**
 * <b>Tools based on Jackson API</b>
 * @author Jedy10
 */
public class Jackson {

    private static ObjectMapper mapper = new ObjectMapper()
            .registerModule(new SimpleModule().addSerializer(
                    new LocalDateSerializer(new DateTimeFormatterBuilder()
                            .appendValue(MONTH_OF_YEAR, 2)
                            .appendLiteral('-')
                            .appendValue(DAY_OF_MONTH, 2)
                            .appendLiteral('-')
                            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                            .toFormatter())))
            .registerModule(new SimpleModule().addDeserializer(LocalDate.class,
                    new LocalDateDeserializer(new DateTimeFormatterBuilder()
                            .appendValue(MONTH_OF_YEAR, 2)
                            .appendLiteral('-')
                            .appendValue(DAY_OF_MONTH, 2)
                            .appendLiteral('-')
                            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                            .toFormatter())));
    //.configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true);
    //https://stackoverflow.com/questions/23121765/write-objectnode-to-json-string-with-utf-8-characters-to-escaped-ascii


    /**
     * <b>Convert java Object to Json</b>
     * @param javaObject java Object
     * @return JSON string
     */
    public static String convertJavaToJson(Object javaObject){
        String expectedJson = null;
        try {
            expectedJson = mapper.writeValueAsString(javaObject);
            //System.out.println("ResultingJSONstring = " + expectedJson);
            //System.out.println(json);
            //https://blog.codota.com/how-to-convert-a-java-object-into-a-json-string/
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return expectedJson;
    }
}
