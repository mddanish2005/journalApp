package com.ndtv.journalApp.service;

 
import com.ndtv.journalApp.entity.JournalEntry;
import com.ndtv.journalApp.entity.User;
import com.ndtv.journalApp.repository.JournalRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component

public class JournalEntryService {

    @Autowired JournalRepository journalRepository;

    @Autowired UserService userService;



    @Transactional
    public void saveEntry(JournalEntry journalEntry , String userName)
    {
     try {
         journalEntry.setDate(LocalDateTime.now());
         JournalEntry saved = journalRepository.save(journalEntry);

         User user = userService.findByuserName(userName);
         user.getJournalEntries().add(saved);

         userService.updateUser(user);

     }catch (Exception e)
     {

         throw new RuntimeException();
     }

    }
    public void saveEntry(JournalEntry journalEntry)
    {

        journalEntry.setDate(LocalDateTime.now());
        journalRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll()
    {
       return journalRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id)
    {
        return journalRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id,String userName)
    {  boolean removed = false;
        try {
            User user = userService.findByuserName(userName);
             removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id) );
            if(removed) {
                userService.updateUser(user);
                journalRepository.deleteById(id);
            }

        } catch (Exception e) {
            throw new RuntimeException("An error occured while deleting "+e);
        }
        return removed;
    }
    public void deleteById(ObjectId id)
    {
        journalRepository.deleteById(id);
    }

}
