import React, { useState, useEffect } from "react";
import axios from "axios";
import "./App.css";

function App() {
  const [books, setBooks] = useState([]);
  const [title, setTitle] = useState("");
  const [author, setAuthor] = useState("");
  const [selectedBook, setSelectedBook] = useState(null);

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    const response = await axios.get("http://localhost:8080/api/books");
    setBooks(response.data);
  };

  const addBook = async (e) => {
    e.preventDefault();
    await axios.post("http://localhost:8080/api/books", { title, author });
    setTitle("");
    setAuthor("");
    fetchBooks();
  };

  return (
    <div className="App">
      <h1>Book Management</h1>
      <form onSubmit={addBook}>
        <input
          type="text"
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          data-cy="title-input"
        />
        <input
          type="text"
          placeholder="Author"
          value={author}
          onChange={(e) => setAuthor(e.target.value)}
          data-cy="author-input"
        />
        <button type="submit" data-cy="add-book-button">
          Add Book
        </button>
      </form>
      <h2>Books</h2>
      <ul data-cy="book-list">
        {books.map((book) => (
          <li key={book.id}>
            {book.title} by {book.author}{" "}
            <button
              onClick={() => setSelectedBook(book)}
              data-cy={`view-details-${book.id}`}
            >
              View Details
            </button>
          </li>
        ))}
      </ul>

      {selectedBook && (
        <div className="book-details" data-cy="book-details">
          <h3>Book Details</h3>
          <p data-cy="detail-title">Title: {selectedBook.title}</p>
          <p data-cy="detail-author">Author: {selectedBook.author}</p>
        </div>
      )}
    </div>
  );
}

export default App;
