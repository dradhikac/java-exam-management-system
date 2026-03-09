package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {

    public static List<String[]> readCSV(String path) {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }

        } catch (Exception e) {
            System.out.println("CSV Read Error: " + e.getMessage());
        }

        return rows;
    }
}
