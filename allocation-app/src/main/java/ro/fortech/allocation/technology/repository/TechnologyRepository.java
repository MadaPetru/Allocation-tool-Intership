package ro.fortech.allocation.technology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.fortech.allocation.technology.model.Technology;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {
    Optional<Technology> findByExternalId(String externalId);
    boolean deleteByExternalId(String externalId);
    Optional<Technology> findByName(String name);
    @Query(value = "select * from technologies t where t.name LIKE %:name%", nativeQuery = true )
    List<Technology> findTechnologyByName(@Param("name") String name);
}