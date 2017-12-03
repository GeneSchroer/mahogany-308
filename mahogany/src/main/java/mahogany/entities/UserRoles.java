package mahogany.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UserRoles")
public class UserRoles {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Long getId() {
		return id;
	}
	
}
