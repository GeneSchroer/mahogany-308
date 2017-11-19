package mahogany.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mahogany.entities.Boundaries;


@Repository
public interface BoundariesRepository extends CrudRepository<Boundaries, Long> {

	Boundaries findByCoordinatesString(String coordinatesString);
}

