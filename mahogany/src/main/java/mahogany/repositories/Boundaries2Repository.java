package mahogany.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.MultiPolygon;

import mahogany.entities.Boundaries2;


@Repository
public interface Boundaries2Repository extends CrudRepository<Boundaries2, Long> {

	
	Boundaries2 findByCoordinates(MultiPolygon multiPolygon);
	
}


