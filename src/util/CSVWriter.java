package util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {

    public static void writeCSV(String path, List<String[]> rows) {

        try (FileWriter fw = new FileWriter(path)) {

            for (String[] row : rows) {
                fw.write(String.join(",", row));
                fw.write("\n");
            }

            System.out.println("CSV exported successfully: " + path);

        } catch (IOException e) {
            System.out.println("CSV Write Error: " + e.getMessage());
        }
    }
}
