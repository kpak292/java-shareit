package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getAllByUser(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getById(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getByText(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search", null, parameters);
    }

    public ResponseEntity<Object> create(ItemRequestDto itemDto, long userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> update(long id, ItemRequestDto itemDto, long userId) {
        return patch("/" + id, userId, itemDto);
    }

    public ResponseEntity<Object> delete(long id, long userId) {
        return delete("/" + id, userId);
    }

    public ResponseEntity<Object> createComment(long id, long userId, CommentRequestDto commentRequestDto) {
        return post("/" + id + "/comment", userId, commentRequestDto);
    }
}
