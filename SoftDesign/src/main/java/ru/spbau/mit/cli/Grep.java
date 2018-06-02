package ru.spbau.mit.cli;

import com.beust.jcommander.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**Команда grep, ищущая в файле или входном потоке строки в которых найдено соответствие
 *поддерживает следующие аргументы:
 *-i нечуствительность к регистру
 *-w поиск слова целиком
 *-A [number] вывод number строк после совпадения*/
public class Grep implements Command {

    /**класс для проверки числа на не отрицательность*/
    public static class NonNegativeInteger implements IParameterValidator {

        @Override
        public void validate(String name, String value) throws ParameterException {
            try {
                int n = Integer.parseInt(value);
                if (n < 0) {
                    throw new ParameterException("parameter " + name + " should be positive, but " + value + " found");
                }
            } catch (NumberFormatException exception) {
                throw new ParameterException("parameter " + name + " should be number, but " + value + " found");
            }
        }
    }

    @Parameter(names = "-i", description = "case insensitivity")
    private boolean caseInsensitivity = false;

    @Parameter(names = "-w", description = "whole word search")
    private boolean wholeWord = false;

    @Parameter(names = "-A", description = "print num lines after current", validateWith = NonNegativeInteger.class)
    private int numLines = 0;

    @Parameter(description = "other arguments")
    private List<String> otherArgs = new ArrayList<>();

    /**Метод выполняющий команду для входного потока pin, возвращает поток*/
    @Override
    public PipedInputStream execute(List<Argument> args, PipedInputStream pin) {

        try {
            JCommander.newBuilder()
                    .addObject(this)
                    .build()
                    .parse(args.stream()
                            .map(Argument::getValue).toArray(String[]::new));
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            return null;
        }

        if (otherArgs.isEmpty()) {
            System.out.println("regexp expected");
            return null;
        }
        String regexp = otherArgs.get(0);
        if (wholeWord) regexp = "\\b" + regexp + "\\b";
        Pattern pattern;
        if (caseInsensitivity) {
            pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(regexp);
        }
        StringBuilder build = new StringBuilder();
        PipedInputStream rss = new PipedInputStream();
        try (PipedOutputStream pout = new PipedOutputStream()) {
            try {
                pout.connect(rss);
            } catch (IOException e) {
                throw new Error("Could not connect pipes");
            }
            BufferedReader fReader;
            if (otherArgs.size() > 1) {
                fReader = new BufferedReader(new InputStreamReader(new FileInputStream(otherArgs.get(1))));
            } else {
                if (pin != null) {
                    fReader = new BufferedReader(new InputStreamReader(pin));
                } else {
                    System.out.println("empty input");
                    return null;
                }
            }
            List<String> lines = fReader.lines()
                    .collect(Collectors.toList());
            int limit = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (pattern.matcher(lines.get(i)).find()) {
                    limit = i + numLines;
                }
                if (i <= limit) {
                    pout.write((lines.get(i) + '\n').getBytes());
                }
            }
            fReader.close();
            pout.flush();
        } catch (IOException e) {
            throw new Error("Pipes Error");
        }

        return rss;
    }
}
