package com.morse_coders.aucdaisbackend.Users;

import com.morse_coders.aucdaisbackend.Email.EmailDetails;
import com.morse_coders.aucdaisbackend.Email.EmailSender;
import com.morse_coders.aucdaisbackend.Session.SessionToken;
import com.morse_coders.aucdaisbackend.Session.SessionTokenRepository;
import com.morse_coders.aucdaisbackend.Session.SessionTokenService;
import com.morse_coders.aucdaisbackend.Token.ConfirmationToken;
import com.morse_coders.aucdaisbackend.Token.ConfirmationTokenService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    private final ConfirmationTokenService confirmationTokenService;

    private final SessionTokenService sessionTokenService;

    private final SessionTokenRepository sessionTokenRepository;

    private final EmailSender emailSender;

    public UsersService(UsersRepository usersRepository, ConfirmationTokenService confirmationTokenService, SessionTokenService sessionTokenService, SessionTokenRepository sessionTokenRepository, EmailSender emailSender) {
        this.usersRepository = usersRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.sessionTokenService = sessionTokenService;
        this.emailSender = emailSender;
        this.sessionTokenRepository = sessionTokenRepository;
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
            //return new ResponseEntity<Long>(user.getId(), HttpStatus.BAD_REQUEST);
        }
        String pwHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(pwHash);
        usersRepository.save(user);


        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String confirmationLink = "http://localhost:3000/login";

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setFrom("morse@coders.com");
        emailDetails.setReceiver(user.getEmail());
        emailDetails.setSubject("Confirm your account");
        emailDetails.setBody(buildEmail(user.getFirstName(), confirmationLink));
        emailSender.send(emailDetails);
    }


    @Transactional
    public HttpEntity<String> confirmToken(String token){

        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenService.getToken(token);
        if (confirmationTokenOptional.isEmpty()) {
            throw new IllegalStateException("Token " + token + " not found");
        }
        ConfirmationToken confirmationToken = confirmationTokenOptional.get();
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Token " + token + " already confirmed");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token " + token + " expired");
        }
        confirmationTokenService.setConfirmedAt(token);

        Users user = confirmationToken.getUser();
        user.setConfirmed(true);


        //return "Token Confirmed";
        return new ResponseEntity<String>("Token Confirmed", HttpStatus.OK);

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
    public boolean updateUserEmail(Long id, String email){
        Users user = usersRepository.findById(id).orElseThrow(() -> new IllegalStateException("User with id " + id + " does not exist"));

        if (email!=null && email.length()>0 && !Objects.equals(user.getEmail(), email)) {
            Optional<Users> userOptional = usersRepository.findUsersByEmail(email);
            if (userOptional.isPresent()) {
                throw new IllegalStateException("User with email " + email + " already exists");
            }
            user.setEmail(email);
            return true;
        }
        else if (Objects.equals(user.getEmail(), email)){
            return true;
        }
        return false;
    }

    public HttpEntity<SessionToken> login(Users user) {
        String email = user.getEmail();
        String password = user.getPassword();

        Optional<Users> userOptional = usersRepository.findUsersByEmail(email);
        String token = "12345"; //dummy
        SessionToken sessionToken = new SessionToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), user);
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (checkPassword(password, user.getPassword())) {
                token = UUID.randomUUID().toString();
                Optional<SessionToken> sessionTokenOptional = sessionTokenService.getToken(user);
                if (sessionTokenOptional.isPresent()) {
                    sessionToken = sessionTokenOptional.get();
                    sessionToken.setToken(token);
                    sessionToken.setCreatedAt(LocalDateTime.now());
                    sessionToken.setExpiresAt(LocalDateTime.now().plusMinutes(60));
                    sessionTokenService.saveSessionToken(sessionToken);
                }
                else {
                    sessionToken = new SessionToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), user);
                    sessionTokenService.saveSessionToken(sessionToken);
                }
                return new ResponseEntity<SessionToken>(sessionToken, HttpStatus.OK);
            } else {
                return new ResponseEntity<SessionToken>(sessionToken, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<SessionToken>(sessionToken, HttpStatus.BAD_REQUEST);
    }

    public HttpEntity<Users> updateUser(Users user, String token) {
        Long uid = user.getId();
        Optional<Users> userOptional = usersRepository.findById(uid);
        if (userOptional.isPresent()) {
            Users olduser = userOptional.get();
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(olduser, LocalDateTime.now());
            if (getUserToken.isPresent()) {
                if (getUserToken.get().getToken().equals(token)) {
                    if (!user.getPassword().isEmpty()) {
                        String pwHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                        user.setPassword(pwHash);
                    } else user.setPassword(olduser.getPassword());

                    if (user.getFirstName().isEmpty()) {
                        user.setFirstName(olduser.getFirstName());
                    }

                    if (user.getLastName().isEmpty()) {
                        user.setLastName(olduser.getLastName());
                    }

                    if (user.getAddress().isEmpty()) {
                        user.setAddress(olduser.getAddress());
                    }

                    if (user.getPhoneNumber().isEmpty()) {
                        user.setPhoneNumber(olduser.getPhoneNumber());
                    }

                    if (user.getDateOfBirth() == null) {
                        user.setDateOfBirth(olduser.getDateOfBirth());
                    }

                    if (user.getImage().isEmpty()) {
                        user.setImage(olduser.getImage());
                    }

                    if (!user.getEmail().isEmpty()) {
                        if (updateUserEmail(uid, user.getEmail())) {
                            System.out.println("New email exists");
                            user.setConfirmed(olduser.getConfirmed());
                            usersRepository.save(user);
                            user.setPassword("");
                            return new ResponseEntity<>(user, HttpStatus.OK);
                        }
                        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST); //Email already exists
                    }
                    else {
                        // email update field is empty
                        System.out.println("New email does not exist");
                        user.setEmail(olduser.getEmail());
                        usersRepository.save(user);
                        user.setPassword("");
                        return new ResponseEntity<>(user, HttpStatus.OK);
                    }
                }
                else {
                    System.out.println("TOKENS ARE NOT EQUAL");
                    return new ResponseEntity<>(user, HttpStatus.FORBIDDEN);
                }
            }
        }
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }

    public HttpEntity<String> deletePhoto(Users user, String token) {
        System.out.println("user id: " + user.getId());
        Optional<Users> userOptional = usersRepository.findById(user.getId());
        if(userOptional.isPresent()) {
            user = userOptional.get();
            Optional<SessionToken> getUserToken = sessionTokenRepository.findByUserAndExpiresAt(user, LocalDateTime.now());
            if(getUserToken.isPresent()) {
                if(getUserToken.get().getToken().equals(token)) {
                    user.setImage("");
                    usersRepository.save(user);
                    return new ResponseEntity<>("good", HttpStatus.OK);
                }
                else {
                    System.out.println("Tokens are not equal");
                    return new ResponseEntity<>("forbidden", HttpStatus.FORBIDDEN);
                }
            }
        }
        return new ResponseEntity<>("bad", HttpStatus.BAD_REQUEST);
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public Users getUser(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    public HttpEntity<Users> updateAddress(Long user_id, Double latitude, Double longitude, String token) {
        Users users = usersRepository.findById(user_id).orElse(null);
        if (users!=null){
            Optional<SessionToken> sessionToken = sessionTokenRepository.findByUser(users);
            if (sessionToken.isPresent()){
                if (sessionToken.get().getToken().equals(token)){
                    users.setLatitude(latitude);
                    users.setLongitude(longitude);
                    usersRepository.save(users);
                    // https://api.mapbox.com/geocoding/v5/mapbox.places/-73.989,40.733.json?access_token=pk.eyJ1IjoicHAwMDYzeCIsImEiOiJjazhiNmZiMnkwNWw0M2RzMjJub2xhMXYwIn0.OssYldnMWVzFiQr0o24_iw"

                    String map_token = "pk.eyJ1IjoicHAwMDYzeCIsImEiOiJjazhiNmZiMnkwNWw0M2RzMjJub2xhMXYwIn0.OssYldnMWVzFiQr0o24_iw";
                    String _url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + longitude + "," + latitude + ".json?access_token=" + map_token;
                    try {
                        URL url = new URL(_url);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Accept", "application/json");
                        if (conn.getResponseCode() != 200) {
                            throw new RuntimeException("Failed : HTTP error code : "
                                    + conn.getResponseCode());
                        }
                        StringBuilder output = new StringBuilder();
                        Scanner scanner = new Scanner(url.openStream());
                        while (scanner.hasNext()) {
                            output.append(scanner.nextLine());
                        }
                        scanner.close();

                        JSONParser parser = new JSONParser();
                        JSONObject jsonObject = (JSONObject) parser.parse(output.toString());
                        JSONArray jsonArray = (JSONArray) jsonObject.get("features");

                        JSONObject newOb = (JSONObject) parser.parse(jsonArray.get(0).toString());
                        String address = newOb.get("place_name").toString();
                        if (address!=null){
                            users.setAddress(address);
                            usersRepository.save(users);
                        }



                    } catch (MalformedURLException e) {
                        throw new RuntimeException("Invalid URL");
                    } catch (IOException e) {
                        throw new RuntimeException("Error connecting to URL");

                    } catch (ParseException e) {
                        throw new RuntimeException("Error parsing JSON");
                    }


                    return new ResponseEntity<>(users, HttpStatus.OK);
                }
            }
        }


        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    public Map<String, Double> getUserLocation(Long id) {
        Users users = usersRepository.findById(id).orElse(null);
        if (users!=null){

            if (users.getLatitude() != null && users.getLongitude()!=null){
                Map<String, Double> map = new HashMap<>();
                map.put("lng", users.getLongitude());
                map.put("lat", users.getLatitude());
                return map;
            }

        }
        return null;
    }

    public HttpEntity<String> updateReceiveMessageEmail(Long user_id, Boolean check, String token) {
        Users users = usersRepository.findById(user_id).orElse(null);
        if (users!=null){
            Optional<SessionToken> sessionToken = sessionTokenRepository.findByUser(users);
            if (sessionToken.isPresent()){
                if (sessionToken.get().getToken().equals(token)){
                    users.setReceiveMessageEmail(check);
                    usersRepository.save(users);
                    return new ResponseEntity<>("Success", HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public HttpEntity<String> updateReceiveSavedNotificationEmail(Long user_id, Boolean check, String token) {
        Users users = usersRepository.findById(user_id).orElse(null);
        if (users!=null){
            Optional<SessionToken> sessionToken = sessionTokenRepository.findByUser(users);
            if (sessionToken.isPresent()){
                if (sessionToken.get().getToken().equals(token)){
                    users.setReceiveSavedNotificationEmail(check);
                    usersRepository.save(users);
                    return new ResponseEntity<>("Success", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
