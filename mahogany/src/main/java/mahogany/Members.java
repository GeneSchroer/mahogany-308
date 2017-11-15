package mahogany;

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
	
	//private Long nameId;
	private Long districtId;
	private Long partyId;
	private Long nameId;
	@ManyToOne
	@JoinColumn(name="nameId", updatable=false, insertable=false)
	private MemberNames memberName;
	
	@ManyToOne
	@JoinColumn(name="districtId", updatable=false, insertable=false)
	private Districts district;
	
	@ManyToOne
	@JoinColumn(name="partyId", updatable=false, insertable=false)
	private Parties party;
	
	protected Members() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getNameId() {
		return nameId;
	}
	
	public void setNameId(Long nameId) {
		this.nameId = nameId;
	}
	
	public Long getDistrictId() {
		return districtId;
	}
	
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	
	public Long getPartyId() {
		return partyId;
	}
	
	public void setPartyId(Long partyId) {
		this.partyId = partyId;
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
