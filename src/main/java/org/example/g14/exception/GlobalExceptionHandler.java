package org.example.g14.exception;

import org.example.g14.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(Exception e){
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(Exception e){
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleBadRequestDeserializeException(){
        return new ResponseEntity<>(new ErrorDto("Campos inválidos y/o faltantes."), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(Exception e){
        return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.CONFLICT);
    }

}
