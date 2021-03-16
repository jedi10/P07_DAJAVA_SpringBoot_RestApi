package com.nnk.springboot.web.dto.validation;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiResponse {

    private Object data;
    private String message;
    private boolean error = true;

    public ApiResponse(Object data, String message){
        this.data = data;
        this.message = message;
    }
}


//https://github.com/anicetkeric/custom-password-validation/blob/master/src/main/java/com/passay/sample/custompasswordvalidation/web/ApiResponse.java




