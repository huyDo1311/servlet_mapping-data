package services;

import java.util.List;

import entity.Roles;
import repository.RoleRepository;

public class RoleService {
	private RoleRepository roleRepository = new RoleRepository();
	
	public List<Roles> getAllRole() {
		return roleRepository.findAll();
	}
}
