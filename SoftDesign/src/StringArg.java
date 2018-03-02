public class StringArg extends Argument {//Аргумент строка
    StringArg(String str) {
        arg = str;
    }
    String getValue() {//Строковое представление
        return arg;
    }
}
