package ru.practicum.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FakeUserRepositoryImpl implements UserRepository {
//    private static final List<User> FAKE_USERS = createManyFakeUsers(3);
    private static final List<User> FAKE_USERS = new ArrayList<>();
    private static Long nextId = 1L;

    @Override
    public List<User> findAll() {
        return FAKE_USERS;
    }

    @Override
    public User save(User user) {
        user.setId(nextId++);
        FAKE_USERS.add(user);
        return user;
    }

//    private static List<User> createManyFakeUsers(int count) {
//        List<User> fakeUsers = new ArrayList<>();
//        for (long id = 1; id <= count; id++) {
//            fakeUsers.add(createFakeUser(id));
//        }
//        return Collections.unmodifiableList(fakeUsers);
//    }

//    private static User createFakeUser(long id) {
//        User fakeUser = new User();
//        fakeUser.setId(id);
//        fakeUser.setEmail("mail" + id + "@example.com");
//        fakeUser.setName("Akakiy Akakievich #" + id);
//        return fakeUser;
//    }
}
