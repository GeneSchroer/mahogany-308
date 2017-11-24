package mahogany.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.MultiPolygon;

import mahogany.entities.Boundaries;


@Repository
public interface BoundariesRepository extends CrudRepository<Boundaries, Long> {

	
	Boundaries findByCoordinates(MultiPolygon multiPolygon);
	
}


