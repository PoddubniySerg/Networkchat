package view;

import view.IAdmin;

import java.util.Scanner;

public class ConsoleAdmin implements IAdmin {

    private final Scanner scanner;

    public ConsoleAdmin() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getCommand() {
        return scanner.nextLine();
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void closeAdmin() {
        this.scanner.close();
    }
}
