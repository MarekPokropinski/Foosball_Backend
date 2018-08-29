package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class User extends BaseModel {
	private long externalID;

	@OneToOne(optional = false)
	private UserHistory userHistory;

	public User() {
	}

	public User(long externalID, UserHistory userHistory) {
		this.externalID = externalID;
		this.userHistory = userHistory;
	}

	public long getExternalID() {
		return externalID;
	}

	public void setExternalID(long externalID) {
		this.externalID = externalID;
	}

	public UserHistory getUserHistory() {
		return userHistory;
	}

	public void setUserHistory(UserHistory userHistory) {
		this.userHistory = userHistory;
	}
}
