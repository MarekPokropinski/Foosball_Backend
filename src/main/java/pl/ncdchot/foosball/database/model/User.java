package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;

@Entity
public class User extends BaseModel {

    private long externalID;

    public User() {
    }

    public User(long externalID) {
        this.externalID = externalID;
    }

    public long getExternalID() {
        return externalID;
    }

    public void setExternalID(long externalID) {
        this.externalID = externalID;
    }
}
