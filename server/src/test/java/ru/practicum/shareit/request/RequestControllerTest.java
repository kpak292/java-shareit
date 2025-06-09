package ru.practicum.shareit.request;

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
import ru.practicum.shareit.config.Constants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
public class RequestControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    @Qualifier("itemRequestServiceV1")
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mockMvc;

    ItemRequestDto itemRequestDto;
    LocalDateTime created;
    DateTimeFormatter formatter;
    ItemDto itemDto;

    @BeforeEach
    public void dataPreparation() {
        created = LocalDateTime.now();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        itemDto = ItemDto.builder()
                .id(5L)
                .name("ItemName")
                .description("ItemDescription")
                .available(false)
                .requestId(1L)
                .hostId(7L)
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("desc")
                .created(created)
                .items(List.of(itemDto))
                .userId(7L)
                .build();

    }

    @Test
    public void findAllByUserTest() throws Exception {
        when(itemRequestService.findAllByUserId(Mockito.anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests")
                        .header(Constants.HEADER_FOR_USER_ID, 7L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is("desc")))
                .andExpect(jsonPath("$.[0].created", is(itemRequestDto.getCreated().format(formatter))))
                .andExpect(jsonPath("$.[0].items.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].items.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].items.[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.[0].items.[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.[0].items.[0].requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.[0].items.[0].hostId", is(itemDto.getHostId()), Long.class))
                .andExpect(jsonPath("$.[0].userId", is(itemRequestDto.getUserId()), Long.class));

        verify(itemRequestService, Mockito.times(1))
                .findAllByUserId(Mockito.anyLong());
    }

    @Test
    public void findAllTest() throws Exception {
        when(itemRequestService.findAll(Mockito.anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests/all")
                        .header(Constants.HEADER_FOR_USER_ID, 7L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is("desc")))
                .andExpect(jsonPath("$.[0].created", is(itemRequestDto.getCreated().format(formatter))))
                .andExpect(jsonPath("$.[0].items.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].items.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.[0].items.[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.[0].items.[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.[0].items.[0].requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.[0].items.[0].hostId", is(itemDto.getHostId()), Long.class))
                .andExpect(jsonPath("$.[0].userId", is(itemRequestDto.getUserId()), Long.class));

        verify(itemRequestService, Mockito.times(1))
                .findAll(Mockito.anyLong());
    }

    @Test
    public void findByIdTest() throws Exception {
        when(itemRequestService.findById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header(Constants.HEADER_FOR_USER_ID, 7L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is("desc")))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().format(formatter))))
                .andExpect(jsonPath("$.items.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.items.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.items.[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.items.[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.items.[0].requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.items.[0].hostId", is(itemDto.getHostId()), Long.class))
                .andExpect(jsonPath("$.userId", is(itemRequestDto.getUserId()), Long.class));

        verify(itemRequestService, Mockito.times(1))
                .findById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void create() throws Exception {
        when(itemRequestService.create(Mockito.any(ItemRequestDto.class), Mockito.anyLong()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header(Constants.HEADER_FOR_USER_ID, 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is("desc")))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated().format(formatter))))
                .andExpect(jsonPath("$.items.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.items.[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$.items.[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.items.[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.items.[0].requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.items.[0].hostId", is(itemDto.getHostId()), Long.class))
                .andExpect(jsonPath("$.userId", is(itemRequestDto.getUserId()), Long.class));

        verify(itemRequestService, Mockito.times(1))
                .create(Mockito.any(ItemRequestDto.class), Mockito.anyLong());
    }
}

