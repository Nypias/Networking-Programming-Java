package bankjpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity(name = "Owner")
public class Owner implements Serializable {
	private static final long serialVersionUID = 706795289816654474L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long accountId;

	@Column(name = "name", nullable = false)
	private String name;

	@Version
	@Column(name="OPTLOCK")
	private int versionNum;

	public Owner() {
		this(null);
	}

	public Owner(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}