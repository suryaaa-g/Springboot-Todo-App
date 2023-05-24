package com.TodoApp.springboot.springbootapp.todo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// performs actions on Entities

public interface TodoRepository extends JpaRepository<Todo, Integer> {

	public List<Todo> findByUsername(String username);

}
