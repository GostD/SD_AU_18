package ru.spbau.mit.cli;
/**Базовый класс для аргументов*/
public class Argument {
    protected String arg;

    public Argument(String str) {
        arg = str;
    }

    /**Строковое представление аргумента*/
    public String getValue() {
        return arg;
    }
}
