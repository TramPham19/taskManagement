package com.example.taskManagement.repository;

import com.example.taskManagement.model.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    // find task through Description task
    @Query("SELECT t FROM Task t WHERE t.description_task=?1")
    List<Task> findByDescription(String description_task);

    //Sorting ascending
    @Query("SELECT t FROM Task t ORDER BY t.id ASC ")
    List<Task> sortAscById();

    //Sorting descending
    @Query("SELECT t FROM Task t ORDER BY t.id DESC ")
    List<Task> sortDescById();

    //multiple search fields
    @Query("SELECT t FROM Task t WHERE (:description_task is null or t.description_task like %:description_task%) AND"
            +"(:point_task is null or t.point_task =:point_task) AND"+"(:assignee is null or t.assignee like %:assignee%) AND"
            +"(:progress is null or t.progress like %:progress%)" )
    List<Task> findByDescriptionAndPointAndAssigneeProgress(@Param("description_task") String description_task,
                                                    @Param("point_task") Integer point_task, @Param("assignee") String assignee,
                                                    @Param("progress") String progress);

    //multiple search fields Sorting ascending and descending
//    @Query("SELECT t FROM Task t order by t.description_task "+ "desc" )
////            "ORDER BY (:description_task is null or :description_task),"
////            +"(:point_task is null or :point_task),"+"(:assignee is null or :assignee),"
////            +"(:progress is null or :progress)" )
//    List<Task> findMultipleSort(@Param("description_task") String description_task);
//            ,
//                                                    @Param("point_task") String point_task, @Param("assignee") String assignee,
//                                                    @Param("progress") String progress);

    @Query("SELECT t FROM Task t WHERE (:description_task is null or t.description_task like %:description_task%) AND"
            +"(:point_task is null or t.point_task =:point_task) AND"+"(:assignee is null or t.assignee like %:assignee%) AND"
            +"(:progress is null or t.progress like %:progress%)" )
    List<Task> findByDescriptionAndPointAndAssignee(@Param("description_task") String description_task,
                                                            @Param("point_task") Integer point_task, @Param("assignee") String assignee,
                                                            @Param("progress") String progress, Sort sort);

}
