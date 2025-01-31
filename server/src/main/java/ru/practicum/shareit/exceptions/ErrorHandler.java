package ru.practicum.shareit.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ErrorResponse notFoundHandler(final NotFoundException e) {
        log.error(e.getMessage());
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse duplicateEmailHandler(final DuplicateEmailException e) {
        log.error(e.getMessage());
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse unauthorizedAccessHandler(final UnauthorizedAccessException e) {
        log.error(e.getMessage());
        return ErrorResponse.create(e, HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse notAvailableHandler(final NotAvailableException e, HttpServletResponse response) {
        log.error(e.getMessage());
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /*@ExceptionHandler
    public ErrorResponse methodArgumentNotValidHandler(final MethodArgumentNotValidException e) {
        log.error(e.getMessage());

        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }*/

    /*Изменил формат ответа, тк валидатор жестко ищет поле Error
    а у меня ответ в формате:
    {
    "type": "about:blank",
    "title": "Bad Request",
    "status": 400,
    "detail": "Validation failed for argument [0] in public org.springframework.http.ResponseEntity<ru.practicum.shareit.item.dto.ItemDto> ru.practicum.shareit.item.controller.ItemController.create(ru.practicum.shareit.item.dto.ItemDto,long): [Field error in object 'itemDto' on field 'name': rejected value [null]; codes [NotBlank.itemDto.name,NotBlank.name,NotBlank.java.lang.String,NotBlank]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [itemDto.name,name]; arguments []; default message [name]]; default message [не должно быть пустым]] ",
    "instance": "/items"
    }
     */
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidHandler(final MethodArgumentNotValidException e) {
        log.error(e.getMessage());

        Map<String, Object> responseException = new HashMap<>();
        responseException.put("error", e.getMessage());
        return new ResponseEntity<>(responseException, HttpStatus.BAD_REQUEST);
    }
}
