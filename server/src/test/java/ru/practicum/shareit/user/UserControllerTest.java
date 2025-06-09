package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    @Qualifier("userServiceV1")
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    UserDto userDto;

    @BeforeEach
    public void dataPreparation() {
        userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("test@test.uz")
                .build();
    }

    @Test
    public void findAllTest() throws Exception {
        when(userService.findAll())
                .thenReturn(List.of(userDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail())));

        verify(userService, Mockito.times(1))
                .findAll();
    }

    @Test
    public void getByIdTest() throws Exception {
        when(userService.findById(Mockito.anyLong()))
                .thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, Mockito.times(1))
                .findById(Mockito.anyLong());
    }

    @Test
    public void createTest() throws Exception {
        when(userService.create(Mockito.any(UserDto.class)))
                .thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, Mockito.times(1))
                .create(Mockito.any(UserDto.class));
    }

    @Test
    public void updateTest() throws Exception {
        when(userService.update(Mockito.anyLong(), Mockito.any(UserDto.class)))
                .thenReturn(userDto);

        mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, Mockito.times(1))
                .update(Mockito.anyLong(), Mockito.any(UserDto.class));
    }

    @Test
    public void removeTest() throws Exception {
        when(userService.remove(Mockito.anyLong()))
                .thenReturn(userDto);

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, Mockito.times(1))
                .remove(Mockito.anyLong());
    }
}
