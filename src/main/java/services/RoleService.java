package services;

import java.util.List;

import config.MySQLConfig;
import entity.Roles;
import repository.RoleRepository;

public class RoleService {
	private RoleRepository roleRepository = new RoleRepository();

	public List<Roles> getAllRole() {
		return roleRepository.findAll();
	}

	public boolean insertRole(String name, String desc) {
		return roleRepository.save(name, desc) > 0;
	}

	public Roles getRoleById(String role_id) {
		return roleRepository.findById(role_id);
	}

	public boolean updateRole(Roles role) {
		if (role == null || role.getId() <= 0) {
			return false; // validate cơ bản
		}
		return roleRepository.update(role);
	}
	
	public boolean deleteRole(int id) {
        return roleRepository.deleteRole(id);
    }

	
}
