package ru.practicum.shareit.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @OneToOne
    @JoinColumn(name = "booker_id")
    User booker;

    @OneToOne
    @JoinColumn(name = "item_id")
    Item item;

    @Column(name = "booking_start")
    LocalDateTime start;

    @Column(name = "booking_end")
    LocalDateTime end;

    @Enumerated(EnumType.STRING)
    State status;

}
