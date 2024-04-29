package Variant3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Author implements Externalizable {
    private String name;

    public Author() {}

    public Author(String name) {
        this.name = name;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
    }

    @Override
    public String toString() {
        return "Author{name='" + name + "'}";
    }
}

class Book implements Externalizable {
    private String title;
    private Author author;

    public Book() {}

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(title);
        out.writeObject(author);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        title = (String) in.readObject();
        author = (Author) in.readObject();
    }

    @Override
    public String toString() {
        return "Book{title='" + title + "', author=" + author + "}";
    }
}

class Reader implements Externalizable {
    private String name;

    public Reader() {}

    public Reader(String name) {
        this.name = name;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
    }

    @Override
    public String toString() {
        return "Reader{name='" + name + "'}";
    }
}

class Rental implements Externalizable {
    private Book book;
    private Reader reader;

    public Rental() {}

    public Rental(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(book);
        out.writeObject(reader);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        book = (Book) in.readObject();
        reader = (Reader) in.readObject();
    }

    @Override
    public String toString() {
        return "Rental{book=" + book + ", reader=" + reader + "}";
    }
}

class Library implements Externalizable {
    private List<Book> books;
    private List<Rental> rentals;

    public Library() {
        books = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        System.out.println("Writing objects to file...");
        out.writeInt(books.size());
        for (Book book : books) {
            book.writeExternal(out);
        }
        out.writeInt(rentals.size());
        for (Rental rental : rentals) {
            rental.writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        System.out.println("Reading objects from file...");
        int size = in.readInt();
        books = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Book book = new Book();
            book.readExternal(in);
            books.add(book);
        }
        int rentalSize = in.readInt();
        rentals = new ArrayList<>(rentalSize);
        for (int i = 0; i < rentalSize; i++) {
            Rental rental = new Rental();
            rental.readExternal(in);
            rentals.add(rental);
        }
    }

    @Override
    public String toString() {
        return "Library{books=" + books + ", rentals=" + rentals + "}";
    }
}

public class LibraryDriver {
    public static void main(String[] args) {
        try {
            // Створення об'єктів
            Author author = new Author("John Doe");
            Book book = new Book("Sample Book", author);
            Reader reader = new Reader("Alice");
            Rental rental = new Rental(book, reader);
            Library library = new Library();
            library.addBook(book);
            library.addRental(rental);

            // Виводимо початковий стан бібліотеки
            System.out.println("Initial state:");
            System.out.println(library);

            // Запис серіалізованих об'єктів у файл
            FileOutputStream fileOut = new FileOutputStream("library3.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            library.writeExternal(out);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in library3.ser");

            // Відновлення об'єктів з файлу
            FileInputStream fileIn = new FileInputStream("library3.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Library restoredLibrary = new Library();
            restoredLibrary.readExternal(in);
            in.close();
            fileIn.close();
            System.out.println("Deserialized data:");
            System.out.println(restoredLibrary);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
