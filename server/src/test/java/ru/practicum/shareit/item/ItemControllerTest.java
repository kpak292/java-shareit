package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.config.Constants;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    @Qualifier("itemServiceV1")
    ItemService itemService;

    @Autowired
    MockMvc mockMvc;

    ItemDto itemDto;
    LocalDateTime commentDate;
    DateTimeFormatter formatter;

    CommentDto commentDto;

    @BeforeEach
    public void dataPreparation() {
        commentDate = LocalDateTime.now().minusDays(1);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        commentDto = CommentDto.builder()
                .id(2L)
                .text("CommentText")
                .authorName("authorName")
                .created(commentDate)
                .build();

        BookingDto lastBooking = BookingDto.builder()
                .id(3L)
                .build();

        BookingDto nextBooking = BookingDto.builder()
                .id(4L)
                .build();

        itemDto = ItemDto.builder()
                .id(5L)
                .name("ItemName")
                .description("ItemDescription")
                .available(false)
                .comments(List.of(commentDto))
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .requestId(6L)
                .hostId(7L)
                .build();
    }

    @Test
    public void findAllByUserTest() throws Exception {

        when(itemService.findAllByUser(Mockito.anyLong()))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items")
                        .header(Constants.HEADER_FOR_USER_ID, 7L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is("ItemName")))
                .andExpect(jsonPath("$.[0].description", is("ItemDescription")))
                .andExpect(jsonPath("$.[0].available", is(false)))
                .andExpect(jsonPath("$.[0].comments.[0].id", is(2)))
                .andExpect(jsonPath("$.[0].comments.[0].text", is("CommentText")))
                .andExpect(jsonPath("$.[0].comments.[0].authorName", is("authorName")))
                .andExpect(jsonPath("$.[0].comments.[0].created", is(commentDate.format(formatter))))
                .andExpect(jsonPath("$.[0].lastBooking", is(itemDto.getLastBooking()), BookingDto.class))
                .andExpect(jsonPath("$.[0].nextBooking", is(itemDto.getNextBooking()), BookingDto.class))
                .andExpect(jsonPath("$.[0].requestId", is(6)))
                .andExpect(jsonPath("$.[0].hostId", is(7)));

        verify(itemService, Mockito.times(1))
                .findAllByUser(Mockito.anyLong());
    }

    @Test
    public void findByIdTest() throws Exception {
        when(itemService.findById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(get("/items/{id}", 5L)
                        .header(Constants.HEADER_FOR_USER_ID, 7L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is("ItemName")))
                .andExpect(jsonPath("$.description", is("ItemDescription")))
                .andExpect(jsonPath("$.available", is(false)))
                .andExpect(jsonPath("$.comments.[0].id", is(2)))
                .andExpect(jsonPath("$.comments.[0].text", is("CommentText")))
                .andExpect(jsonPath("$.comments.[0].authorName", is("authorName")))
                .andExpect(jsonPath("$.comments.[0].created", is(commentDate.format(formatter))))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking()), BookingDto.class))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking()), BookingDto.class))
                .andExpect(jsonPath("$.requestId", is(6)))
                .andExpect(jsonPath("$.hostId", is(7)));

        verify(itemService, Mockito.times(1))
                .findById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void findByTextTest() throws Exception {

        when(itemService.findByText(Mockito.anyString()))
                .thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "text"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is("ItemName")))
                .andExpect(jsonPath("$.[0].description", is("ItemDescription")))
                .andExpect(jsonPath("$.[0].available", is(false)))
                .andExpect(jsonPath("$.[0].comments.[0].id", is(2)))
                .andExpect(jsonPath("$.[0].comments.[0].text", is("CommentText")))
                .andExpect(jsonPath("$.[0].comments.[0].authorName", is("authorName")))
                .andExpect(jsonPath("$.[0].comments.[0].created", is(commentDate.format(formatter))))
                .andExpect(jsonPath("$.[0].lastBooking", is(itemDto.getLastBooking()), BookingDto.class))
                .andExpect(jsonPath("$.[0].nextBooking", is(itemDto.getNextBooking()), BookingDto.class))
                .andExpect(jsonPath("$.[0].requestId", is(6)))
                .andExpect(jsonPath("$.[0].hostId", is(7)));

        verify(itemService, Mockito.times(1))
                .findByText(Mockito.anyString());
    }

    @Test
    public void createTest() throws Exception {
        when(itemService.create(Mockito.any(ItemDto.class), Mockito.anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header(Constants.HEADER_FOR_USER_ID, 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is("ItemName")))
                .andExpect(jsonPath("$.description", is("ItemDescription")))
                .andExpect(jsonPath("$.available", is(false)))
                .andExpect(jsonPath("$.comments.[0].id", is(2)))
                .andExpect(jsonPath("$.comments.[0].text", is("CommentText")))
                .andExpect(jsonPath("$.comments.[0].authorName", is("authorName")))
                .andExpect(jsonPath("$.comments.[0].created", is(commentDate.format(formatter))))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking()), BookingDto.class))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking()), BookingDto.class))
                .andExpect(jsonPath("$.requestId", is(6)))
                .andExpect(jsonPath("$.hostId", is(7)));

        verify(itemService, Mockito.times(1))
                .create(Mockito.any(ItemDto.class), Mockito.anyLong());
    }

    @Test
    public void updateTest() throws Exception {
        when(itemService.update(Mockito.anyLong(), Mockito.any(ItemDto.class), Mockito.anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/{id}", 5L)
                        .header(Constants.HEADER_FOR_USER_ID, 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is("ItemName")))
                .andExpect(jsonPath("$.description", is("ItemDescription")))
                .andExpect(jsonPath("$.available", is(false)))
                .andExpect(jsonPath("$.comments.[0].id", is(2)))
                .andExpect(jsonPath("$.comments.[0].text", is("CommentText")))
                .andExpect(jsonPath("$.comments.[0].authorName", is("authorName")))
                .andExpect(jsonPath("$.comments.[0].created", is(commentDate.format(formatter))))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking()), BookingDto.class))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking()), BookingDto.class))
                .andExpect(jsonPath("$.requestId", is(6)))
                .andExpect(jsonPath("$.hostId", is(7)));

        verify(itemService, Mockito.times(1))
                .update(Mockito.anyLong(), Mockito.any(ItemDto.class), Mockito.anyLong());
    }

    @Test
    public void removeTest() throws Exception {
        when(itemService.remove(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(delete("/items/{id}", 5L)
                        .header(Constants.HEADER_FOR_USER_ID, 7L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is("ItemName")))
                .andExpect(jsonPath("$.description", is("ItemDescription")))
                .andExpect(jsonPath("$.available", is(false)))
                .andExpect(jsonPath("$.comments.[0].id", is(2)))
                .andExpect(jsonPath("$.comments.[0].text", is("CommentText")))
                .andExpect(jsonPath("$.comments.[0].authorName", is("authorName")))
                .andExpect(jsonPath("$.comments.[0].created", is(commentDate.format(formatter))))
                .andExpect(jsonPath("$.lastBooking", is(itemDto.getLastBooking()), BookingDto.class))
                .andExpect(jsonPath("$.nextBooking", is(itemDto.getNextBooking()), BookingDto.class))
                .andExpect(jsonPath("$.requestId", is(6)))
                .andExpect(jsonPath("$.hostId", is(7)));

        verify(itemService, Mockito.times(1))
                .remove(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void addCommentTest() throws Exception {
        when(itemService.addComment(Mockito.anyLong(), Mockito.any(CommentDto.class), Mockito.anyLong()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/{id}/comment", 5L)
                        .header(Constants.HEADER_FOR_USER_ID, 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.text", is("CommentText")))
                .andExpect(jsonPath("$.authorName", is("authorName")))
                .andExpect(jsonPath("$.created", is(commentDate.format(formatter))));

        verify(itemService, Mockito.times(1))
                .addComment(Mockito.anyLong(), Mockito.any(CommentDto.class), Mockito.anyLong());
    }
}
