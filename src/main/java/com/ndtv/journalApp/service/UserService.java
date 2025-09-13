package com.ndtv.journalApp.service;


import com.ndtv.journalApp.controller.JournalEntryController;
import com.ndtv.journalApp.entity.User;
import com.ndtv.journalApp.repository.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {


    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository userRepository;

    public User findByuserName(@NonNull String username) {

        return userRepository.findByUserName(username);
    }



    public void updateUser(User user)
    {

        userRepository.save(user);
    }

    public boolean registerUser(User user)
    {
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Error occur for {}",user.getUserName(),e);
            log.info("rrgr");
            log.warn("rrgr");
            log.debug("rrgr");
            log.trace("rrgr");
            return false;
        }

    }
    public void registerAdmin(User user)
    {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(List.of("USER","ADMIN"));
        userRepository.save(user);
    }



    public List<User> getAll()
    {
       return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id)
    {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id)
    {
        userRepository.deleteById(id);
    }

}
