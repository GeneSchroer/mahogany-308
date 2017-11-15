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
@Table(name="Elections")
public class Elections {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="districtId")
	private Districts district;

	@OneToOne
	@JoinColumn(name="winnerId")
	private Members winner;
	
	protected Elections() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public Districts getDistrict() {
		return district;
	}
	
	public void setDistrict(Districts district) {
		this.district = district;
	}
	
	public Members getWinner() {
		return winner;
	}
	
	public void setWinner(Members winner) {
		this.winner = winner;
	}
}
