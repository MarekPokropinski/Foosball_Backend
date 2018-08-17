package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	private long id;

	public User() {

	}

	public User(long id) {
		this.id = id;
	}

	public long getUserId() {
		return id;
	}

	public void setUserId(long userId) {
		this.id = userId;
	}
}
