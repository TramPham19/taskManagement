package com.example.taskManagement.controller;

import com.example.taskManagement.model.Users;
import com.example.taskManagement.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @Autowired
    UsersRepository usersRepository;

    public UsersController(UsersRepository Repository)
    {
        usersRepository = Repository;
    }
    @GetMapping(value = "/all")
    public List<Users> getAll() {
        return usersRepository.findAll();
    }

    @PostMapping(value = "/load")
    public List<Users> persist(@RequestBody final Users users) {
        usersRepository.save(users);
        return usersRepository.findAll();
    }

}
