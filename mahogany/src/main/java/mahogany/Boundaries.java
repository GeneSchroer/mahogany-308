package mahogany;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Boundaries")
public class Boundaries {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="coordinates")
	private String coordinatesString;
	
	public Long getId() {
		return id;
	}
	
	public String getCoordinatesString() {
		return coordinatesString;
	}
	
	public void setCoordinatesString(String coordinatesString) {
		this.coordinatesString = coordinatesString;
	}
}
