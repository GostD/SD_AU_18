public class ExecArg extends Argument {//Аргументы которые требуют вычисления
    ExecArg(String str) {
        arg = str;
    }
    String getValue() {//Строковое представление аргумента
        if (Interpretator.variables.containsKey(arg)) {
            Argument argm = Interpretator.variables.get(arg);
            return argm.getValue();
        } else {
            return "";
        }
    }
}
