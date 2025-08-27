package services;

import java.util.List;

import entity.Jobs;
import repository.GroupWorkRepository;

public class GroupWorkService {
	private GroupWorkRepository groupWorkRepository = new GroupWorkRepository();
	
	public List<Jobs> getAllJob() {
        return groupWorkRepository.findAll();
    }

    public boolean addJob(Jobs job) {
        return groupWorkRepository.insert(job);
    }

    public boolean updateJob(Jobs job) {
        return groupWorkRepository.update(job);
    }

    public boolean deleteJob(int id) {
        return groupWorkRepository.delete(id);
    }

    public Jobs getJobById(int id) {
        return groupWorkRepository.findById(id);
    }
}
