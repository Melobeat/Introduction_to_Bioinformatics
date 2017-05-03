package Exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by kaikr on 30.04.2017.
 * Project Introduction to Bioinformatics
 */
public class SubstringSearch {

    public static void main(String[] args) {

        double time = System.currentTimeMillis();

        if (args.length != 2) {
            System.out.println("Geben Sie bitte \"pattern.fasta\" und \"sequence.fasta\" als Argumente an!");
            System.exit(1);
        }

        // Jeweils eine ArrayList für die Sequenzen und die Pattern, welche mit der Methode loadFasta gefüllt werden
        ArrayList<String> patterns = loadFasta(args[0]);
        ArrayList<String> sequences = loadFasta(args[1]);

        for (String sequence : sequences) {
            for (String pattern : patterns) {
                patternSearch(pattern, sequence);
            }
        }

        // Laufzeit in Millisekunden
        System.out.println("Time needed: " + (System.currentTimeMillis() - time) + "ms");

    }

    /**
     * Methode für das Laden einer .fasta in den Hauptspeicher
     * @param file .fasta Datei
     * @return ArrayList mit einzelenen Sequencen/Pattern
     */
    private static ArrayList<String> loadFasta(String file) {

        ArrayList<String> strings = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sCurrentLine;
            StringBuilder sb = new StringBuilder("");
            int i = -1;

            // Zusammenführen der Sequence zu einem String
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

    /**
     * Methode um eine Sequence nach einer Pattern zu durchsuchen
     * @param pattern Eine Pattern in Form eines Strings
     * @param sequence Eine Sequence in Form eines Strings
     */
    private static void patternSearch(String pattern, String sequence) {
        int occurences = 0;
        int[] positions = new int[10];

        // Erstellt eine HashMap mit den rechtesten Positionen von chars im Pattern für die bad-Character rule
        HashMap<Character, Integer> charPositions = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            charPositions.put(pattern.charAt(i), i);
        }

        // Substring Search mit Hilfe der bad-Character rule von Boyer-Moore
        int i = pattern.length() - 1;
        while (i < sequence.length()) {
            int j = pattern.length() - 1;
            while (j >= 0) {
                if (pattern.charAt(j) != sequence.charAt(i)) {
                    // Char in P und T stimmen nicht überein
                    if (charPositions.containsKey(sequence.charAt(i)) && charPositions.get(sequence.charAt(i)) < j) {
                        // Char in T existiert in P und ist links vom aktuellen Char
                        i += pattern.length() - charPositions.get(sequence.charAt(i)) - 1;
                        break;
                    } else {
                        // Char in T existiert nicht in P oder ist nicht links vom aktuellen Char
                        i += pattern.length();
                        break;
                    }
                } else {
                    // Char in P und T stimmen überein
                    if (j == 0) {
                        // kompletter P stimmt mit T überein
                        if (occurences < 10) {
                            positions[occurences] = i + 1;
                        }
                        occurences++;
                        i += pattern.length();
                        break;
                    }
                    i--;
                }
                j--;
            }
        }

        System.out.println(pattern + ": " + occurences);
        System.out.println(Arrays.toString(positions));
    }

}
