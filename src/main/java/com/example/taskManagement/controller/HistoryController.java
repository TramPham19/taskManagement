package com.example.taskManagement.controller;

import com.example.taskManagement.model.HistoryTask;
import com.example.taskManagement.model.Task;
import com.example.taskManagement.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/history")
public class HistoryController {

    @Autowired
    HistoryRepository historyRepository;

    public HistoryController(HistoryRepository Repository)
    {
        historyRepository = Repository;
    }
    @GetMapping(value = "/all")
    public List<HistoryTask> getAll() {
        return historyRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public List<HistoryTask> getHistory(@PathVariable int id) {
        return historyRepository.getListHistoryOfTask(id);
    }


//    @PostMapping(value = "/load")
    public List<HistoryTask> persist( final HistoryTask history) {
        historyRepository.save(history);
        return historyRepository.findAll();
    }

}
