package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.dto.RequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    @Autowired
    RequestClient requestClient;

    private final String userHeader = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> findAllByUser(
            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        log.info("GET/requests from User {}", userId);

        return requestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(
            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        log.info("GET/requests/all from User {}", userId);

        return requestClient.getRequestsAll(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(
            @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
            @PathVariable long requestId) {
        log.info("GET/requests/{} from User {}", requestId, userId);

        return requestClient.getRequestsById(userId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(Constants.HEADER_FOR_USER_ID) long userId,
                                         @RequestBody RequestDto itemRequestDto) {
        log.info("POST/requests from User {}", userId);

        return requestClient.create(userId, itemRequestDto);
    }
}
