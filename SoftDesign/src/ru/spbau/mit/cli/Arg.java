package ru.spbau.mit.cli;
/**Класс для аргументов*/
public class Arg extends Argument {
    public Arg(String str) {
        arg = str;
    }

    /**Строковое представление аргумента*/
    public String getValue() {
        return arg;
    }
}
