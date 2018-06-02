package ru.spbau.mit.cli;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpretator {
    protected static Map<String, Argument> variables = new HashMap<>();

    /**Считываем вход, пока не будет exit*/
    public static String Interpret(InputStream is) {
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
                                if (rs == null) {
                                    return "";
                                }
                            } catch (IllegalArgumentException | FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            if (pin != null) pin.close();
                            chose = false;

                        } else {
                            if (pin != null) pin.close();
                            try {
                                pin = cmd.command.execute(cmd.args, rs);
                                if (pin == null) {
                                    return "";
                                }
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
                BufferedReader fReader = new BufferedReader(new InputStreamReader(chose ? pin : rs));
                while (fReader.ready()) {
                    String s = fReader.readLine();
                    result += s + '\n';
                    System.out.println(s);
                }
                if (!result.equals(""))
                    result = result.substring(0, result.length()-1);
                fReader.close();
                if (pin != null) pin.close();
                rs.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
    }

    public static void main(String[] args) {
        while (true) {
            Interpret(System.in);
        }
    }
}