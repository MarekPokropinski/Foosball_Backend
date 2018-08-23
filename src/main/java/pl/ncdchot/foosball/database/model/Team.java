package pl.ncdchot.foosball.database.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

@Entity
public class Team extends BaseModel {
	@ManyToMany(fetch = FetchType.EAGER)
	private List<User> users;

	public Team() {
	}

	public Team(List<User> users) {
		this.users = users;
	}

	public Team(User user) {
		this.users = Arrays.asList(user);
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
