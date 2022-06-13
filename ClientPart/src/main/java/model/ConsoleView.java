package model;

import services.IView;

import java.util.Scanner;

public class ConsoleView implements IView {

    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getString() {
        return scanner.nextLine();
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void closeView() {
        scanner.close();
    }
}
