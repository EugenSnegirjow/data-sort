
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    private static final String INTEGERS_FILENAME = "integers.txt";
    private static final String FLOATS_FILENAME = "floats.txt";
    private static final String STRINGS_FILENAME = "strings.txt";

    private static final List<Double> doubles = new ArrayList<>();
    private static final List<Long> longs = new ArrayList<>();
    private static final List<String> strings = new ArrayList<>();

    private static final List<String> inputFiles = new ArrayList<>();
    private static String outputDirectory = "";
    private static String prefix = "";
    private static boolean appendData = false;
    private static Statistic statisticsMode = Statistic.OFFSTAT;

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o")) {
                outputDirectory = args[++i] + "/";
            } else if (args[i].equals("-p")) {
                prefix = args[++i];
            } else if (args[i].equals("-a")) {
                appendData = true;
            } else if (args[i].equals("-s")) {
                statisticsMode = Statistic.SHORTSTAT;
            } else if (args[i].equals("-f")) {
                statisticsMode = Statistic.FULLSTAT;
            } else if (isTxtFile(args[i])) {
                inputFiles.add(args[i]);
            }
        }

        if (!inputFiles.isEmpty()) {
            for (String inputFile : inputFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                    while (reader.ready()) {
                        String line = reader.readLine();
                        if (isLong(line)) {
                            longs.add(Long.parseLong(line));
                        } else if (isFloat(line)) {
                            doubles.add(Double.parseDouble(line));
                        } else {
                            strings.add(line);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Не указаны файлы для чтения");
        }

        try {
            File outputPath = new File(outputDirectory);
            outputPath.mkdirs();
        } catch (Exception e) {
            System.out.println("При создании дирректории произошла ошибка");
        }

        if (!doubles.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + prefix + FLOATS_FILENAME, appendData))) {
                for (Double aDouble : doubles) {
                    writer.write(aDouble + "\n");
                }
            } catch (IOException e) {
                System.out.println("Ошибка при записи вещественных чисел");
                e.printStackTrace();
            }
        }

        if (!longs.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + prefix + INTEGERS_FILENAME, appendData))) {
                for (Long aLong : longs) {
                    writer.write(aLong + "\n");
                }
            } catch (IOException e) {
                System.out.println("Ошибка при записи целых чисел");
                e.printStackTrace();
            }
        }

        if (!strings.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + prefix + STRINGS_FILENAME, appendData))) {
                for (String string : strings) {
                    writer.write(string + "\n");
                }
            } catch (IOException e) {
                System.out.println("Ошибка при записи строк");
                e.printStackTrace();
            }
        }

        switch (statisticsMode) {
            case OFFSTAT -> {
            }
            case FULLSTAT -> printFullStat();
            case SHORTSTAT -> printShortStat();
        }

    }

    private static void printShortStat() {
        if (!doubles.isEmpty()) {
            System.out.println("Записано " + doubles.size() + " вещественных чисел в " + prefix + FLOATS_FILENAME);
        }
        if (!longs.isEmpty()) {
            System.out.println("Записано " + longs.size() + " целых чисел в " + prefix + INTEGERS_FILENAME);
        }
        if (!strings.isEmpty()) {
            System.out.println("Записано " + strings.size() + " строк в " + prefix + STRINGS_FILENAME);
        }
    }

    private static void printFullStat() {
        printShortStat();

        if (!doubles.isEmpty()) {

            double maxDouble = Double.MIN_VALUE;
            double minDouble = Double.MAX_VALUE;

            for (Double aDouble : doubles) {
                if (aDouble > maxDouble) {
                    maxDouble = aDouble;
                }
                if (aDouble < minDouble) {
                    minDouble = aDouble;
                }
            }
            System.out.println("Минимальное вещественное число: " + minDouble);
            System.out.println("Максимальное вещественное число: " + maxDouble);
        }

        if (!longs.isEmpty()) {

            long maxLong = Long.MIN_VALUE;
            long minLong = Long.MAX_VALUE;

            for (Long aLong : longs) {
                if (aLong > maxLong) {
                    maxLong = aLong;
                }
                if (aLong < minLong) {
                    minLong = aLong;
                }
            }
            System.out.println("Минимальное целое число: " + minLong);
            System.out.println("Максимальное целое число: " + maxLong);
        }

        if (!strings.isEmpty()) {

            int maxString = 0;
            int minString = Integer.MAX_VALUE;

            for (String string : strings) {
                if (string.length() > maxString) {
                    maxString = string.length();
                }
                if (string.length() < minString) {
                    minString = string.length();
                }
            }
            System.out.println("Длина самой короткой строки: " + minString);
            System.out.println("Длина самой длинной строки: " + maxString);
        }

    }

    private static boolean isFloat(String data) {
        try {
            Float.parseFloat(data);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isLong(String data) {
        try {
            Long.parseLong(data);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isTxtFile(String arg) {
        Pattern txtPattern = Pattern.compile("^\\w+\\.txt$");
        Matcher matcher = txtPattern.matcher(arg);
        return matcher.matches();
    }

    enum Statistic {
        FULLSTAT,
        SHORTSTAT,
        OFFSTAT
    }

}
