package ru.geekbrains.lesson2.online;

public class Main {

    private interface Animal {
        void breath();
        void look();
    }
    private interface Human extends Animal{
        void talk();
        default void walk(){
            System.out.println("walk on legs");
        }
    }

    private interface Bull extends Animal{
        default void walk(){
            System.out.println("walk on hooves");
        }
        void voice();
    }

    private static class Minotaur implements Human, Bull{
        @Override public void breath() {}
        @Override public void look() { }
        @Override public void talk() { }
        @Override public void voice() { }
        @Override public void walk() { Human.super.walk(); }
    }

    private static class Man implements Human{
        @Override public void breath() {}
        @Override public void look() {}
        @Override public void talk() {}
        @Override public void walk() {}
    }

    public static void main(String[] args) {
        Minotaur ivan = new Minotaur();
        ivan.walk();
    }
}
