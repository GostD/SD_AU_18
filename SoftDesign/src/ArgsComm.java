import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

public class ArgsComm extends Command {//Класс для echo
    ArgsComm(String cmd) {
        this.cmd = cmd;
    }
    PipedInputStream execute(List<Argument> args, PipedInputStream pin) throws FileNotFoundException {//Метод выполняющий команду для входного потока pin, возвращает поток
        PipedOutputStream pout = new PipedOutputStream();
        PipedInputStream res = new PipedInputStream();
        try {
            pout.connect(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cmd.equals("echo")) {
            boolean start = true;
            for (Argument arg : args) {
                try {
                    if (!start) pout.write(' ');
                    start = false;
                    pout.write(arg.getValue().getBytes());
//                    pout.write(' ');
                    pout.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                pout.write('\n');
                pout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (cmd.equals("=")) {
            Interpretator.variables.put(args.get(0).arg, args.get(1));
        }
        try {
            pout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    private String cmd;
}
