package com.a_olumide.todo_application.dto;

import lombok.Data;

@Data
public class TodoDto {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
