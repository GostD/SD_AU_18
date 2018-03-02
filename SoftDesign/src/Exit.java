import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

public class Exit extends Command {
    PipedInputStream execute(List<Argument> args, PipedInputStream pin) {System.exit(0); return null;}
}
