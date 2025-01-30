package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.Constants;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    @Autowired
    @Qualifier("itemRequestServiceV1")
    ItemRequestService requestService;

    @GetMapping
    public ResponseEntity<Collection<ItemRequestDto>> findAllByUser(
            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {

        Collection<ItemRequestDto> requestDtos = requestService.findAllByUserId(userId);

        log.info("GET/requests from User {} with with response {}", userId, requestDtos);

        return new ResponseEntity<>(
                requestDtos,
                HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<ItemRequestDto>> findAll(
            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {

        Collection<ItemRequestDto> requestDtos = requestService.findAll(userId);

        log.info("GET/requests/all from User {} with with response {}", userId, requestDtos);

        return new ResponseEntity<>(
                requestDtos,
                HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> findById(
            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
            @PathVariable long requestId) {

        ItemRequestDto requestDto = requestService.findById(requestId, userId);

        log.info("GET/requests/{} from User {} with with response {}", requestId, userId, requestDto);

        return new ResponseEntity<>(
                requestDto,
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemRequestDto> create(@RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequestDto request = requestService.create(itemRequestDto, userId);

        log.info("POST/requests from User {} with body {}", userId, itemRequestDto);

        return new ResponseEntity<>(
                request,
                HttpStatus.OK);
    }
}
