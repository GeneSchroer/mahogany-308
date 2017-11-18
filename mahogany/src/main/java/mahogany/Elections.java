package mahogany;

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
	

	@OneToMany(mappedBy="election")
	private List<Votes> votes;
	
	@ManyToOne
	@JoinColumn(name="partyId")
	private Parties party;
	
	

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
	public List<Votes> getVotes(){
		return votes;
	}
	public void setVotes(List<Votes> votes) {
		this.votes = votes;
	}
	
	public Parties getParty() {
		return party;
	}

	public void setParty(Parties party) {
		this.party = party;
	}
	
}
