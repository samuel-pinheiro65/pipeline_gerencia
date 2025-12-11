package com.example.pipeline_gerencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.example.pipeline_gerencia.model.*;
import com.example.pipeline_gerencia.repository.TaskRepository;
import com.example.pipeline_gerencia.repository.UserRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryTaskRepository;
import com.example.pipeline_gerencia.repository.impl.InMemoryUserRepository;
import com.example.pipeline_gerencia.service.TaskService;
import com.example.pipeline_gerencia.service.UserService;
import java.time.LocalDateTime;

@SpringBootApplication
public class PipelineGerenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PipelineGerenciaApplication.class, args);
	}

	@Bean
	CommandLineRunner demoRunner() {
		return args -> {
			// Demo of the Task Manager
			System.out.println("\n=== Task Manager Demo ===\n");
			
			UserRepository userRepository = new InMemoryUserRepository();
			TaskRepository taskRepository = new InMemoryTaskRepository();
			UserService userService = new UserService(userRepository);
			TaskService taskService = new TaskService(taskRepository);
			
			// Create some users
			User user1 = userService.createUser("João Silva", "joao@example.com", "Desenvolvimento");
			User user2 = userService.createUser("Maria Santos", "maria@example.com", "Gerência");
			
			// Create some tasks
			Task task1 = new Task("Implementar autenticação", "Sistema de login com JWT");
			task1.setAssignee(user1);
			task1.setPriority(Priority.HIGH);
			task1.setDueDate(LocalDateTime.now().plusDays(3));
			taskService.createTask(task1);
			
			Task task2 = new Task("Revisar código", "Code review dos endpoints");
			task2.setAssignee(user2);
			task2.setPriority(Priority.MEDIUM);
			task2.setDueDate(LocalDateTime.now().plusDays(5));
			taskService.createTask(task2);
			
			// Display statistics
			System.out.println("Total users: " + userService.getAllUsers().size());
			System.out.println("Total tasks: " + taskService.getAllTasks().size());
			System.out.println("High priority tasks: " + taskService.getTasksByPriority(Priority.HIGH).size());
			System.out.println("\n=== Application is now running ===\n");
		};
	}
}
