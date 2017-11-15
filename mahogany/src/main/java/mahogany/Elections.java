package mahogany;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Elections")
public class Elections {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Long districtId;
	private Long winnerId;
	
	@ManyToOne
	@JoinColumn(name="districtId", updatable=false, insertable=false)
	private Districts district;
	
	protected Elections() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getDetailsId() {
		return districtId;
	}
	
	public void setDetailsId(Long detailsId) {
		this.districtId = detailsId;
	}
	
	public Long getWinnerId() {
		return winnerId;
	}
	
	public void setWinnerId(Long winnerId) {
		this.winnerId = winnerId;
	}
	
}
