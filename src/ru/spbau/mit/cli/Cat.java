package ru.spbau.mit.cli;
import java.io.*;
import java.util.List;

public class Cat implements Command {

    /**Метод выполняющий команду для входного потока pin, возвращает поток*/
    public PipedInputStream execute(List<Argument> args, PipedInputStream p) throws FileNotFoundException {
        PipedOutputStream pout = new PipedOutputStream();
        PipedInputStream rss = new PipedInputStream();
        try {
            pout.connect(rss);
        } catch (IOException e) {
            throw new Error("Could not connect pipes");
        }
        if (!args.isEmpty()) {
            String fileName = args.get(0).arg;
            try {
                FileInputStream file = new FileInputStream(fileName);
                BufferedReader fReader = new BufferedReader(new InputStreamReader(file));
                while (fReader.ready()) {
                    String str = fReader.readLine();
                    pout.write(str.getBytes());
                    pout.write('\n');
                }
                fReader.close();
                file.close();
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("No such file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (p != null) {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(p));
            try {
                while (streamReader.ready()) {
                    String str = streamReader.readLine();
                    pout.write(str.getBytes());
                    pout.write('\n');
                    pout.flush();
                }
                streamReader.close();
                pout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            pout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rss;
    }
}
