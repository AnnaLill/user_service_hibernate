package org.example.dto;

import org.example.exception.ValidationException;


public class CreateUserDto {

    private final String name;
    private final String email;
    private final int age;

    public CreateUserDto(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }


    public void validate() {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Имя не может быть пустым.");
        }
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email не может быть пустым.");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            throw new ValidationException("Некорректный формат email.");
        }
        if (age < 0 || age > 150) {
            throw new ValidationException("Возраст должен быть от 0 до 150.");
        }
    }
}
