package ru.tbank.education.school.homework

/**
 * Исключение, которое выбрасывается при попытке забронировать занятое место
 */
class SeatAlreadyBookedException(message: String) : Exception(message)

/**
 * Исключение, которое выбрасывается при попытке забронировать место при отсутствии свободных мест
 */
class NoAvailableSeatException(message: String) : Exception(message)

data class BookedSeat(
    val movieId: String, // идентификатор фильма
    val seat: Int // номер места
)

class MovieBookingService(
    private val maxQuantityOfSeats: Int // Максимальное кол-во мест
) {
    private val booked = mutableListOf<BookedSeat>()
    init {
        if (maxQuantityOfSeats <= 0) {
            throw IllegalArgumentException("Не положительное количество мест в зале, мест должно быть больше 0")
        }
    }

    /**
     * Бронирует указанное место для фильма.
     *
     * @param movieId идентификатор фильма
     * @param seat номер места
     * @throws IllegalArgumentException если номер места вне допустимого диапазона
     * @throws NoAvailableSeatException если нет больше свободных мест
     * @throws SeatAlreadyBookedException если место уже забронировано
     */
    fun bookSeat(movieId: String, seat: Int) {
        if (seat <= 0 || seat > maxQuantityOfSeats) {
            throw IllegalArgumentException("Неправильное место, укажите место в диапозоне от 1 до $maxQuantityOfSeats")
        }
        var seatsBooked = 0
        for (i in 1..maxQuantityOfSeats) {
            if (isSeatBooked(movieId, i)) { seatsBooked++ }
        }

        if (seatsBooked == maxQuantityOfSeats) {
            throw NoAvailableSeatException("Места закончились, попробуйте другой фильм")
        }

        if (isSeatBooked(movieId, seat)) {
            throw SeatAlreadyBookedException("Место уже занято, выберите другое")
        }
        booked.add(BookedSeat(movieId, seat))
    }

    /**
     * Отменяет бронь указанного места.
     *
     * @param movieId идентификатор фильма
     * @param seat номер места
     * @throws NoSuchElementException если место не было забронировано
     */
    fun cancelBooking(movieId: String, seat: Int) {
        if (!isSeatBooked(movieId, seat)) {
            throw NoSuchElementException("Место было свободно, ошибка отмены бронирования")
        }
        booked.removeIf { it.movieId == movieId && it.seat == seat }
    }

    /**
     * Проверяет, забронировано ли место
     *
     * @return true если место занято, false иначе
     */
    fun isSeatBooked(movieId: String, seat: Int): Boolean {
        return booked.any { it.movieId == movieId && it.seat == seat }
    }
}