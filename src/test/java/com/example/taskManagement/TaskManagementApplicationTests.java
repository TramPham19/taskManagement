package com.example.taskManagement;

import com.example.taskManagement.controller.HistoryController;
import com.example.taskManagement.model.HistoryTask;
import com.example.taskManagement.model.Task;
import com.example.taskManagement.repository.HistoryRepository;
import com.example.taskManagement.repository.TaskRepository;
import com.example.taskManagement.controller.TaskController;
import jdk.internal.org.jline.reader.History;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TaskManagementApplicationTests {

	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
//	@Test
//	void contextLoads() {
//	}
	@Autowired
	private TaskController taskController;

//	@Autowired
//	private MockMvc mvc;
//	@InjectMocks
//	private TaskController taskController;
	public MockMvc mockMvc;
	@MockBean
	private TaskRepository taskRepository;

	@Autowired
	private HistoryController historyController;

	@MockBean
	private HistoryRepository historyRepository;

	@Test
	public void getAllTest(){
		when(taskRepository.findAll()).thenReturn(Stream
				.of(new Task(1,"Task 1", 1, "An", "TODO",null),
						new Task(2,"Task 2", 2, "An", "TODO",2)).collect(Collectors.toList()));
		assertEquals(2, taskController.getAll().size());
	}

	@Test
	public void postTaskTest(){
		Task task = new Task(1,"Task 3",3,"An", "TODO",null);
		Task task1 = new Task("Task 3",3,"An", "TODO",null);
		when(taskRepository.save(task1)).thenReturn(task);
		assertEquals(task, taskController.postTask(task1));

	}

	@Test
	public void postTaskPointInValidTest(){
		Task task = new Task(3,"Task 3",6,"An", "TODO",null);
		when(taskRepository.save(task)).thenReturn(task);
		assertEquals(null, taskController.postTask(task));

	}

	//Ability to add a subtask for a given task. All of above abilities on task should be available on subtask
	@Test
	public void postSubTaskTest(){
		Task ExpectedTask = new Task(4,"sub task",3,"An", "TODO",3);
		Task task_parent = new Task(3,"Task 3",3,"An", "TODO",null);
		Task subtask = new Task(4,"sub task",1,null, null,3);

		when(taskRepository.save(subtask)).thenReturn(subtask);

		when(taskRepository.findById(3)).thenReturn(Optional.of(task_parent));
		assertEquals(ExpectedTask.getId(), taskController.postTask(subtask).getId());
		assertEquals(ExpectedTask.getDescription_task(), taskController.postTask(subtask).getDescription_task());
		assertEquals(ExpectedTask.getPoint_task(), taskController.postTask(subtask).getPoint_task());
		assertEquals(ExpectedTask.getAssignee(), taskController.postTask(subtask).getAssignee());
		assertEquals(ExpectedTask.getProgress(), taskController.postTask(subtask).getProgress());
		assertEquals(ExpectedTask.getPoint_task(), taskController.postTask(subtask).getPoint_task());

	}

	@Test
	public void updateAssignTest() throws Exception {
		Task task = new Task(1,"Task 3",3,"An", "TODO",null);
		when(taskRepository.save(task)).thenReturn(task);
		when(taskRepository.findById(1)).thenReturn(Optional.of(task));
		assertEquals("Tram", taskController.updateAssign("Tram",1).get().getAssignee());
	}

	@Test
	public void updatePointTest() throws Exception {
		Task task = new Task(1,"Task 3",3,"An", "TODO",null);
		when(taskRepository.save(task)).thenReturn(task);
		when(taskRepository.findById(1)).thenReturn(Optional.of(task));
		assertEquals(1, taskController.updatePoint(1,1).get().getPoint_task());
	}

	@Test
	public void updateProgressTest() throws Exception {
		Task task = new Task(1,"Task 3",3,"An", "TODO",null);
		Task taskExpected = new Task(1,"Task 3",3,"An", "IN_PROGRESS",null,formatDate.parse(formatDate.format(new Date())));
		when(taskRepository.save(task)).thenReturn(task);
		when(taskRepository.findById(1)).thenReturn(Optional.of(task));
		//start_date & end_date updated if change progress
		assertEquals(taskExpected.getProgress(), taskController.updateProgress("IN_PROGRESS",1).get().getProgress());
		assertEquals(taskExpected.getStart_date(), taskController.updateProgress("IN_PROGRESS",1).get().getStart_date());
	}


	@Test
	public void updateProgressDoneTest() throws Exception {
		Task task = new Task(1,"Task 3",3,"An", "TODO",null);
		Task taskExpected = new Task(1,"Task 3",3,"An", "DONE",null,null,formatDate.parse(formatDate.format(new Date())));
		when(taskRepository.save(task)).thenReturn(task);
		when(taskRepository.findById(1)).thenReturn(Optional.of(task));
		//start_date & end_date updated if change progress
		assertEquals(taskExpected.getProgress(), taskController.updateProgress("DONE",1).get().getProgress());
		assertEquals(taskExpected.getEnd_date(), taskController.updateProgress("DONE",1).get().getEnd_date());

	}

	//test search by discription example to do multiple sort
	@Test
	public void searchTaskDiscriptionTest() throws Exception {
		String description = "Task";
		when(taskController.getTaskByDescription(description)).thenReturn(Stream
				.of(new Task(3,"Task 3",3,"An", "TODO",null)).collect(Collectors.toList()));
		assertEquals(1, taskController.getTaskByDescription(description).size());
	}

	@Test
	public void searchTaskTest() throws Exception {
		String description = "Task";
		when(taskController.search(description,null,null,null,null,null,null,null)).thenReturn(Stream
				.of(new Task(3,"Task 3",3,"An", "TODO",null)).collect(Collectors.toList()));
		assertEquals(1, taskController.search(description,null,null,null,null,null,null,
				null).size());
	}


	@Test
	public void showAllHistoryTest() throws Exception {
		when(historyRepository.findAll()).thenReturn(Stream
				.of(new HistoryTask(1,"1",new Date()),
						new HistoryTask(2,"2",new Date())).collect(Collectors.toList()));
		assertEquals(2, historyController.getAll().size());
	}

	@Test
	public void showHistoryOfTaskTest() throws Exception {
		Integer TaskId = 1;
		when(historyRepository.getListHistoryOfTask(TaskId)).thenReturn(Stream
				.of(new HistoryTask(1,"1",new Date()),
						new HistoryTask(1,"2",new Date())).collect(Collectors.toList()));
		assertEquals(2, historyController.getHistory(TaskId).size());
	}

}
