package services;

import java.util.List;

import entity.Users;
import repository.UserRepository;

public class UserService {
	private final UserRepository userRepository = new UserRepository();

	public List<Users> getAllUser() {
		return userRepository.findAll();
	}

	public Users getUserById(int id) {
		return userRepository.findById(id);
	}

	public Users getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean addUser(Users user) {
		return userRepository.insert(user);
	}

	public boolean updateUser(Users user) {
		return userRepository.update(user);
	}

	public boolean deleteUser(int id) {
		return userRepository.delete(id);
	}

	public List<Users> getUsersByJobId(int jobId) {
		return userRepository.findByJobId(jobId);
	}

}
