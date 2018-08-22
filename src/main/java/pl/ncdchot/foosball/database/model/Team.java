package pl.ncdchot.foosball.database.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Arrays;
import java.util.List;

@Entity
public class Team extends BaseModel {
    @ManyToMany
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
