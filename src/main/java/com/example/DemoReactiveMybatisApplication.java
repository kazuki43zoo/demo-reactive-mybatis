package com.example;

import com.example.domain.Todo;
import com.example.mapper.TodoMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class DemoReactiveMybatisApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoReactiveMybatisApplication.class, args);
	}

	private final TodoMapper todoMapper;

	public DemoReactiveMybatisApplication(TodoMapper todoMapper) {
		this.todoMapper = todoMapper;
	}

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		// load test data
		{
			Todo newTodo = new Todo();
			newTodo.setTitle("飲み会");
			newTodo.setDetails("4/22 19:00 銀座");

			todoMapper.insert(newTodo);
		}
		{
			Todo newTodo = new Todo();
			newTodo.setTitle("お茶会");
			newTodo.setDetails("4/24 20:00 銀座");

			todoMapper.insert(newTodo);
		}
	}

}
