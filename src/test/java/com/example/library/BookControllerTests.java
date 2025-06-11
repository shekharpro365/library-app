package com.example.library;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testAddBook() throws Exception {
		String newBookJson = "{\"title\":\"Test Book\",\"author\":\"Author Name\",\"price\":10.0}";
		mockMvc.perform(post("/books")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newBookJson))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.title").value("Test Book"))
				.andExpect(jsonPath("$.author").value("Author Name"))
				.andExpect(jsonPath("$.price").value(10.0));
	}

	@Test
	public void testGetBookByIdNotFound() throws Exception {
		mockMvc.perform(get("/books/999"))
				.andExpect(status().isNotFound())
				.andExpect(content().string("Book not found"));
	}
}
