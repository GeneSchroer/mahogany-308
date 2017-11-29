package mahogany.entities;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Elections")
public class Elections {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer totalVotes;
	
	@OneToOne
	@JoinColumn(name="districtId")
	private Districts district;

	@OneToOne
	@JoinColumn(name="winnerId")
	private Members winner;

	@OneToMany(mappedBy="election", fetch = FetchType.EAGER)
	@MapKeyColumn(name="partyId")
	private Map<Long, Votes> votes;
	
	@ManyToOne
	@JoinColumn(name="partyId")
	private Parties party;
	
	public Elections() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public Integer getTotalVotes() {
		return totalVotes;
	}
	
	public void setTotalVotes(Integer totalVotes) {
		this.totalVotes = totalVotes;
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
	
	public Map<Long, Votes> getVotes(){
		return votes;
	}
	
	public void setVotes(Map<Long, Votes> votes) {
		this.votes = votes;
	}
	
	public Parties getParty() {
		return party;
	}

	public void setParty(Parties party) {
		this.party = party;
	}
	
}
