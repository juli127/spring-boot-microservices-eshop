package com.gmail.kramarenko104.userservice.services;

import com.gmail.kramarenko104.userservice.models.User;
import com.gmail.kramarenko104.userservice.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final static String SALT = "34Ru9k";
    private UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public User createUser(User user){
        User criptUser = user;
        criptUser.setPassword(hashString(user.getPassword()));
        return userRepo.save(criptUser);
    }

    @Override
    public User getUser(long id){
        return (userRepo.findById(id).isPresent()?userRepo.findById(id).get():null);
    }

    @Override
    public User getUserByLogin(String login){
        return userRepo.findByLogin(login);
    }

    @Override
    public User update(User newUser) {
        return userRepo.updateUser(newUser.getLogin(),
                newUser.getPassword(), newUser.getName(),
                newUser.getAddress(), newUser.getComment(), newUser.getUser_id());
    }

    @Override
    public void deleteUser(long id){
        userRepo.deleteById(id);
    }

    @Override
    public List<User> getAllUsers(){
        return (List) userRepo.findAll();
    }

    @Override
    public Map<String, String> verifyUser(User user, String repassword) {
        Map<String, String> errors = new HashMap<>();
        String login = user.getLogin();
        String password = user.getPassword();

        if (repassword.length() > 0 && !password.equals(repassword)) {
            errors.put("", "Password and retyped one don't match!");
        }

        String patternString = "([0-9a-zA-Z._-]+@[0-9a-zA-Z_-]+[.]{1}[a-z]+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(login);
        if (!matcher.matches()) {
            errors.put("", "e-mail should have correct format");
        }
        return errors;
    }

    @Override
    public String hashString(String hash) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(StandardCharsets.UTF_8.encode(hash + SALT));
        return String.format("%032x", new BigInteger(md5.digest()));
    }

}
