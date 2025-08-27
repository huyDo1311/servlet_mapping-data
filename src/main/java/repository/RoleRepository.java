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
	
	public int save(String name, String desc) {
		int rowCount = 0;
		
		//injection là truyền câu truy vấn vào 
		// để tránh sql inject thì không nối chuỗi và truyền theo biến
		
		String query = "insert into roles (name, description) values(?, ?)";
		
		Connection connection = MySQLConfig.getConnection();
		
		try {
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, name);
			statement.setString(2, desc);
			
			rowCount = statement.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
		
		return rowCount;
	}
	
	public Roles findById(String role_id) {
	    String query = "SELECT * FROM roles WHERE id = ?";
	    Roles role = null;

	    try (Connection connection = MySQLConfig.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {

	        statement.setString(1, role_id);
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            role = new Roles();
	            role.setId(resultSet.getInt("id"));
	            role.setName(resultSet.getString("name"));
	            role.setDescription(resultSet.getString("description"));
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return role;
	}
	
	public boolean update(Roles role) {
        String query = "UPDATE roles SET name = ?, description = ? WHERE id = ?";
        try (Connection connection = MySQLConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, role.getName());
            statement.setString(2, role.getDescription());
            statement.setInt(3, role.getId());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;

        } catch (Exception e) {
            System.out.println("Error update role: " + e.getMessage());
            return false;
        }
    }
	
	public boolean deleteRole(int id) {
        String query = "DELETE FROM roles WHERE id = ?";
        try (Connection connection = MySQLConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu xóa thành công
        } catch (Exception e) {
        	System.out.println("Error delete role: " + e.getMessage());
        }
        return false;
    }


}
