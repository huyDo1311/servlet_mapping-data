package services;

import java.util.List;
import entity.Tasks;
import repository.TaskRepository;

public class TaskService {
    private TaskRepository taskRepository = new TaskRepository();

    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    public boolean addTask(Tasks task) {
        return taskRepository.insert(task);
    }

    public boolean updateTask(Tasks task) {
        return taskRepository.update(task);
    }

    public boolean deleteTask(int id) {
        return taskRepository.delete(id);
    }

    public Tasks getTaskById(int id) {
        return taskRepository.findById(id);
    }
    
    public List<Tasks> getTaskByIdUser(int idUser) {
    	return taskRepository.findByUserId(idUser);
    }
    
    public List<Tasks> getTaskByIdJob(int idJob) {
    	return taskRepository.getTaskByJobId(idJob);
    }
    
    public boolean updateStatus(int taskId, int statusId) {
    	return taskRepository.updateStatus(taskId, statusId);
    }
}
