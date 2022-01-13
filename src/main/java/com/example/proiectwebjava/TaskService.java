package com.example.proiectwebjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {

    @Autowired
    private EmployeeRepository employeeRepository ;
    @Autowired
    private TaskRepository taskRepository;

    public TaskService(EmployeeRepository employeeRepository, TaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
    }

    public Collection<Task> getAllTasks() {
        return (Collection<Task>) taskRepository.findAll();
    }

    public Optional<Task> findTaskById(long id){
        return taskRepository.findById(id);
    }

    public void saveTask (Task task) {
        taskRepository.save(task);
    }

    public void deleteTask (Task task) {
        taskRepository.delete(task);
    }

}
