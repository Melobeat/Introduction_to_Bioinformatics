package Exercise2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class LocalAlignment {
    private final static int GAP_COST = -8;
    private static int length, matches, replacements, deletions;

    private static void fillDotplot(String[] pairs, int[][] matrix) {
        int dotplot[][] = new int[pairs[0].length() + 1][pairs[1].length() + 1];
        char dotway[][] = new char[pairs[0].length() + 1][pairs[1].length() + 1];

        //1.Zeile auf Null setzen
        for (int i = 0; i < pairs[0].length() + 1; i++) {
            dotplot[i][0] = 0;
            dotway[i][0] = 'x';
        }

        //1.Spalte auf Null setzen
        for (int i = 0; i < pairs[1].length() + 1; i++) {
            dotplot[0][i] = 0;
            dotway[0][i] = 'x';
        }

        // Berechne die Matrixwerte
        for (int i = 1; i < pairs[0].length() + 1; i++) {
            for (int j = 1; j < pairs[1].length() + 1; j++) {
                int l = dotplot[i - 1][j] + GAP_COST;
                int u = dotplot[i][j - 1] + GAP_COST;
                int d = dotplot[i - 1][j - 1] + alignCost(pairs, matrix, i, j);
                int x = 0;
                //Finde das Maximum
                if (x >= d && x >= u && x >= l) {
                    dotplot[i][j] = x;
                    dotway[i][j] = 'x';
                } else if (l >= d && l >= u && l >= x) {
                    dotplot[i][j] = l;
                    dotway[i][j] = 'l';
                } else if (u >= d && u >= l && u >= x) {
                    dotplot[i][j] = u;
                    dotway[i][j] = 'u';
                } else {
                    dotplot[i][j] = d;
                    dotway[i][j] = 'd';
                }
            }
        }

        findeMax(dotplot, dotway, pairs);
    }

    private static void findeMax(int[][] dotplot, char[][] dotway, String[] pairs) {
        int max = 0;
        int maxi = 0;
        int maxj = 0;
        for (int i = 1; i <= pairs[0].length(); i++) {
            for (int j = 1; j <= pairs[1].length(); j++) {
                if (dotplot[i][j] > max) {
                    max = dotplot[i][j];
                    maxi = i;
                    maxj = j;
                }
            }
        }
        System.out.println("Best Alignment:");
        printAlignmentPair1(dotplot, dotway, pairs[0], maxi, maxj);
        printAlignmentBetween(dotplot, dotway, pairs, maxi, maxj);
        printAlignmentPair2(dotplot, dotway, pairs[1], maxi, maxj);
        System.out.println();
        System.out.println("Score: " + max);
        System.out.println("Length: " + length);
        float similarity = matches * 100 / length;
        System.out.println("Matches: " + matches + " (similarity: " + similarity + "%)");
        System.out.println("Replacements: " + replacements);
        System.out.println("Deletions/Insertions: " + deletions);

        System.out.println();
        System.exit(1);
    }

    private static void printAlignmentPair1(int[][] dotplot, char[][] dotway, String pair, int i, int j) {
        StringBuilder dna1 = new StringBuilder("");
        while (dotplot[i][j] != 0) {
            switch (dotway[i][j]) {
                case 'l':
                    i = i - 1;
                    dna1.append(pair.charAt(i));
                    break;
                case 'u':
                    j = j - 1;
                    dna1.append("_");
                    break;
                case 'd':
                    i = i - 1;
                    j = j - 1;
                    dna1.append(pair.charAt(i));
                    break;
                default:
                    break;
            }
        }
        System.out.println(dna1.reverse().toString());
    }

    private static void printAlignmentPair2(int[][] dotplot, char[][] dotway, String pair, int i, int j) {
        StringBuilder dna2 = new StringBuilder("");
        while (dotplot[i][j] != 0) {
            switch (dotway[i][j]) {
                case 'l':
                    i = i - 1;
                    dna2.append("_");
                    break;
                case 'u':
                    j = j - 1;
                    dna2.append(pair.charAt(j));
                    break;
                case 'd':
                    i = i - 1;
                    j = j - 1;
                    dna2.append(pair.charAt(j));
                    break;
                default:
                    break;
            }
        }
        System.out.println(dna2.reverse().toString());
    }

    private static void printAlignmentBetween(int[][] dotplot, char[][] dotway, String[] pairs, int i, int j) {
        StringBuilder between = new StringBuilder("");
        length = 0;
        matches = 0;
        replacements = 0;
        deletions = 0;

        while (dotplot[i][j] != 0) {
            length++;
            switch (dotway[i][j]) {
                case 'l':
                    i = i - 1;
                    between.append(" ");
                    deletions++;
                    break;
                case 'u':
                    j = j - 1;
                    between.append(" ");
                    deletions++;
                    break;
                case 'd':
                    i = i - 1;
                    j = j - 1;
                    if (pairs[0].charAt(i) == pairs[1].charAt(j)) {
                        between.append("|");
                        matches++;
                    } else {
                        between.append(".");
                        replacements++;
                    }
                    break;
                default:
                    break;
            }
        }

        System.out.println(between.reverse().toString());
    }

    private static int alignCost(String[] pairs, int[][] matrix, int i, int j) {
        char base1 = pairs[0].charAt(i - 1);
        int k = baseNumber(base1);
        char base2 = pairs[1].charAt(j - 1);
        int l = baseNumber(base2);
        return matrix[k][l];
    }

    private static int baseNumber(char base) {
        switch (base) {
            case 'A':
                return 0;
            case 'T':
                return 1;
            case 'G':
                return 2;
            case 'C':
                return 3;
            default:
                return 4;
        }
    }


    private static String[] loadFasta(String file) {
        String[] pairs = new String[2];
        String sCurrentLine;
        StringBuilder sb = new StringBuilder("");
        int i = -1;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.charAt(0) == '>') {
                    if (i < 0) {
                        i++;
                        sb = new StringBuilder("");
                    } else {
                        pairs[i] = sb.toString();
                        i++;
                        sb = new StringBuilder("");
                    }
                } else {
                    sb.append(sCurrentLine);
                }
            }
            pairs[i] = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pairs;
    }

    private static int[][] loadMatrix(String file) {
        int[][] matrix = new int[5][5];

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sCurrentLine;
            String[] currentTokens;
            boolean firstLine = true;
            int j = 0;

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.charAt(0) != '#' && !firstLine) {
                    currentTokens = sCurrentLine.split("\\s+");

                    for (int i = 1; i < currentTokens.length; i++) {
                        if (currentTokens[i].charAt(0) == '-') {
                            matrix[j][i - 1] = Character.getNumericValue(currentTokens[i].charAt(1)) * -1;
                        } else {
                            matrix[j][i - 1] = Character.getNumericValue(currentTokens[i].charAt(0));
                        }
                    }
                    j++;
                } else if (sCurrentLine.charAt(0) != '#' && firstLine) {
                    firstLine = false;
                }
            }

            for (int i = 0; i < 5; i++) {
                matrix[i][4] = GAP_COST;
                matrix[4][i] = GAP_COST;
            }

        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        System.out.println(Arrays.deepToString(matrix));
        return matrix;
    }

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Geben Sie bitte \"pair.fasta\" und \"matrix.txt\" als Argumente an!");
            System.exit(1);
        }

        String[] pairs = loadFasta(args[0]);
        int[][] matrix = loadMatrix(args[1]);
        fillDotplot(pairs, matrix);
    }
}
