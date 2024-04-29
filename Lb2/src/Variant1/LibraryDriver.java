package Variant1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Author implements Serializable {
    private String name;

    public Author(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                '}';
    }
}

class Book implements Serializable {
    private String title;
    private List<Author> authors;

    public Book(String title, List<Author> authors) {
        this.title = title;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + authors +
                '}';
    }
}

class BookStore implements Serializable {
    private String name;
    private List<Book> books;

    public BookStore(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "BookStore{" +
                "name='" + name + '\'' +
                ", books=" + books +
                '}';
    }
}

class BookReader implements Serializable {
    private String name;
    private List<Book> borrowedBooks;

    public BookReader(String name) {
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    @Override
    public String toString() {
        return "BookReader{" +
                "name='" + name + '\'' +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }
}

class Library implements Serializable {
    private String name;
    private List<BookStore> bookStores;
    private List<BookReader> readers;

    public Library(String name) {
        this.name = name;
        this.bookStores = new ArrayList<>();
        this.readers = new ArrayList<>();
    }

    public void addBookStore(BookStore bookStore) {
        bookStores.add(bookStore);
    }

    public void addReader(BookReader reader) {
        readers.add(reader);
    }

    @Override
    public String toString() {
        return "Library{" +
                "name='" + name + '\'' +
                ", bookStores=" + bookStores +
                ", readers=" + readers +
                '}';
    }

    public static void serializeObject(String fileName, Object obj) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
            os.writeObject(obj);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deSerializeObject(String fileName) {
        Object obj = null;
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            obj = is.readObject();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}

public class LibraryDriver {
    public static void main(String[] args) {
        // Створюємо авторів
        Author author1 = new Author("Author 1");
        Author author2 = new Author("Author 2");

        // Створюємо книги
        List<Author> authors1 = new ArrayList<>();
        authors1.add(author1);
        Book book1 = new Book("Book 1", authors1);

        List<Author> authors2 = new ArrayList<>();
        authors2.add(author2);
        Book book2 = new Book("Book 2", authors2);

        // Створюємо книжкові сховища та додаємо книги
        BookStore store1 = new BookStore("Store 1");
        store1.addBook(book1);

        BookStore store2 = new BookStore("Store 2");
        store2.addBook(book2);

        // Створюємо читача та видаємо йому книги
        BookReader reader1 = new BookReader("Reader 1");
        reader1.borrowBook(book1);
        reader1.borrowBook(book2);

        // Створюємо бібліотеку та додаємо книжкові сховища та читачів
        Library library = new Library("Library");
        library.addBookStore(store1);
        library.addBookStore(store2);
        library.addReader(reader1);

        // Виводимо початковий стан бібліотеки
        System.out.println("Initial state:");
        System.out.println(library);

        // Серіалізуємо та десеріалізуємо бібліотеку
        Library.serializeObject("library1.ser", library);
        Library deserializedLibrary = (Library) Library.deSerializeObject("library1.ser");

        // Виводимо десеріалізовану бібліотеку
        System.out.println("Deserialized state:");
        System.out.println(deserializedLibrary);
    }
}
