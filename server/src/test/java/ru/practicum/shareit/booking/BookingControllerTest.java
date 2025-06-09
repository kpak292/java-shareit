package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StateDto;
import ru.practicum.shareit.booking.entity.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.config.Constants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    @Qualifier("bookingServiceV1")
    BookingService bookingService;

    @Autowired
    MockMvc mockMvc;

    BookingDto bookingDto;
    DateTimeFormatter formatter;

    @BeforeEach
    public void dataPreparation() {
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().minusDays(1);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        ItemDto itemDto = ItemDto.builder()
                .id(3L)
                .name("Item Name")
                .description("Item Desc")
                .available(true)
                .build();

        bookingDto = BookingDto.builder()
                .id(1L)
                .booker(new UserDto(2L, "booker", "booker desc"))
                .item(itemDto)
                .start(start)
                .end(end)
                .status(State.WAITING)
                .build();

    }

    @Test
    public void findAllByUserIdTest() throws Exception {

        when(bookingService.findByUserId(Mockito.anyLong(), Mockito.any(StateDto.class)))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header(Constants.HEADER_FOR_USER_ID, 1L)
                        .param("state", "WAITING"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingDto.getStart().format(formatter))))
                .andExpect(jsonPath("$.[0].end", is(bookingDto.getEnd().format(formatter))))
                .andExpect(jsonPath("$.[0].item", is(bookingDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.[0].booker", is(bookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.[0].status", is(String.valueOf(bookingDto.getStatus()))));

        verify(bookingService, Mockito.times(1))
                .findByUserId(Mockito.anyLong(), Mockito.any(StateDto.class));
    }

    @Test
    public void findByIdTest() throws Exception {

        when(bookingService.findById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header(Constants.HEADER_FOR_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().format(formatter))))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().format(formatter))))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(bookingDto.getStatus()))));

        verify(bookingService, Mockito.times(1))
                .findById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    public void findAllByOwnerTest() throws Exception {

        when(bookingService.findByOwnerId(Mockito.anyLong(), Mockito.any(StateDto.class)))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header(Constants.HEADER_FOR_USER_ID, 1L)
                        .param("state", "WAITING"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingDto.getStart().format(formatter))))
                .andExpect(jsonPath("$.[0].end", is(bookingDto.getEnd().format(formatter))))
                .andExpect(jsonPath("$.[0].item", is(bookingDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.[0].booker", is(bookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.[0].status", is(String.valueOf(bookingDto.getStatus()))));

        verify(bookingService, Mockito.times(1))
                .findByOwnerId(Mockito.anyLong(), Mockito.any(StateDto.class));
    }

    @Test
    public void createTest() throws Exception {

        when(bookingService.create(Mockito.anyLong(), Mockito.any(BookingDto.class)))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header(Constants.HEADER_FOR_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().format(formatter))))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().format(formatter))))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(bookingDto.getStatus()))));

        verify(bookingService, Mockito.times(1))
                .create(Mockito.anyLong(), Mockito.any(BookingDto.class));
    }

    @Test
    public void approveTest() throws Exception {

        when(bookingService.approve(Mockito.anyLong(), Mockito.anyLong(),Mockito.anyBoolean()))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(Constants.HEADER_FOR_USER_ID, 1L)
                        .param("approved","true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().format(formatter))))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().format(formatter))))
                .andExpect(jsonPath("$.item", is(bookingDto.getItem()), ItemDto.class))
                .andExpect(jsonPath("$.booker", is(bookingDto.getBooker()), UserDto.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(bookingDto.getStatus()))));

        verify(bookingService, Mockito.times(1))
                .approve(Mockito.anyLong(), Mockito.anyLong(),Mockito.anyBoolean());
    }
}
