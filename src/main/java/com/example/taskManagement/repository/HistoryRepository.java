package com.example.taskManagement.repository;

import com.example.taskManagement.model.HistoryTask;
import com.example.taskManagement.model.Task;
import com.example.taskManagement.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryTask, Integer> {

    @Query("SELECT t FROM HistoryTask t WHERE t.taskId=:id ORDER BY t.id DESC ")
    List<HistoryTask> getListHistoryOfTask(@PathVariable int id);
}
