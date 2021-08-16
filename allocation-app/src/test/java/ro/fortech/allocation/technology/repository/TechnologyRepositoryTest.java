package ro.fortech.allocation.technology.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.fortech.allocation.technology.model.Technology;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TechnologyRepositoryTest {
    @Autowired
    TechnologyRepository repository;

    @Test
    public void findByExternalIdTest() {
        Technology tech = Technology.builder()
                .externalId("externalId")
                .id(1L)
                .name("tech")
                .build();
        repository.save(tech);
        Optional<Technology> expected = repository.findByExternalId(tech.getExternalId());
        assertThat(expected.get().getExternalId()).isEqualTo(tech.getExternalId());
    }

    @Test
    public void deleteByExternalIdTest() {
        Technology tech = Technology.builder()
                .externalId("externalId")
                .name("tech")
                .build();

        Technology expected = repository.save(tech);
    }

    @Test
    public void findByNameTest() {
        Technology tech = Technology.builder()
                .externalId("externalId")
                .id(1L)
                .name("tech")
                .build();

        repository.save(tech);
        Technology expected = repository.findByName(tech.getName()).get();

        assertThat(expected.getName()).isEqualTo(tech.getName());
        assertThat(expected.getExternalId()).isEqualTo(tech.getExternalId());
    }

    @Test
    public void findTechnologyByName(){
        Technology tech = Technology.builder()
                .externalId("externalId")
                .id(1L)
                .name("tech")
                .build();
        repository.save(tech);
        Technology response = repository.findByName(tech.getName()).get();
        assertEquals(tech.getName(),response.getName());
    }
}