package codesquad.springcafe.user.dao;

import codesquad.springcafe.user.User;
import codesquad.springcafe.user.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserMemoryDao implements UserDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final Map<String, User> users = new LinkedHashMap<>();

    public void save(final User user) {
        if (users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("중복된 회원입니다.");
        }
        log.debug("회원가입한 유저 정보 : {}", user);
        users.put(user.getUserId(), user);
        log.debug("유저 수 : {}", users.size());
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public Optional<User> findUser(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public void updateUser(User user) {
        users.put(user.getUserId(), user);
    }
}