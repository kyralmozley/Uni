package textfarming.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    /**
     * Saves a User to the database.
     * If the telephone number already exists, data is overwritten.
     * @param user
     * @return the User object for further operations,
     * since the save operation may have changed the entity.
     */
    public User save(User user) {
        return userRepo.save(user);
    }

    public boolean contains(String tel) {
        return userRepo.existsById(tel);
    }

    /**
     * @param tel
     * @return a User object retrieved from the database if tel is registered.
     * Otherwise, null.
     */
    public User getUser(String tel) {
        Optional<User> option = userRepo.findById(tel);
        return (option.isPresent() ? option.get() : null);
    }

    public void deregister(String tel) {
        userRepo.deleteById(tel);
    }

    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteAllUsers() {
        userRepo.deleteAll();
    }
}
