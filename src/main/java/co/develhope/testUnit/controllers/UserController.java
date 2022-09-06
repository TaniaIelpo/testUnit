package co.develhope.testUnit.controllers;


import co.develhope.testUnit.entities.User;
import co.develhope.testUnit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Tania Ielpo
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("")
    public @ResponseBody User create(@RequestBody User user){
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public @ResponseBody User getSingleUser(@PathVariable Long id){
        Optional<User> user=userRepository.findById(id);
        if (user.isPresent()) return user.get();
        else return null;
    }

    @GetMapping("")
    public @ResponseBody
    List<User> getAllUser(){
        return userRepository.findAll();
    }

    @PutMapping("/{id}")
    public @ResponseBody User update(@PathVariable Long id,
                              @RequestBody User user){
        user.setId(id);
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser (@PathVariable Long id){
        userRepository.deleteById(id);
    }
}
