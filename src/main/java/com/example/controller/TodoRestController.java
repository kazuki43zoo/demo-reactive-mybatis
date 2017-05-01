package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Todo;
import com.example.mapper.TodoMapper;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RequestMapping("/todos")
@RestController
public class TodoRestController {

	private final TodoMapper mapper;

	TodoRestController(TodoMapper mapper) {
		this.mapper = mapper;
	}

	@GetMapping
	Flux<Todo> getTodos() {
		return Flux.defer(() -> Flux.fromIterable(mapper.selectAll()))
				.subscribeOn(Schedulers.elastic());
	}

}
