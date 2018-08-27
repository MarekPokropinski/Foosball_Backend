package pl.ncdchot.foosball.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User with this nick name not exist")
public class UserByNickNoExistException extends Exception {
    private String nickName;

    public UserByNickNoExistException(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String getMessage() {
        return String.format("UserDTO with nickName: %s not exit", nickName);
    }
}
