package com.TodoApp.springboot.springbootapp.todo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;

// class that controllers the pages 

@Controller
@SessionAttributes("name")

public class TodoControllerJPA {

	private TodoRepository todoRepository;

	public TodoControllerJPA(TodoRepository todoRepository) {
		super();
		this.todoRepository = todoRepository;
	}

	@RequestMapping("list-todos")
	public String listAllTodos(ModelMap model) {

		String username = getLoggedInUsername(model);
		List<Todo> todos = todoRepository.findByUsername(username);
		model.addAttribute("todos", todos);
		return "listTodos";
	}

	// You want to have a get request here so you only get the page but not post any
	// data
	@RequestMapping(value = "add-todo", method = RequestMethod.GET)
	public String showNewTodoPage(ModelMap model) {
		String username = getLoggedInUsername(model);

		Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false); // creates a default value, for the
																					// method below (two way binding
																					// between the controller and the
																					// jsp)
		model.put("todo", todo);
		return "todo";
	}

	// once you click on the add todo button you want the request to post the data
	// and redirect to the main page
	@RequestMapping(value = "add-todo", method = RequestMethod.POST)
	public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) { // add a todo not a default
																						// // value

		if (result.hasErrors()) {
			return "todo";
		}

		String username = getLoggedInUsername(model);
		todo.setUsername(username);

		todoRepository.save(todo);
		return "redirect:list-todos"; // redirects to the previous link where all todos are listed
	}

	@RequestMapping("delete-todo")
	public String deleteTodo(@RequestParam int id) {

		// delete todo

		todoRepository.deleteById(id);
		return "redirect:list-todos";
	}

	@RequestMapping(value = "update-todo", method = RequestMethod.GET)
	public String showUpdateTodoPage(@RequestParam int id, ModelMap model) {

		Todo todo = todoRepository.findById(id).get();
		model.addAttribute("todo", todo);
		// todoService.deleteById(id);
		return "todo";
	}

	@RequestMapping(value = "update-todo", method = RequestMethod.POST)
	public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {

		if (result.hasErrors()) {
			return "todo";
		}
		String username = getLoggedInUsername(model);
		todo.setUsername(username);
		todoRepository.save(todo);
		return "redirect:list-todos";
	}

	private String getLoggedInUsername(ModelMap model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return authentication.getName();
	}

}
