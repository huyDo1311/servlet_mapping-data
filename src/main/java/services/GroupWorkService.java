package services;

import java.util.List;

import entity.Jobs;
import repository.GroupWorkRepository;

public class GroupWorkService {
	private GroupWorkRepository groupWorkRepository = new GroupWorkRepository();
	
	public List<Jobs> getAllJob(){
		return groupWorkRepository.findAll();
	}
}
