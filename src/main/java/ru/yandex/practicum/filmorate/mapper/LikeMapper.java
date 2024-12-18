package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.LikeDTO;
import ru.yandex.practicum.filmorate.model.Like;

public class LikeMapper {
    public static LikeDTO mapToLikeDTO(Like like) {
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setUserId(like.getUserId());
        return likeDTO;
    }
}
