import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.nio.file.Paths;

public class FDir {
    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        File folder = new File(currentDir);
        File[] ls = folder.listFiles();
        for (File file : ls) {
            System.out.println(file.getName());
        }
    }
}
