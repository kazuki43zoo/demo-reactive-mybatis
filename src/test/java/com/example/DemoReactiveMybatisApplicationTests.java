package com.example;

import com.example.domain.Todo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoReactiveMybatisApplicationTests {

	@LocalServerPort
	private int port;

	@Test
	public void contextLoads() throws InterruptedException {
		WebClient client = WebClient.create("http://localhost:" + port);

		final List<Todo> todos = client.get()
			.uri("/todos")
			.exchange()
			.flatMapMany(response -> response.bodyToFlux(Todo.class))
			.collectList()
			.block(Duration.ofSeconds(1));

		assertThat(todos).hasSize(2);
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
	}

}
