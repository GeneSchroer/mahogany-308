package mahogany.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;
//import org.hibernate.annotations.Type;
import com.vividsolutions.jts.geom.MultiPolygon;


@Entity
@Table(name="Boundaries2")
public class Boundaries2 {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	//@Column(name="coordinates", columnDefinition="Geometry")
	private MultiPolygon coordinates;

	//private String coordinates2;
	
	public Long getId() {
		return id;
	}
	@Type(type = "org.hibernate.spatial.GeometryType")
	@Column(columnDefinition="MultiPolygon")
	public MultiPolygon getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(MultiPolygon coordinates) {
		this.coordinates = coordinates;
	}
}
