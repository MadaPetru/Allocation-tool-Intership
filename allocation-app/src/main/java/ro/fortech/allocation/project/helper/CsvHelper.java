package ro.fortech.allocation.project.helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import ro.fortech.allocation.employees.exception.CsvParseException;
import ro.fortech.allocation.project.model.Project;

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

    public static boolean hasCSVFormat(MultipartFile file){
        return file.getContentType().equals(TYPE);
    }
    public static List<Project> csvToProjects(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            List<Project> projects = new ArrayList<>();

            List<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Project project = Project.builder().externalId(UUID.randomUUID().toString()).build();
                csvParser.getHeaderNames().stream().forEach(e -> {
                    try {
                        Field field = Arrays.stream(Project.class.getDeclaredFields()).filter(f -> f.getName().equalsIgnoreCase(e)).collect(Collectors.toList()).get(0);
                        field.setAccessible(true);
                        if (e.equalsIgnoreCase("startDate") || e.equalsIgnoreCase("endDate")) {
                            field.set(project, new SimpleDateFormat("yyyy-MM-dd").parse(csvRecord.get(e)));
                        } else {
                            field.set(project, csvRecord.get(e));
                        }
                    } catch (IllegalAccessException | ParseException ex) {
                        ex.printStackTrace();
                    }
                });
                projects.add(project);
            }
            return projects;
        } catch (IOException e) {
            throw new CsvParseException(e.getMessage());
        }
    }
}
