package Exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kaikr on 30.04.2017.
 * Project Introduction to Bioinformatics
 */
public class SubstringSearch {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Geben Sie bitte \"pattern.fasta\" und \"sequence.fasta\" als Argumente an!");
            System.exit(1);
        }

        ArrayList<String> patterns = loadFasta(args[0]);
        ArrayList<String> sequences = loadFasta(args[1]);

        for (String pattern : patterns) {
            for (String sequence : sequences) {
                patternSearch(pattern, sequence);
            }
        }

    }

    private static ArrayList<String> loadFasta(String file) {

        ArrayList<String> strings = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sCurrentLine;
            StringBuilder sb = new StringBuilder("");
            int i = -1;

            while ((sCurrentLine = br.readLine()) != null) {
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

    private static void patternSearch(String pattern, String sequence) {
        int occurences = 0;
        int[] positions = new int[10];
        for (int i = 0; i < sequence.length() - pattern.length(); i++) {
            int k = i;
            for (int j = 0; j < pattern.length(); j++) {
                if (pattern.charAt(j) != sequence.charAt(k)) {
                    break;
                } else {
                    if (j == pattern.length() - 1) {
                        if (occurences < 10) {
                            positions[occurences] = i + 1;
                        }
                        occurences++;
                        break;
                    }
                    k++;
                }
            }
        }
        System.out.println(pattern + ": " + occurences);
        System.out.println(Arrays.toString(positions));
    }

}
