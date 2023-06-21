package com.auction.portal.product.exception;

import com.auction.portal.product.dto.ResponseError;
import com.auction.portal.utils.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ImageExceptionAdvice {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException exc) {

        List<String> details = new ArrayList<String>();
        details.add(exc.getMessage());

        ResponseError err = new ResponseError(LocalDateTime.now(), ErrorCodes.FILE_NOT_FOUND, details);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException exc) {

        List<String> details = new ArrayList<String>();
        details.add(exc.getMessage());

        ResponseError err = new ResponseError(LocalDateTime.now(), ErrorCodes.IMAGE_SIZE_EXCEEDED,details);

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(err);
    }
}
