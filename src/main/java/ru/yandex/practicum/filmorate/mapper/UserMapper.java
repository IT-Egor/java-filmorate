package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.MergeUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDTO;
import ru.yandex.practicum.filmorate.model.User;

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
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setLogin(user.getLogin());
        userDTO.setName(user.getName());
        userDTO.setBirthday(user.getBirthday());
        return userDTO;
    }
}
