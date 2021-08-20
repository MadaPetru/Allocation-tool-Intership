package ro.fortech.allocation;

import ro.fortech.allocation.employees.dto.EmployeeDto;
import ro.fortech.allocation.employees.model.Employee;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.model.Technology;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class EmployeeFactory {
    public static Employee createEmployee() throws ParseException {
        return Employee.builder()
                .uid("SomeUniqueEmployeeExternalID")
                .email("RandomEmployee@yahoo.com")
                .name("RandomEmployee")
                .technologies(getTechnologies())
                .active(true)
                .internalPosition("Employee")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
    }

    public static EmployeeDto createEmployeeDto() throws ParseException {
        return EmployeeDto.builder()
                .uid("SomeUniqueEmployeeExternalID")
                .email("RandomEmployee@yahoo.com")
                .name("RandomEmployee")
                .technologies(getTechnologiesDTOs())
                .active(true)
                .internalPosition("Employee")
                .startDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-10"))
                .endDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-06-10"))
                .build();
    }

    private static Set<TechnologyDto> getTechnologiesDTOs() {
        TechnologyDto techOne = TechnologyDto.builder().name("TechOne").externalId("TechnologyUID_One").build();

        return new HashSet<>(asList(techOne));
    }

    private static Set<Technology> getTechnologies() {
        Technology techOneDto = Technology.builder().name("TechOne").externalId("TechnologyUID_One").build();

        return new HashSet<>(asList(techOneDto));
    }
}
