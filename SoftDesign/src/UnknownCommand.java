import java.io.*;
import java.util.List;

public class UnknownCommand extends Command {//Класс для неизвестных команд
    String cmd;
    UnknownCommand(String cmd) {
        this.cmd = cmd;
    }
    PipedInputStream execute(List<Argument> args, PipedInputStream  pin) throws FileNotFoundException {
        String cm = cmd;
        for (Argument arg : args) {
            cm += " " + arg.arg;
        }
        ProcessBuilder builder = new ProcessBuilder(cm);
        Process process = null;
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
        InputStream rs = process.getInputStream();
        BufferedReader res = new BufferedReader(new InputStreamReader(rs));
        PipedInputStream p = new PipedInputStream();
        PipedOutputStream pout = new PipedOutputStream();
        try {
            pout.connect(p);
            while (res.ready()) {
                pout.write(res.readLine().getBytes());
                pout.write('\n');
            }
            pout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }
}
