package pl.ncdchot.foosball.exceptions;

public class UserNotExist extends Throwable {
	private static final long serialVersionUID = -8851281773946507170L;
	private long id;

	public UserNotExist(long userID) {
		this.id = userID;
	}

	@Override
	public String getMessage() {
		return String.format("UserDTO with id: %s not exit", id);
	}
}
