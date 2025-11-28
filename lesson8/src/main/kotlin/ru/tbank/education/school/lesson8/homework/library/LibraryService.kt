package ru.tbank.education.school.lesson8.homework.library

class LibraryService {
    private val books = mutableMapOf<String, Book>()
    private val borrowedBooks = mutableMapOf<String, String>()
    private val borrowerFines = mutableMapOf<String, Int>()

    fun addBook(book: Book) {
        books[book.isbn] = book
    }

    fun borrowBook(isbn: String, borrower: String) {
        if (!books.containsKey(isbn)) {
            throw IllegalArgumentException("Такой книги нет")
        }
        if (borrowedBooks.contains(isbn)) {
            throw IllegalArgumentException("Книга уже была выдана")
        }
        if (hasOutstandingFines(borrower)) {
            throw IllegalArgumentException("У читателя есть непогашенный штраф")
        }
        borrowedBooks[isbn] = borrower
    }

    fun returnBook(isbn: String) {
        if (!borrowedBooks.contains(isbn)) {
            throw IllegalArgumentException("Книга не была выдана")
        }
        borrowedBooks.remove(isbn)
    }

    fun isAvailable(isbn: String): Boolean {
        return !borrowedBooks.contains(isbn)
    }

    fun calculateOverdueFine(isbn: String, daysOverdue: Int): Int {
        if (daysOverdue <= 10) { return 0 }
        val fine = (daysOverdue - 10) * 60
        val person = borrowedBooks[isbn]!!
        borrowerFines[person] = borrowerFines.getOrDefault(person, 0) + fine
        return fine
    }

    private fun hasOutstandingFines(borrower: String): Boolean {
        return (borrowerFines[borrower] ?: 0) > 0
    }
}