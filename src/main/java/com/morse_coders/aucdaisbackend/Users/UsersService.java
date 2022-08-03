package com.morse_coders.aucdaisbackend.Users;

import com.morse_coders.aucdaisbackend.Email.EmailDetails;
import com.morse_coders.aucdaisbackend.Email.EmailSender;
import com.morse_coders.aucdaisbackend.Session.SessionToken;
import com.morse_coders.aucdaisbackend.Session.SessionTokenService;
import com.morse_coders.aucdaisbackend.Token.ConfirmationToken;
import com.morse_coders.aucdaisbackend.Token.ConfirmationTokenService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    private final ConfirmationTokenService confirmationTokenService;

    private final SessionTokenService sessionTokenService;

    private final EmailSender emailSender;

    public UsersService(UsersRepository usersRepository, ConfirmationTokenService confirmationTokenService, SessionTokenService sessionTokenService, EmailSender emailSender) {
        this.usersRepository = usersRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.sessionTokenService = sessionTokenService;
        this.emailSender = emailSender;
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

        String confirmationLink = "http://localhost:8080/users/confirm?token=" + token;

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setFrom("morse@coders.com");
        emailDetails.setReceiver(user.getEmail());
        emailDetails.setSubject("Confirm your account");
        emailDetails.setBody(buildEmail(user.getFirstName(), confirmationLink));
        emailSender.send(emailDetails);
    }


    @Transactional
    public HttpEntity<ConfirmationToken> confirmToken(String token){
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
        return new ResponseEntity<ConfirmationToken>(confirmationToken, HttpStatus.OK);
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
}
