package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.MySQLConfig;
import entity.Users;

public class UserRepository {
	
	public List<Users> findAll() {
		List<Users> listUsers = new ArrayList<Users>();
		
		String query = "SELECT * FROM users u JOIN roles r ON u.role_id = r.id";
		
		Connection connection = MySQLConfig.getConnection();
		
		try {	
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Users users = new Users();
				users.setFullName(resultSet.getString("fullname"));
				users.setEmail(resultSet.getString("email"));
				users.setId(resultSet.getInt("id"));
				users.setRoleDescription(resultSet.getNString("description"));
				
				listUsers.add(users);
				
			}
			
		} catch (Exception e) {
			System.out.println("Truy vấn bị lỗi" + e.getMessage());
		}
		
		return listUsers;
		
	}
	
	 public Users findById(int id) {
	        String query = "SELECT u.*, r.description AS role_description " +
	                       "FROM users u " +
	                       "JOIN roles r ON u.role_id = r.id " +
	                       "WHERE u.id=?";
	        try (Connection conn = MySQLConfig.getConnection();
	             PreparedStatement ps = conn.prepareStatement(query)) {

	            ps.setInt(1, id);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                Users user = new Users();
	                user.setId(rs.getInt("id"));
	                user.setFullName(rs.getString("fullname"));
	                user.setEmail(rs.getString("email"));
	                user.setAvatar(rs.getString("avatar"));
	                user.setRoleId(rs.getInt("role_id"));
	                user.setRoleDescription(rs.getString("role_description"));
	                return user;
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	 
	 public Users findByEmail(String email) {
		    String query = "SELECT u.*, r.description AS role_description " +
		                   "FROM users u " +
		                   "JOIN roles r ON u.role_id = r.id " +
		                   "WHERE u.email = ?";
		    try (Connection conn = MySQLConfig.getConnection();
		         PreparedStatement ps = conn.prepareStatement(query)) {

		        ps.setString(1, email);
		        ResultSet rs = ps.executeQuery();

		        if (rs.next()) {
		            Users user = new Users();
		            user.setId(rs.getInt("id"));
		            user.setFullName(rs.getString("fullname"));
		            user.setEmail(rs.getString("email"));
		            user.setAvatar(rs.getString("avatar"));
		            user.setRoleId(rs.getInt("role_id"));
		            user.setRoleDescription(rs.getString("role_description"));
		            return user;
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return null;
		}

	 
	 public boolean insert(Users user) {
	        String query = "INSERT INTO users (fullname, email, password, avatar, role_id) VALUES (?, ?, ?, ?, ?)";
	        try (Connection conn = MySQLConfig.getConnection();
	             PreparedStatement ps = conn.prepareStatement(query)) {

	            ps.setString(1, user.getFullName());
	            ps.setString(2, user.getEmail());
	            ps.setString(3, user.getPassword());
	            ps.setString(4, user.getAvatar());
	            ps.setInt(5, user.getRoleId());

	            return ps.executeUpdate() > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	 public boolean update(Users user) {
	        String query = "UPDATE users SET fullname=?, email=?, password=?, avatar=?, role_id=? WHERE id=?";
	        try (Connection conn = MySQLConfig.getConnection();
	             PreparedStatement ps = conn.prepareStatement(query)) {

	            ps.setString(1, user.getFullName());
	            ps.setString(2, user.getEmail());
	            ps.setString(3, user.getPassword());
	            ps.setString(4, user.getAvatar());
	            ps.setInt(5, user.getRoleId());
	            ps.setInt(6, user.getId());

	            return ps.executeUpdate() > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

	    public boolean delete(int id) {
	        String query = "DELETE FROM users WHERE id=?";
	        try (Connection conn = MySQLConfig.getConnection();
	             PreparedStatement ps = conn.prepareStatement(query)) {

	            ps.setInt(1, id);
	            return ps.executeUpdate() > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	    
	    public List<Users> findByJobId(int jobId) {
	        List<Users> users = new ArrayList<>();
	        String query = "SELECT DISTINCT u.* FROM users u " +
	                       "JOIN tasks t ON u.id = t.user_id " +
	                       "WHERE t.job_id = ?";
	        try (Connection conn = MySQLConfig.getConnection();
	             PreparedStatement ps = conn.prepareStatement(query)) {
	            ps.setInt(1, jobId);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                Users u = new Users();
	                u.setId(rs.getInt("id"));
	                u.setFullName(rs.getString("fullname"));    
	                u.setEmail(rs.getString("email"));
	                // … các field khác
	                users.add(u);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return users;
	    }

}
