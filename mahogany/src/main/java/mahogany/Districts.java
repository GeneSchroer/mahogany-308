package mahogany;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="Districts")
public class Districts {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	
	private Integer number;
	private Integer congress;
//	private Long stateId;
	
	@ManyToOne
	@JoinColumn(name="stateId")//, updatable=false, insertable=false)
	private StateNames stateName;
	
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
	
	//public Long getStateId() {
	//	return stateId;
	//}
	
	public void setStateId(Long stateId) {
	//	this.stateId = stateId;
	}
	
	public StateNames getStateName() {
		return stateName;
	}
	
	public void setStateName(StateNames stateName) {
		this.stateName = stateName;
	}
}