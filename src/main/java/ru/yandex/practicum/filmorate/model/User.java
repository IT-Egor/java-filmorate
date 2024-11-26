package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    private @NonNull long id;
    private @NonNull String email;
    private @NonNull String login;
    private @NonNull String name;
    private @NonNull LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
    @Getter
    private Map<Long, Boolean> friendshipConfirmation = new HashMap<>();

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void removeFriend(Long friendId) {
        friends.remove(friendId);
    }
}
