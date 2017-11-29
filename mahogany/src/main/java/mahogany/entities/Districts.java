package mahogany.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="Districts")
public class Districts {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	private Integer districtNumber;
	private Integer year;
	
	@ManyToOne
	@JoinColumn(name="stateId")
	private StateNames stateName;
	
	@ManyToOne
	@JoinColumn(name="boundaryId")
	private Boundaries boundaries;
	
	@OneToOne(mappedBy="district")
	private Elections election;
	
	@OneToMany(mappedBy="district")
	private List<Members> members;
	
	public Districts() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public Integer getDistrictNumber() {
		return districtNumber;
	}
	
	public void setDistrictNumber(Integer districtNumber) {
		this.districtNumber = districtNumber;
	}
	
	public Integer getYear() {
		return year;
	}
	
	public void setYear(Integer year) {
		this.year = year;
	}
	
	public StateNames getStateName() {
		return stateName;
	}
	
	public void setStateName(StateNames stateName) {
		this.stateName = stateName;
	}
	
	public Boundaries getBoundaries() {
		return boundaries;
	}
	
	public void setBoundaries(Boundaries boundaries) {
		this.boundaries = boundaries;
	}
	
	public Elections getElection() {
		return election;
	}
	
	public void setElection(Elections election) {
		this.election = election;
	}
	
	public List<Members> getMembers(){
		return members;
	}
	
	public void setMembers(List<Members> members) {
		this.members = members;
	}
}