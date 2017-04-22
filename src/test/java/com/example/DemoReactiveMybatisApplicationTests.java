package com.example;

import com.example.domain.Todo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoReactiveMybatisApplicationTests {

	@LocalServerPort
	private int port;

	@Test
	public void contextLoads() throws InterruptedException {
		WebClient client = WebClient.create("http://localhost:" + port);

		CountDownLatch countDownLatchForAssertion = new CountDownLatch(2);

		final List<Todo> todos = new ArrayList<>();
		client.get()
			.uri("/todos")
			.exchange()
			.flatMapMany(response -> response.bodyToFlux(Todo.class))
			.subscribe(todo -> {
				todos.add(todo);
				countDownLatchForAssertion.countDown();
			});

		countDownLatchForAssertion.await(1, TimeUnit.SECONDS);

		assertThat(todos).hasSize(2);
		assertThat(todos.get(0).getId()).isEqualTo(1);
		assertThat(todos.get(0).getTitle()).isEqualTo("飲み会");
		assertThat(todos.get(0).getDetails()).isEqualTo("4/22 19:00 銀座");
		assertThat(todos.get(1).getId()).isEqualTo(2);
		assertThat(todos.get(1).getTitle()).isEqualTo("お茶会");
		assertThat(todos.get(1).getDetails()).isEqualTo("4/24 20:00 銀座");

	}

}
