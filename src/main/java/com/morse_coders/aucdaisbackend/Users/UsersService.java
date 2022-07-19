package com.morse_coders.aucdaisbackend.Users;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users getUserById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    public void createUser(Users user){
        Optional<Users> userOptional = usersRepository.findUsersByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("User with email " + user.getEmail() + " already exists");
        }
        String pwHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(pwHash);
        usersRepository.save(user);
    }

    public Boolean checkPassword(String password, String pwHash) {
        return BCrypt.checkpw(password, pwHash);
    }

    public void deleteUser(Long id){
        boolean isUserExists = usersRepository.existsById(id);

        if (!isUserExists) {
            throw new IllegalStateException("User with id " + id + " does not exist");
        }

        usersRepository.deleteById(id);
    }

    @Transactional
    public void  updateUserEmail(Long id, String email){
        Users user = usersRepository.findById(id).orElseThrow(() -> new IllegalStateException("User with id " + id + " does not exist"));

        if (email!=null && email.length()>0 && !Objects.equals(user.getEmail(), email)) {
            Optional<Users> userOptional = usersRepository.findUsersByEmail(email);
            if (userOptional.isPresent()) {
                throw new IllegalStateException("User with email " + email + " already exists");
            }
            user.setEmail(email);
        }
    }

    public Users login(String email, String password) {
        Optional<Users> userOptional = usersRepository.findUsersByEmail(email);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if (checkPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
}
