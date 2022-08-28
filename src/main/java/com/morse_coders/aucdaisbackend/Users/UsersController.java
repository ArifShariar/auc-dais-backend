package com.morse_coders.aucdaisbackend.Users;

import com.morse_coders.aucdaisbackend.Session.SessionToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

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
    public HttpEntity<String> confirm(@RequestParam("token") String token) {
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

    @GetMapping(value = "/get/{id}/location")
    public Map<String, Double> getUserLocation(@PathVariable("id") Long id) {
        return usersService.getUserLocation(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        usersService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public void updateUserEmail(@PathVariable("id") Long id, @RequestParam(required = false) String email){
        usersService.updateUserEmail(id, email);
    }


    @PostMapping("/delete/photo/{token}")
    public HttpEntity<String> deletePhoto(@PathVariable("token") String token, @RequestBody Users user) {
        return usersService.deletePhoto(user, token);
    }

    @PutMapping("{user_id}/update/address/{latitude}/{longitude}/{token}")
    public HttpEntity<Users> updateAddress(@PathVariable("user_id") Long user_id,@PathVariable("latitude") Double latitude, @PathVariable("longitude") Double longitude, @PathVariable("token") String token) {
        return usersService.updateAddress( user_id,latitude, longitude, token);
    }

    @PutMapping("{user_id}/update/receive_message_email/{bool}/{token}")
    public HttpEntity<String> updateReceiveMessageEmail(@PathVariable("user_id") Long user_id, @PathVariable("bool") String bool, @PathVariable("token") String token) {
        Boolean check = Boolean.parseBoolean(bool);
        return usersService.updateReceiveMessageEmail(user_id, check, token);
    }

    @PutMapping("{user_id}/update/receive_saved_notification_email/{bool}/{token}")
    public HttpEntity<String> updateReceiveSavedNotificationEmail(@PathVariable("user_id") Long user_id, @PathVariable("bool") String bool, @PathVariable("token") String token) {
        Boolean check = Boolean.parseBoolean(bool);
        return usersService.updateReceiveSavedNotificationEmail(user_id, check, token);
    }
}
