import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public final class Main {

    private static <T> List<T> csvToObj(File file, char separator, Class<T> type, String... classFields) throws IOException {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(separator)
                .build();
        try (CSVReader readerBuilder = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))
                .withCSVParser(parser)
                .build()) {
            ColumnPositionMappingStrategy<T> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(type);
            strategy.setColumnMapping(classFields);
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(readerBuilder)
                    .withMappingStrategy(strategy)
                    .build();
            return csvToBean.parse();
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Укажите путь к файлу *.csv, включая диск: ");
                File file = new File(scanner.nextLine());
                if (file.exists() && file.isFile() && file.getName().endsWith(".csv")) {
                    try {
                        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
                        List<Employee> employees = csvToObj(file, ';', Employee.class, columnMapping);
                        System.out.println(employees);

                        return;
                    } catch (FileNotFoundException e) {
                        System.err.println("Проверьте правильность указания пути и имени файла.");
                    } catch (IOException e) {
                        System.err.println("Произошла ошибка при попытке чтения указанного файла.");
                    }
                } else {
                    System.err.println("Проверьте правильность указания пути и имени файла.");
                }
            }
        }
    }
}