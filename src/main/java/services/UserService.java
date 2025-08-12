package services;

import java.util.List;

import entity.Users;
import repository.UserRepository;

public class UserService {
	
	private UserRepository userRepository = new UserRepository();
	
	public List<Users> getAllUser() {
		return userRepository.findAll();
	} 
}
