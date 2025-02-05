package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service("itemServiceV1")
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    @Qualifier("userServiceV1")
    private UserService userService;

    @Autowired
    @Qualifier("bookingServiceV1")
    @Lazy
    private BookingService bookingService;

    @Autowired
    @Qualifier("itemRequestServiceV1")
    @Lazy
    ItemRequestService requestService;


    @Override
    public Collection<ItemDto> findAllByUser(long userId) {
        User host = userService.findUserById(userId);

        return itemRepository.findByHost(host).stream()
                .map(this::loadCommentsAndBookings)
                .toList();
    }

    @Override
    public ItemDto findById(long id, long userId) {
        //userService.findById(userId);

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item is not found with id = " + id));

        return loadCommentsAndBookings(item);
    }

    @Override
    public Item findItemById(long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item is not found with id = " + id));
    }

    @Override
    public Collection<ItemDto> findByText(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text).stream()
                .filter(Item::isAvailable)
                .map(ItemMapper.INSTANCE::getItemDto)
                .toList();
    }

    @Override
    public ItemDto create(ItemDto itemDto, long userId) {
        User user = userService.findUserById(userId);

        Item item = ItemMapper.INSTANCE.getItem(itemDto);
        item.setHost(user);

        item = itemRepository.save(item);

        if (itemDto.getRequestId() != null) {
            //Check request exists
            ItemRequest itemRequest = requestService.getbyId(itemDto.getRequestId());

            requestService.create(item, itemRequest);
        }

        return ItemMapper.INSTANCE.getItemDto(item);
    }

    @Override
    public ItemDto update(long id, ItemDto itemDto, long userId) {
        User host = userService.findUserById(userId);
        Item item = itemRepository.findByIdAndHost(id, host)
                .orElseThrow(() -> new NotFoundException("Item is not found with id = " + id +
                        "related to user with id = " + userId));

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.INSTANCE.getItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto remove(long id, long userId) {
        User host = userService.findUserById(userId);

        Item item = itemRepository.findByIdAndHost(id, host)
                .orElseThrow(() -> new NotFoundException("Item is not found with id = " + id +
                        "related to user with id = " + userId));

        itemRepository.deleteById(id);

        return ItemMapper.INSTANCE.getItemDto(item);
    }

    @Override
    public CommentDto addComment(long itemId, CommentDto commentDto, long userId) {
        //Check user exists
        User author = userService.findUserById(userId);

        //Check item exists
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item is not found with id = " + itemId));

        Collection<BookingDto> bookingDtos = bookingService.findByItemAndUser(author, item);

        if (bookingDtos.isEmpty()) {
            throw new NotAvailableException("User does not have PAST bookings of this Item");
        }

        Comment comment = CommentMapper.INSTANCE.getComment(commentDto);

        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        comment = commentRepository.save(comment);

        return CommentMapper.INSTANCE.getCommentDto(comment);
    }

    private ItemDto loadCommentsAndBookings(Item item) {
        Collection<Comment> comments = commentRepository.findAllByItem(item);

        ItemDto itemDto = ItemMapper.INSTANCE.getItemDto(item);
        itemDto.setComments(comments.stream()
                .map(CommentMapper.INSTANCE::getCommentDto)
                .toList());

        Optional<Booking> last = bookingService.findLastBooking(item);
        last.ifPresent(booking -> itemDto.setLastBooking(BookingMapper.INSTANCE.getBookingDto(booking)));

        Optional<Booking> next = bookingService.findNextBooking(item);
        last.ifPresent(booking -> itemDto.setNextBooking(BookingMapper.INSTANCE.getBookingDto(booking)));

        return itemDto;
    }
}
