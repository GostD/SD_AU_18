package ru.spbau.mit.cli;
import java.io.*;
import java.util.List;

public class Wc implements Command {

    /**Метод выполняющий команду для входного потока pin, возвращает поток*/
    public PipedInputStream execute(List<Argument> args, PipedInputStream p) {

        PipedInputStream rss = new PipedInputStream();
        try (PipedOutputStream pout = new PipedOutputStream()) {
            try {
                pout.connect(rss);
            } catch (IOException e) {
                throw new Error("Could not connect pipes");
            }
            String fileName = "";
            if (!args.isEmpty()) {
                fileName = args.get(0).arg;
            }
            try (InputStream file = fileName.equals("") ? p : new FileInputStream(fileName);
                 BufferedReader fReader = new BufferedReader(new InputStreamReader(file))) {
                int count = 0;
                int countWords = 0;
                int countChars = 0;
                while (fReader.ready()) {
                    String str = fReader.readLine();
                    count++;
                    String[] res = str.split(" ");
                    int len = 0;
                    for (int i = 0; i < res.length; i++) {
                        if (!res[i].equals("")) {
                            len++;
                        }
                    }
                    countWords += len;
                    countChars += str.length() + 1;
                }
                pout.write((count + " " + countWords + " " + countChars + "\n").getBytes());
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("No such file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new Error("Pipes Error");
        }
        return rss;
    }
}
