package com.a_olumide.todo_application.service.impl;

import com.a_olumide.todo_application.dto.TodoDto;
import com.a_olumide.todo_application.entity.Todo;
import com.a_olumide.todo_application.exception.ResourceNotFoundException;
import com.a_olumide.todo_application.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {
    @Mock
    private TodoRepository todoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    private TodoDto todoDto;
    private Todo todo;

    @BeforeEach
    void setUp() {
        todoDto = new TodoDto();
        todoDto.setTitle("Learn Driving");
        todoDto.setDescription("Driving tutorials");

        todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Learn Driving");
        todo.setDescription("Driving tutorials");
    }

    @Test
    void addTodo() {
        when(modelMapper.map(todoDto, Todo.class)).thenReturn(todo);
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        when(modelMapper.map(todo, TodoDto.class)).thenReturn(todoDto);

        TodoDto result = todoService.addTodo(todoDto);


        assertEquals(todoDto.getTitle(), result.getTitle());
        assertEquals(todoDto.getDescription(), result.getDescription());
    }

    @Test
    void getTodo() {

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoDto.setId(1L);
        when(modelMapper.map(todo, TodoDto.class)).thenReturn(todoDto);

        TodoDto result = todoService.getTodo(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Learn Driving", result.getTitle());
        assertEquals("Driving tutorials", result.getDescription());

        verify(todoRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(todo, TodoDto.class);
    }

    @Test
    void getTodo_NotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> todoService.getTodo(1L));
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void getAllTodos() {
        List<Todo> todos = new ArrayList<>();
        todos.add(todo);
        when(todoRepository.findAll()).thenReturn(todos);
        when(modelMapper.map(todo, TodoDto.class)).thenReturn(todoDto);

        List<TodoDto> result = todoService.getAllTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Learn Driving", result.get(0).getTitle());
    }

    @Test
    void updateTodo() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        TodoDto updatedDto = new TodoDto();
        updatedDto.setTitle("Updated Title");
        updatedDto.setDescription("Updated Description");
        updatedDto.setCompleted(true);

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        when(modelMapper.map(todo, TodoDto.class)).thenReturn(updatedDto);

        TodoDto result = todoService.updateTodo(updatedDto, 1L);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertTrue(result.isCompleted());
        verify(todoRepository, times(1)).save(todo);
        verify(modelMapper, times(1)).map(todo, TodoDto.class);
    }

}