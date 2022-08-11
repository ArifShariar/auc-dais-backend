package com.morse_coders.aucdaisbackend.Users;

import com.morse_coders.aucdaisbackend.Session.SessionToken;
import com.morse_coders.aucdaisbackend.Token.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<Users> getUsers() {
        return usersService.getAllUsers();
    }

    @PostMapping(value = "/signup")
    public void createUser(@RequestBody Users user) {
        usersService.createUser(user);
    }

    @PostMapping(value = "/login")
    public HttpEntity<SessionToken> login(@RequestBody Users user) {
        return usersService.login(user);
    }

    @GetMapping(value = "/confirm")
    public HttpEntity<ConfirmationToken> confirm(@RequestParam("token") String token) {
        return usersService.confirmToken(token);
    }

    @PostMapping(value = "/update/{token}")
    public HttpEntity<Users> updateUser(@PathVariable("token") String token, @RequestBody Users user) {
        return usersService.updateUser(user, token);
    }

    @GetMapping(value = "/get/{id}")
    public Users getUser(@PathVariable("id") Long id) {
        return usersService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        usersService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public void updateUserEmail(@PathVariable("id") Long id, @RequestParam(required = false) String email){
        usersService.updateUserEmail(id, email);
    }
}
