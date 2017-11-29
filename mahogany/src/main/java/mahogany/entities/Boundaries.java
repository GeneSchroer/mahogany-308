package mahogany.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

//import org.hibernate.annotations.Type;
import com.vividsolutions.jts.geom.MultiPolygon;


@Entity
@Table(name="Boundaries")
public class Boundaries {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private MultiPolygon coordinates; // spatial coordinates in jts format
	
	public Long getId() {
		return id;
	}
	
	@Type(type = "org.hibernate.spatial.GeometryType") //tells hibernate how to treat this variable
	@Column(columnDefinition="MultiPolygon")
	public MultiPolygon getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(MultiPolygon coordinates) {
		this.coordinates = coordinates;
	}
}
