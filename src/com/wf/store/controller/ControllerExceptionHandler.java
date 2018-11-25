package com.wf.store.controller;

import com.wf.store.exception.EntityWasNotFoundException;
import com.wf.store.exception.NotEnoughProductsInWarehouseException;
import com.wf.store.exception.WarehouseServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotEnoughProductsInWarehouseException.class)
    public ResponseEntity<String> NotEnoughProductsInWarehouse(final NotEnoughProductsInWarehouseException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
    }

    @ExceptionHandler(EntityWasNotFoundException.class)
    public ResponseEntity<String> notFoundException(final EntityWasNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WarehouseServiceUnavailableException.class)
    public ResponseEntity<String> warehouseIsUnavailable(final WarehouseServiceUnavailableException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
