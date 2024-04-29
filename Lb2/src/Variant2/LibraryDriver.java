package Variant2;

import java.io.*;
import java.util.ArrayList;

class Author {
    private String name;

    public Author(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author{name='" + name + "'}";
    }

}

class Reader {
    private String name;

    public Reader(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Reader{name='" + name + "'}";
    }
}

class Book {
    private String title;
    private ArrayList<Author> authors;

    public Book(String title) {
        this.title = title;
        this.authors = new ArrayList<>();
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    @Override
    public String toString() {
        return "Book{title='" + title + "', authors=" + authors + "}";
    }
}

class Library implements Serializable {
    private transient ArrayList<Book> books;
    private transient ArrayList<Reader> readers;

    public Library() {
        this.books = new ArrayList<>();
        this.readers = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addReader(Reader reader) {
        readers.add(reader);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(books.size());
        for (Book book : books) {
            out.writeObject(book.getTitle());
            out.writeInt(book.getAuthors().size());
            for (Author author : book.getAuthors()) {
                out.writeObject(author.toString());
            }
        }
        out.writeInt(readers.size());
        for (Reader reader : readers) {
            out.writeObject(reader.toString());
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        books = new ArrayList<>();
        int bookSize = in.readInt();
        for (int i = 0; i < bookSize; i++) {
            String title = (String) in.readObject();
            int authorSize = in.readInt();
            Book book = new Book(title);
            for (int j = 0; j < authorSize; j++) {
                book.addAuthor(new Author((String) in.readObject()));
            }
            books.add(book);
        }
        readers = new ArrayList<>();
        int readerSize = in.readInt();
        for (int i = 0; i < readerSize; i++) {
            readers.add(new Reader((String) in.readObject()));
        }
    }

    @Override
    public String toString() {
        return "Library{books=" + books + ", readers=" + readers + "}";
    }
}

public class LibraryDriver  {
    public static void main(String[] args) {
        Library library = new Library();
        Book book1 = new Book("Book 1");
        book1.addAuthor(new Author("Author 1"));
        book1.addAuthor(new Author("Author 2"));
        library.addBook(book1);
        Book book2 = new Book("Book 2");
        book2.addAuthor(new Author("Author 3"));
        library.addBook(book2);
        Reader reader1 = new Reader("John");
        library.addReader(reader1);

        // Виводимо початковий стан бібліотеки
        System.out.println("Initial state:");
        System.out.println(library);

        // Серіалізація
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("library2.ser"))) {
            oos.writeObject(library);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Десеріалізація
        Library deserializedLibrary = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("library2.ser"))) {
            deserializedLibrary = (Library) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (deserializedLibrary != null) {
            // Виводимо десеріалізовану бібліотеку
            System.out.println("Deserialized state:");
            System.out.println(deserializedLibrary);
        }
    }
}
