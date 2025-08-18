package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.MySQLConfig;
import entity.Roles;
import entity.Users;

public class RoleRepository {
	
	public List<Roles> findAll() {
		List<Roles> listRoles = new ArrayList<Roles>();
		
		String query = "Select * from roles";
		
		Connection connection = MySQLConfig.getConnection();
		
		try {	
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Users users = new Users();
				Roles roles = new Roles();
				roles.setId(resultSet.getInt("id"));
				roles.setName(resultSet.getString("name"));
				roles.setDescription(resultSet.getString("description"));
	
				listRoles.add(roles);
				
			}
			
		} catch (Exception e) {
			System.out.println("Truy vấn bị lỗi" + e.getMessage());
		}
		
		return listRoles;
	}
}
