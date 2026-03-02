package org.example;

import org.example.model.User;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args ) {
        UserService userService = new UserServiceImpl();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== User Service ===");
            System.out.println("1. Создать пользователя");
            System.out.println("2. Показать всех пользователей");
            System.out.println("3. Найти пользователя по id");
            System.out.println("4. Обновить email и возраст пользователя");
            System.out.println("5. Удалить пользователя по id");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Возраст: ");
                    int age = Integer.parseInt(scanner.nextLine());

                    User user = new User(null, name, email, age, LocalDateTime.now());
                    userService.create(user);
                    System.out.println("Пользователь создан: " + user);
                }
                case "2" -> {
                    System.out.println("Список пользователей:");
                    userService.findAll().forEach(System.out::println);
                }
                case "3" -> {
                    System.out.print("ID пользователя: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    Optional<User> found = userService.findById(id);
                    System.out.println(found.map(Object::toString).orElse("Пользователь не найден"));
                }
                case "4" -> {
                    System.out.print("ID пользователя: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    Optional<User> existing = userService.findById(id);
                    if (existing.isEmpty()) {
                        System.out.println("Пользователь не найден");
                        break;
                    }
                    User user = existing.get();
                    System.out.print("Новый email: ");
                    user.setEmail(scanner.nextLine());
                    System.out.print("Новый возраст: ");
                    user.setAge(Integer.parseInt(scanner.nextLine()));
                    userService.update(user);
                    System.out.println("Пользователь обновлён: " + user);
                }
                case "5" -> {
                    System.out.print("ID пользователя: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    userService.delete(id);
                    System.out.println("Пользователь (если был) удалён.");
                }
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неизвестная команда");
            }
        }
    }

}
