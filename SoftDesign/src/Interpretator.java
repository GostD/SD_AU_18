import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpretator {
    public static Map<String, Argument> variables = new HashMap<>();
    public static String Interpret(InputStream is) {//Считываем вход, пока не будет exit
        String str = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try {
                str = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parser parser = new Parser(str);
            List<CommandWithArgs> cmds = parser.getCommands();
            PipedInputStream pin = null;
            PipedInputStream rs = new PipedInputStream();
            boolean chose = true;
            try {
                for (CommandWithArgs cmd : cmds) {
                        if (chose) {
                            try {
                                rs = cmd.command.execute(cmd.args, pin);
                            } catch (IllegalArgumentException | FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            if (pin != null) pin.close();
                            chose = false;

                        } else {
                            if (pin != null) pin.close();
                            try {
                                pin = cmd.command.execute(cmd.args, rs);
                            } catch (IllegalArgumentException | FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            chose = true;
                            rs.close();
                        }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String result = "";
            try {
                BufferedReader fReader;
                if (chose) {
                    fReader = new BufferedReader(new InputStreamReader(pin));

                } else {
                    fReader = new BufferedReader(new InputStreamReader(rs));
                }

                while (fReader.ready()) {
                    String s = fReader.readLine();
                    result += s;
                    System.out.println(s);
                }
                fReader.close();
                if (pin != null) pin.close();
                rs.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
//        }
    }

    public static void main(String[] args) {
        if (args.length != 0) {
            if (args[0].equals("--test")) {
                Test.tst();
            }
        } else {
            while (true) {
                Interpret(System.in);
            }
        }
    }
}