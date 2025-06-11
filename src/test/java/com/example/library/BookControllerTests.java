package com.example.library;

import com.example.library.controller.BookController;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BookRepository bookRepository;


	@Test
	public void testAddBook() throws Exception {
		Book book = new Book();
		book.setId(1L);
		book.setTitle("Test Book");
		book.setAuthor("Author Name");
		book.setPrice(10.0);

		when(bookRepository.save(any(Book.class))).thenReturn(book);

		String newBookJson = "{\"title\":\"Test Book\",\"author\":\"Author Name\",\"price\":10.0}";

		mockMvc.perform(post("/books")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newBookJson))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.title").value("Test Book"))
				.andExpect(jsonPath("$.author").value("Author Name"))
				.andExpect(jsonPath("$.price").value(10.0));
	}

	@Test
	public void testGetBookByIdNotFound() throws Exception {
		when(bookRepository.findById(999L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/books/999"))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Book not found"));
	}
}
