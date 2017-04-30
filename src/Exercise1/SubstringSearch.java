package Exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kaikr on 30.04.2017.
 * Project Introduction to Bioinformatics
 */
public class SubstringSearch {

    private static final String SEQUENCE_PATH = "C:\\Users\\kaikr\\Downloads\\sequence.fasta";
    private static final String PATTERN_PATH = "C:\\Users\\kaikr\\Downloads\\pattern.fasta";

    public static void main(String[] args) {

        double time = System.currentTimeMillis();
        ArrayList<String> patterns = loadFasta(PATTERN_PATH);
        ArrayList<String> sequences = loadFasta(SEQUENCE_PATH);
        System.out.println((System.currentTimeMillis() - time) * 0.001 + " s");
        System.out.println(sequences.get(0).length());

    }

    private static ArrayList<String> loadFasta(String file) {

        ArrayList<String> strings = new ArrayList<>();
        int i = -1;
        int j = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sCurrentLine;
            StringBuilder sb = new StringBuilder("");

            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println(j++);
                if (sCurrentLine.charAt(0) == '>') {
                    if (i < 0) {
                        i++;
                        sb = new StringBuilder("");
                    } else {
                        i++;
                        strings.add(sb.toString());
                        sb = new StringBuilder("");
                    }
                } else {
                    sb.append(sCurrentLine);
                }
            }
            strings.add(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings;
    }

}
