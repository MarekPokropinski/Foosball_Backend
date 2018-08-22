package pl.ncdchot.foosball.modelDTO;

import java.util.Objects;

public class UserDTO {
	private String name;
	private String surname;
	private String nick;
	private String email;
	private String id;

	public UserDTO() {
	}

	public UserDTO(String name, String surname, String nick, String email, String id) {
		this.name = name;
		this.surname = surname;
		this.nick = nick;
		this.email = email;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDTO userDTO = (UserDTO) o;
		return Objects.equals(name, userDTO.name) && Objects.equals(surname, userDTO.surname)
				&& Objects.equals(nick, userDTO.nick) && Objects.equals(email, userDTO.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, surname, nick, email);
	}
}