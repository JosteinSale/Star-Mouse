package main_classes;

public class MainClass {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.d3d", "true"); // Enable hardware acceleration
        new Game();
    }
}