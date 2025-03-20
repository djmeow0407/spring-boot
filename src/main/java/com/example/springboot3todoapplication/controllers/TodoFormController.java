package com.example.springboot3todoapplication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.springboot3todoapplication.services.TodoItemService;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.example.springboot3todoapplication.models.TodoItem;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class TodoFormController {

    @Autowired
    private TodoItemService todoItemService;

    @GetMapping("/create-todo")
    public String showCreateForm(TodoItem todoItem) {
        return "new-todo-item";
    }

    @PostMapping("/todo")
    public String createTodoItem(@Valid TodoItem todoItem, BindingResult result) {
        if (result.hasErrors()) {
            return "new-todo-item";
        }
        todoItemService.save(todoItem);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodoItem(@PathVariable("id") Long id) {
        todoItemService.getById(id).ifPresent(todoItemService::delete);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        TodoItem todoItem = todoItemService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));
        model.addAttribute("todo", todoItem);
        return "edit-todo-item";
    }

    @PostMapping("/todo/{id}")
    public String updateTodoItem(@PathVariable("id") Long id, @Valid TodoItem todoItem, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-todo-item";
        }
        TodoItem existingItem = todoItemService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));
        existingItem.setIsComplete(todoItem.getIsComplete());
        existingItem.setDescription(todoItem.getDescription());
        todoItemService.save(existingItem);
        return "redirect:/";
    }
}