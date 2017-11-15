package mahogany;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="Districts")
public class Districts {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	private Integer number;
	private Integer congress;
	
	@ManyToOne
	@JoinColumn(name="stateId")//, updatable=false, insertable=false)
	private StateNames stateName;
	
	@ManyToOne
	@JoinColumn(name="boundaryId")
	private Boundaries boundaries;
	
	@OneToOne(mappedBy="district")
	private Elections election;
	
	protected Districts() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public Integer getCongress() {
		return congress;
	}
	
	public void setCongress(Integer congress) {
		this.congress = congress;
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
}