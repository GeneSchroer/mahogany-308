package mahogany;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Votes")
public class Votes {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private Integer votes;
	private Float percentage;
	
	@ManyToOne
	@JoinColumn(name="partyId")
	private Parties party;
	
	@ManyToOne
	@JoinColumn(name="electionId")
	private Elections election;
	
	public Long getId() {
		return id;
	}
	public Parties getParty() {
		return party;
	}
	public void setParty(Parties party) {
		this.party= party;
	}
	public Integer getVotes() {
		return votes;
	}
	public void setVotes(Integer count) {
		this.votes = count;
	}
	public Float getPercentage() {
		return percentage;
	}
	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}
	
	public Elections getElection() {
		return election;
	}
	
	public void setElection(Elections election) {
		this.election = election;
	}
}
