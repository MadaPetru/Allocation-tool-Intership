package ro.fortech.allocation.employees.helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import ro.fortech.allocation.employees.exception.CsvParseException;
import ro.fortech.allocation.employees.model.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CsvHelper {
    public static final String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {
        return file.getContentType().equals(TYPE);
    }

    public static List<Employee> csvToEmployees(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<Employee> employees = new ArrayList<>();

            List<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Employee employee = Employee.builder().uid(UUID.randomUUID().toString()).build();
                csvParser.getHeaderNames().stream().forEach(e -> {
                    try {
                        Field field = Arrays.stream(Employee.class.getDeclaredFields()).filter(f -> f.getName().equalsIgnoreCase(e)).collect(Collectors.toList()).get(0);
                        field.setAccessible(true);
                        if (e.equalsIgnoreCase("startDate") || e.equalsIgnoreCase("endDate")) {
                            field.set(employee, new SimpleDateFormat("yyyy-MM-dd").parse(csvRecord.get(e)));
                        } else if (e.equalsIgnoreCase("Active")) {
                            field.set(employee, Boolean.parseBoolean(csvRecord.get(e)));
                        } else if (e.equalsIgnoreCase("workingHours")) {
                            field.set(employee, Integer.parseInt(csvRecord.get(e)));
                        } else {
                            field.set(employee, csvRecord.get(e));
                        }
                    } catch (IllegalAccessException | ParseException ex) {
                        ex.printStackTrace();
                    }
                });
                employees.add(employee);
            }
            return employees;
        } catch (IOException e) {
            throw new CsvParseException(e.getMessage());
        }
    }
}
