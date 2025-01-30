package ru.practicum.shareit.request.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.entity.Item;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_items", schema = "public")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @OneToOne
    @JoinColumn(name = "item_id")
    Item item;

    @OneToOne
    @JoinColumn(name = "request_id")
    ItemRequest request;

    @Column(name = "created_at")
    LocalDateTime created;
}
