package mahogany.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Members")
public class Members {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="nameId")
	private MemberNames memberName;
	
	@ManyToOne
	@JoinColumn(name="districtId")
	private Districts district;
	
	@ManyToOne
	@JoinColumn(name="partyId")
	private Parties party;
	
	public Members() {
		
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
	
	public Parties getParty() {
		return party;
	}

	public void setParty(Parties party) {
		this.party = party;
	}
	
	public MemberNames getMemberName() {
		return memberName;
	}

	public void setMemberName(MemberNames memberName) {
		this.memberName = memberName;
	}
}
