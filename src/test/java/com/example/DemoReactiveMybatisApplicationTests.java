package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.domain.Todo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoReactiveMybatisApplicationTests {
	@Autowired
	WebTestClient webClient;

	@Test
	public void contextLoads() throws InterruptedException {
		webClient.get().uri("/todos").exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(Todo.class)
				.hasSize(2).consumeWith(todos -> {
					{
						Todo todo = todos.get(0);
						assertThat(todo.getId()).isEqualTo(1);
						assertThat(todo.getTitle()).isEqualTo("飲み会");
						assertThat(todo.getDetails()).isEqualTo("4/22 19:00 銀座");
					}
					{
						Todo todo = todos.get(1);
						assertThat(todo.getId()).isEqualTo(2);
						assertThat(todo.getTitle()).isEqualTo("お茶会");
						assertThat(todo.getDetails()).isEqualTo("4/24 20:00 銀座");
					}
				});
	}
}
