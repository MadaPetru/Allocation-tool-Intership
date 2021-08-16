package ro.fortech.allocation.technology.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ro.fortech.allocation.technology.dto.TechnologyDto;
import ro.fortech.allocation.technology.exception.TechnologyAlreadyExistsInTheDatabase;
import ro.fortech.allocation.technology.exception.TechnologyNotFoundByExternalIdException;
import ro.fortech.allocation.technology.service.TechnologyService;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@EnableWebMvc
public class TechnologyControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    TechnologyService service;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void addTechnology_givenTechnology_expectTechnology() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        when(service.add(Mockito.any(TechnologyDto.class))).thenReturn(request);

        mockMvc.perform(post("/technologies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(request.getName()));
        verify(service).add(request);
    }

    @Test
    public void updateTechnology_givenTechnology_expectTechnology() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        when(service.update(Mockito.any(TechnologyDto.class), Mockito.any(String.class))).thenReturn(request);
        mockMvc.perform(put("/technologies/" + request.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.externalId").value(request.getExternalId()))
                .andExpect(jsonPath("$.name").value(request.getName()));
        verify(service).update(request, request.getExternalId());
    }

    @Test
    public void getAllTechnologies_givenTechnologies_expectTechnologies() throws Exception {
        TechnologyDto dto = makeTechnologyDto();
        Pageable pageRequest = PageRequest.of(0, 20);
        Page<TechnologyDto> page = new PageImpl<>(Collections.singletonList(dto));
        when(service.findAll(Mockito.any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/technologies"))
                .andExpect(status().isOk());
        verify(service).findAll(pageRequest);
    }

    @Test
    public void getTechnologyByExternalId_givenExternalId_expectTechnology() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        when(service.findByExternalId(Mockito.any(String.class))).thenReturn(request);
        mockMvc.perform(get("/technologies/" + request.getExternalId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.externalId").value(request.getExternalId()));
        verify(service).findByExternalId(request.getExternalId());
        assertEquals(request.getExternalId(), service.findByExternalId(request.getExternalId()).getExternalId());
    }

    @Test
    public void deleteTechnologyByExternalId_givenExternalId_expectTrue() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        when(service.deleteByExternalId(Mockito.any(String.class))).thenReturn(true);
        mockMvc.perform(delete("/technologies/" + request.getExternalId()))
                .andExpect(status().isOk());
        verify(service).deleteByExternalId(request.getExternalId());
    }

    @Test
    public void addTechnology_givenTechnology_expectConstraintViolationException() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        request.setName("");
        when(service.add(Mockito.any(TechnologyDto.class))).thenThrow(ConstraintViolationException.class);
        mockMvc.perform(post("/technologies")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addTechnology_givenTechnology_expectTechnologyAlreadyExistInTheDatabase() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        when(service.add(Mockito.any(TechnologyDto.class))).thenThrow(TechnologyAlreadyExistsInTheDatabase.class);
        mockMvc.perform(post("/technologies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTechnology_givenTechnology_expectConstraintViolationException() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        request.setName("");
        when(service.update(Mockito.any(TechnologyDto.class), Mockito.any(String.class))).thenThrow(ConstraintViolationException.class);
        mockMvc.perform(put("/technologies/" + request.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTechnology_givenTechnology_expectTechnologyAlreadyExistInTheDatabase() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        when(service.update(Mockito.any(TechnologyDto.class), Mockito.any(String.class))).thenThrow(TechnologyAlreadyExistsInTheDatabase.class);
        mockMvc.perform(put("/technologies/" + request.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTechnology_givenTechnology_expectTechnologyNotFoundByExternalIdException() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        when(service.update(Mockito.any(TechnologyDto.class), Mockito.any(String.class))).thenThrow(TechnologyNotFoundByExternalIdException.class);
        mockMvc.perform(put("/technologies" + request.getExternalId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteByExternalId_givenExternalId_expectTechnologyNotFoundByExternalIdException() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        when(service.deleteByExternalId(Mockito.any(String.class))).thenThrow(TechnologyNotFoundByExternalIdException.class);
        mockMvc.perform(delete("/technologies/" + request.getExternalId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getByExternalId_givenExternalId_expectTechnologyNotFoundByExternalIdException() throws Exception {
        TechnologyDto request = makeTechnologyDto();
        when(service.findByExternalId(Mockito.any(String.class))).thenThrow(TechnologyNotFoundByExternalIdException.class);
        mockMvc.perform(get("/technologies/" + request.getExternalId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTechnologyByName_givenName_expectTechnologiesByName() throws Exception {
        List<TechnologyDto> list = new LinkedList<>();
        TechnologyDto request = makeTechnologyDto();
        list.add(request);
        when(service.findTechnologiesByName(Mockito.any(String.class))).thenReturn(list);
        String name = "tech";
        mockMvc.perform(get("/technologies/find/")
                .param("name",name))
                .andExpect(status().isOk());
        verify(service).findTechnologiesByName(name);
    }

    private TechnologyDto makeTechnologyDto() {
        return TechnologyDto.builder()
                .name("tech")
                .externalId("externalId")
                .build();
    }
}