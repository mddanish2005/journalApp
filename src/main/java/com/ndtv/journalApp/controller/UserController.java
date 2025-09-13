package com.ndtv.journalApp.controller;

import com.ndtv.journalApp.entity.JournalEntry;
import com.ndtv.journalApp.entity.User;
import com.ndtv.journalApp.service.JournalEntryService;
import com.ndtv.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.callback.PasswordCallback;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


//at least one mapping is necessary

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JournalEntryService journalEntryService;


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

         User userInDb = userService.findByuserName(auth.getName());
         if(userInDb!=null)
         {
             userInDb.setUserName(user.getUserName());
             userInDb.setPassword(user.getPassword());
             userService.registerUser(userInDb);
         }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userInDb = userService.findByuserName(auth.getName());
        List<JournalEntry> entries = userInDb.getJournalEntries();
        for (JournalEntry entry : entries) {
            journalEntryService.deleteById(entry.getId());
        }

        userService.deleteById(userInDb.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
