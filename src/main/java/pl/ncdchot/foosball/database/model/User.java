package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

	public User() {

	}

	public User(long id) {
		this.id = id;
	}

	@Id
	private long id;

	public long getUserId() {
		return id;
	}

	public void setUserId(long userId) {
		this.id = userId;
	}
}
