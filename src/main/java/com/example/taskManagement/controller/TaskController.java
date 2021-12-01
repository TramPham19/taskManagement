package com.example.taskManagement.controller;

import com.example.taskManagement.model.HistoryTask;
import com.example.taskManagement.model.Task;
import com.example.taskManagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/")
public class TaskController {

    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    HistoryController historyController;

    public TaskController(TaskRepository Repository)
    {
        taskRepository = Repository;
    }
    @GetMapping(value = "/task")
    public List<Task> getAll() {
//        System.out.println(taskRepository.findAll());
        return taskRepository.findAll();
    }

    @GetMapping(value = "/task/{id}")
    public Optional<Task> getTaskById( @PathVariable int id) {
        return taskRepository.findById(id);
    }

    @PostMapping(value = "/task")
    public Task postTask(@RequestBody final Task task) {
        if ( task.getPoint_task() < 1 || task.getPoint_task() > 5)
        {
            return null;
        }else {
            if(task.getParent_task()!=null){
                taskRepository.findById(task.getParent_task())
                        .map(task1 -> {
                            task.setPoint_task(task1.getPoint_task());
                            task.setAssignee(task1.getAssignee());
                            task.setProgress(task1.getProgress());
                            return taskRepository.save(task);
                        });
            }
            return taskRepository.save(task);
        }
    }

    //Ability to assign/re-assign a given task to a given assignee
    @PutMapping(value = "/task/assign/{id}")
    public Optional<Task> updateAssign(@RequestBody final String NewAssignee, @PathVariable int id){
        HistoryTask history = new HistoryTask(id,"Successful assign/re-assign "+ NewAssignee +" a given task", new Date());
        historyController.persist(history);
        taskRepository.findById(id)
                .map(task1 -> {
                    task1.setAssignee(NewAssignee);
                    return taskRepository.save(task1);
                });
        return taskRepository.findById(id);
    }

    @PutMapping(value = "/task/point/{id}")
    public Optional<Task> updatePoint(@RequestBody final Integer newPoint, @PathVariable int id){
        if ( newPoint < 1 || newPoint > 5)
        {
            return null;
        }else {
            HistoryTask history = new HistoryTask(id,"Successful update point task "+ id +" = "+newPoint, new Date());
            historyController.persist(history);
            taskRepository.findById(id)
                    .map(task1 -> {
                        task1.setPoint_task(newPoint);
                        return taskRepository.save(task1);
                    });
            return taskRepository.findById(id);
        }
    }

    @PutMapping(value = "/task/progress/{id}")
    public Optional<Task> updateProgress(@RequestBody final String newProgress, @PathVariable int id){
        HistoryTask history = new HistoryTask(id,"Successful update progress task "+ id +" = "+newProgress, new Date());
        historyController.persist(history);
        taskRepository.findById(id)
                .map(task1 -> {
                    try {
                        task1.setProgress(newProgress);
                        if(newProgress.toUpperCase().compareTo("IN_PROGRESS")==0){
                                task1.setStart_date(formatDate.parse(formatDate.format(new Date())));
                        }else if(newProgress.toUpperCase().compareTo("DONE")==0){
                            task1.setEnd_date(formatDate.parse(formatDate.format(new Date())));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return taskRepository.save(task1);
                });
        return taskRepository.findById(id);
    }


    //========================check search single search fields
    @GetMapping(value = "/task/description_task/{description_task}")
    public List<Task> getTaskByDescription( @PathVariable String description_task) {
        return taskRepository.findByDescription(description_task);
    }

    @GetMapping(value = "/task/sortASC")
    public List<Task> sortAsc() {
        return taskRepository.sortAscById();
    }

    @GetMapping(value = "/task/sortDESC")
    public List<Task> sortDesc() {
        return taskRepository.sortDescById();
    }

    //========================end check search single search fields================

    //multiple search fields input have to description_task, point_task, assignee, current progress
    // ex: http://localhost:8080/task/search?description_task=api connect&point_task=1&assignee=N4
    @GetMapping(value = "/task/search")
    public List<Task> searchMultiple(@Param("description_task") String description_task,
                                     @Param("point_task") Integer point_task, @Param("assignee") String assignee, @Param("progress") String progress) {
        if(taskRepository.findByDescriptionAndPointAndAssigneeProgress(description_task,point_task,assignee,progress).size()>0)
            return taskRepository.findByDescriptionAndPointAndAssigneeProgress(description_task,point_task,assignee,progress);
        else return null;
    }

    public Sort.Direction typeSort(String property){
        Sort.Direction type;
        if (property.toUpperCase().compareTo("ASC") == 0) {
            type = Sort.Direction.ASC;
        } else if (property.toUpperCase().compareTo("DESC") == 0) {
            type = Sort.Direction.DESC;
        } else {
            type = Sort.Direction.DESC;
        }
        return type;
    }
    //==============================multiple search fields + multiple sort==========
    @GetMapping(value = "/task/searchSort")
    public List<Task> search(@Param("description_task") String description_task,@Param("point_task") Integer point_task,
                             @Param("assignee") String assignee, @Param("progress") String progress, @Param("progressSort") String progressSort, @Param("pointSort") String pointSort
            , @Param("descriptionSort") String descriptionSort, @Param("assigneeSort") String assigneeSort)  {

        List<Order> orders = new ArrayList<Order>();
        Sort.Direction typeSort;
        //rule (ascending: TODO->IN-PROGRESS->DONE, descending: DONE->IN-PROGRESS->TODO)
        if(progressSort!=null) {
            if (progressSort.toUpperCase().compareTo("DESC") == 0) {
                typeSort = Sort.Direction.ASC;
            } else if (progressSort.toUpperCase().compareTo("ASC") == 0) {
                typeSort = Sort.Direction.DESC;
            } else {
                typeSort = Sort.Direction.DESC;
            }

            Order order = new Order(typeSort, "progress");
            orders.add(order);
        }

        if(pointSort!=null) {
            typeSort=typeSort(pointSort);
            Order order1 = new Order(typeSort, "point_task");
            orders.add(order1);
        }

        if(descriptionSort!=null) {
            typeSort = typeSort(descriptionSort);
            Order order2 = new Order(typeSort, "description_task");
            orders.add(order2);
        }

        if(assigneeSort!=null) {
            typeSort = typeSort(assigneeSort);
            Order order3 = new Order(typeSort, "assignee");
            orders.add(order3);
        }

        if(taskRepository.findByDescriptionAndPointAndAssignee(description_task,point_task,assignee,progress,Sort.by(orders)).size()>0)
            return taskRepository.findByDescriptionAndPointAndAssignee(description_task,point_task,assignee,progress,Sort.by(orders));
        else return null;
    }
}
