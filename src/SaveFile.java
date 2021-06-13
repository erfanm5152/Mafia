import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class SaveFile {
    private File file;
    private PrintWriter printWriter;

    public SaveFile(String fileName) {
        this.file = new File("saves" + File.separator + fileName + ".txt");
        try {
            file.createNewFile();
            OutputStream outputStream = new FileOutputStream(file, true);
            this.printWriter = new PrintWriter(outputStream, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized String read() {
        String temp = "";
        try {
            temp = "S-----O-----H\n" + new String(Files.readAllBytes(Path.of(file.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp + "E-----O-----H";
    }

    public synchronized void write(String msg) {
        printWriter.println(msg);
    }

    public void close() {
        printWriter.close();
    }
}
