package org.example;

import org.example.console.UserMenuHandler;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        Scanner scanner = new Scanner(System.in);
        UserMenuHandler menu = new UserMenuHandler(scanner, userService);
        menu.run();
    }
}
