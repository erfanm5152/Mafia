import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * The type Save file.
 * for save messages of the game in a text file.
 *
 * @author Erfanm5152
 * @version 0.1
 */
public class SaveFile {
    // save file of the game
    private File file;
    // write on save file
    private PrintWriter printWriter;

    /**
     * Instantiates a new Save file.
     *
     * @param fileName the file name
     */
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

    /**
     * Read string from file.
     *
     * @return saved messages.
     */
    public synchronized String read() {
        String temp = "";
        try {
            temp = "S-----O-----H\n" + new String(Files.readAllBytes(Path.of(file.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp + "E-----O-----H";
    }

    /**
     * Write message to the file.
     *
     * @param msg the msg
     */
    public synchronized void write(String msg) {
        printWriter.println(msg);
    }

    /**
     * Close stream.
     */
    public void close() {
        printWriter.close();
    }
}
