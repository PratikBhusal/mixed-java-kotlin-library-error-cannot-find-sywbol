package org.example.hello.world;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        for (var arg : args) {
            System.out.println(arg);
        }
    }

    private Main() {}
}
