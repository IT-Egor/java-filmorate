package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.MergeUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserMapper {
    public static User mapMergeRequestToUser(MergeUserRequest mergeUser) {
        User user = new User();
        user.setId(mergeUser.getId());
        user.setEmail(mergeUser.getEmail());
        user.setLogin(mergeUser.getLogin());
        user.setName(mergeUser.getName());
        user.setBirthday(mergeUser.getBirthday());
        return user;
    }

    public static UserDTO mapUserToUserDTO(User user) {
        return createUserDTO(user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
    }

    public static UserDTO mapMergeRequestToUserDTO(MergeUserRequest mergeUser) {
        return createUserDTO(mergeUser.getId(),
                mergeUser.getEmail(),
                mergeUser.getLogin(),
                mergeUser.getName(),
                mergeUser.getBirthday());
    }

    private static UserDTO createUserDTO(Long id,
                                         String email,
                                         String login,
                                         String name,
                                         LocalDate birthday) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setEmail(email);
        userDTO.setLogin(login);
        userDTO.setName(name);
        userDTO.setBirthday(birthday);
        return userDTO;
    }
}
