import java.io.*;
import java.util.List;

public class FileComm extends Command {//Класс для команд работающих с файлами cat и wc
    FileComm(String cmd) {
        this.cmd = cmd;
    }
    PipedInputStream execute(List<Argument> args, PipedInputStream p) throws FileNotFoundException {//Метод выполняющий команду для входного потока pin, возвращает поток
        PipedOutputStream pout = new PipedOutputStream();
        PipedInputStream rss = new PipedInputStream();
        try {
            pout.connect(rss);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cmd.equals("cat")) {
            if (!args.isEmpty()) {
                String fileName = args.get(0).arg;
                try {
                    FileInputStream file = new FileInputStream("src/" + fileName); //Посмотреть пути
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
        } else if (cmd.equals("wc")) {
            if (!args.isEmpty()) {
                String fileName = args.get(0).arg;
                try {
                    InputStream file = new FileInputStream(fileName);
                    BufferedReader fReader = new BufferedReader(new InputStreamReader(file));
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
                    pout.write((count + " " + countWords + " " + countChars).getBytes());
                    pout.write('\n');
                    pout.flush();
                    file.close();
                    fReader.close();
                } catch (FileNotFoundException e) {
                    throw new FileNotFoundException("No such file");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (p != null) {
                BufferedReader fReader = new BufferedReader(new InputStreamReader(p));
                int count = 0;
                int countWords = 0;
                int countChars = 0;
                try {
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

                    pout.write((count + " " + countWords + " " + countChars).getBytes());
                    pout.write('\n');
                    pout.flush();
                    p.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            pout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rss;
    }
    private String cmd;
}
