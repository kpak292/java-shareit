package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.entity.Booking;

import java.util.Collection;


@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    Booking getBooking(BookingDto bookingDto);

    BookingDto getBookingDto(Booking booking);

    Collection<BookingDto> mapToBookingDto(Iterable<Booking> bookings);
}
