import java.io.*;
import java.util.List;

public class NoArgsComm extends Command {//Класс для команды без аргументов pwd
    NoArgsComm(String cmd) {
        this.cmd = cmd;
    }

    PipedInputStream execute(List<Argument> args, PipedInputStream pin) throws FileNotFoundException {//Метод возвращающий поток
        PipedOutputStream pout = new PipedOutputStream();
        PipedInputStream res = new PipedInputStream();
        try {
            pout.connect(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cmd.equals("pwd")) {
            try {
            pout.write((System.getProperty("user.dir")).getBytes());
            pout.write('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            pout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    String cmd;
}
