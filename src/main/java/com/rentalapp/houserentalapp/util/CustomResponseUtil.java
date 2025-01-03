package com.rentalapp.houserentalapp.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponseUtil {

    public static <T> ResponseEntity<ResponseObject<T>> getSuccessResponse(T data, HttpStatus status) {

        ResponseObject<T> responseObject = new ResponseObject<>();

        responseObject.setData(data);
        responseObject.setOperation(ResponseObject.Operation.SUCCESS);
        return new ResponseEntity<>(responseObject, status);
    }

    public static <T> ResponseEntity<ResponseObject<T>> getFailureResponse(String error, HttpStatus status) {

        ResponseObject<T> responseObject = new ResponseObject<>();

        responseObject.setError(error);
        responseObject.setOperation(ResponseObject.Operation.FAILURE);
        return new ResponseEntity<>(responseObject, status);
    }

}
