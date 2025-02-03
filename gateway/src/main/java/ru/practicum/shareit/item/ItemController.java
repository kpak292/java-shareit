package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    @Autowired
    private final ItemClient itemClient;

    @GetMapping()
    public ResponseEntity<Object> findAllByUser(@RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        log.info("GET/items from USER {}", userId);

        return itemClient.getAllByUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable long id,
                                           @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        log.info("GET/items/{} from User {}", id, userId);

        return itemClient.getById(userId, id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByText(@RequestParam String text) {
        log.info("GET/items/search?text={}", text);

        return itemClient.getByText(text);
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto itemDto,
                                         @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        log.info("POST/items from User {}", userId);

        return itemClient.create(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable long id,
                                         @RequestBody ItemRequestDto itemDto,
                                         @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        log.info("PATCH/items{} from User {}", id, userId);

        return itemClient.update(id, itemDto, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remove(@PathVariable long id,
                                         @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        log.info("DELETE/items/{}", id);

        return itemClient.delete(id, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable long itemId,
                                             @RequestBody CommentRequestDto commentDto,
                                             @RequestHeader(Constants.HEADER_FOR_USER_ID) long userId) {
        log.info("POST/items/{}/comment from user {}", itemId, userId);

        return itemClient.createComment(itemId, userId, commentDto);
    }
}
