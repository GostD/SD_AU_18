package ru.spbau.mit.cli;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**Класс неизвестных команд*/
public class UnknownCommand implements Command {
    private String cmd;

    UnknownCommand(String cmd) {
        this.cmd = cmd;
    }

    /**Метод выполняющий команду для входного потока pin, возвращает поток*/
    public PipedInputStream execute(List<Argument> args, PipedInputStream pin) {
        List<String> cm = new ArrayList<>();
        cm.add(cmd);
        if (cmd.equals("")) {
            System.out.println("Wrong command");
            return null;
        }
        for (Argument arg : args) {
            cm.add(arg.arg);
        }
        ProcessBuilder builder = new ProcessBuilder(cm);
        Process process = null;
        System.out.println(cmd);
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (pin != null) {
            OutputStream os = process.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(pin));
            try {
                while (reader.ready()) {
                    os.write(reader.readLine().getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (process == null) {
            System.out.println("Wrong command");
            return null;
        }

        InputStream rs = process.getInputStream();

        PipedInputStream p = new PipedInputStream();
        try (BufferedReader res = new BufferedReader(new InputStreamReader(rs)); PipedOutputStream pout = new PipedOutputStream()) {
            pout.connect(p);
            while (res.ready()) {
                pout.write((res.readLine()+'\n').getBytes());
            }
            pout.flush();
        } catch (IOException e) {
            throw new Error("Streams error");
        }
        return p;
    }
}
