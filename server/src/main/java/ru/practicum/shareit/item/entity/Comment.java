package ru.practicum.shareit.item.entity;

import com.querydsl.core.annotations.QueryExclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

@QueryExclude
@Entity
@Table(name = "comments", schema = "public")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    User author;

    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @Column(name = "comment", nullable = false)
    String text;

    @Column(name = "created_at")
    LocalDateTime created;
}
