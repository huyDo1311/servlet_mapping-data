package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import config.MySQLConfig;
import entity.Tasks;

public class TaskRepository {

	// Lấy toàn bộ Task
	public List<Tasks> findAll() {
		List<Tasks> list = new ArrayList<>();
		String query = "SELECT " + "t.id, " + "t.name AS task_name, " + "t.start_date, " + "t.end_date, "
				+ "t.user_id, " + "t.job_id, " + "t.status_id, " + "u.fullname AS user_name, " + "j.name AS job_name, "
				+ "s.name AS status_name " + "FROM tasks t " + "JOIN users u ON t.user_id = u.id "
				+ "JOIN jobs j ON t.job_id = j.id " + "JOIN status s ON t.status_id = s.id";

		try (Connection conn = MySQLConfig.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Tasks t = new Tasks();
				t.setId(rs.getInt("id"));
				t.setName(rs.getString("task_name"));
				t.setStart_date(rs.getDate("start_date"));
				t.setEnd_date(rs.getDate("end_date"));
				t.setUser_id(rs.getInt("user_id"));
				t.setJob_id(rs.getInt("job_id"));
				t.setStatus_id(rs.getInt("status_id"));
				t.setJobName(rs.getString("job_name"));
				t.setStatusName(rs.getString("status_name"));
				t.setUserName(rs.getString("user_name")); // mới
				list.add(t);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thêm mới
	public boolean insert(Tasks task) {
		String query = "INSERT INTO tasks (name, start_date, end_date, user_id, job_id, status_id) VALUES (?,?,?,?,?,?)";
		try (Connection conn = MySQLConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, task.getName());
			ps.setDate(2, task.getStart_date());
			ps.setDate(3, task.getEnd_date());
			ps.setInt(4, task.getUser_id());
			ps.setInt(5, task.getJob_id());
			ps.setInt(6, task.getStatus_id());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Cập nhật
	public boolean update(Tasks task) {
		String query = "UPDATE tasks SET name=?, start_date=?, end_date=?, user_id=?, job_id=?, status_id=? WHERE id=?";
		try (Connection conn = MySQLConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, task.getName());
			ps.setDate(2, task.getStart_date());
			ps.setDate(3, task.getEnd_date());
			ps.setInt(4, task.getUser_id());
			ps.setInt(5, task.getJob_id());
			ps.setInt(6, task.getStatus_id());
			ps.setInt(7, task.getId());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateStatus(int taskId, int statusId) {
		String query = "UPDATE tasks SET status_id=? WHERE id=?";
		try (Connection conn = MySQLConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setInt(1, statusId);
			ps.setInt(2, taskId);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Xóa
	public boolean delete(int id) {
		String query = "DELETE FROM tasks WHERE id=?";
		try (Connection conn = MySQLConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Tìm theo ID
	public Tasks findById(int id) {
		String query = "SELECT * FROM tasks WHERE id=?";
		try (Connection conn = MySQLConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return new Tasks(rs.getInt("id"), rs.getString("name"), rs.getDate("start_date"),
						rs.getDate("end_date"), rs.getInt("user_id"), rs.getInt("job_id"), rs.getInt("status_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Tìm tất cả task theo user_id
	public List<Tasks> findByUserId(int userId) {
		List<Tasks> list = new ArrayList<>();
		String query = "SELECT " + "t.id AS task_id, " + "t.name AS task_name, " + "t.start_date, " + "t.end_date, "
				+ "t.user_id, " + "t.job_id, " + "t.status_id, " + "u.fullname AS user_name, " + "j.name AS job_name, "
				+ "s.name AS status_name " + "FROM tasks t " + "LEFT JOIN users u ON t.user_id = u.id "
				+ "JOIN jobs j ON t.job_id = j.id " + "JOIN status s ON t.status_id = s.id " + "WHERE t.user_id = ? "
				+ "ORDER BY t.id ASC";

		try (Connection conn = MySQLConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Tasks t = new Tasks();
					t.setId(rs.getInt("task_id"));
					t.setName(rs.getString("task_name"));
					t.setStart_date(rs.getDate("start_date"));
					t.setEnd_date(rs.getDate("end_date"));
					t.setUser_id(rs.getInt("user_id"));
					t.setJob_id(rs.getInt("job_id"));
					t.setStatus_id(rs.getInt("status_id"));
					t.setJobName(rs.getString("job_name"));
					t.setStatusName(rs.getString("status_name"));

					// user_name có thể null nếu user_id không tồn tại
					String userName = rs.getString("user_name");
					t.setUserName(userName != null ? userName : "Chưa gán");

					list.add(t);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Tasks> getTaskByJobId(int jobId) {
		List<Tasks> list = new ArrayList<>();
		String query = "SELECT " + "t.id AS task_id, " + "t.name AS task_name, " + "t.start_date, " + "t.end_date, "
				+ "t.user_id, " + "t.job_id, " + "t.status_id, " + "u.fullname AS user_name, " + "j.name AS job_name, "
				+ "s.name AS status_name " + "FROM tasks t " + "LEFT JOIN users u ON t.user_id = u.id "
				+ "JOIN jobs j ON t.job_id = j.id " + "JOIN status s ON t.status_id = s.id " + "WHERE j.id = ? "
				+ "ORDER BY t.id ASC";

		try (Connection connection = MySQLConfig.getConnection();
				PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setInt(1, jobId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Tasks task = new Tasks();
				task.setId(rs.getInt("task_id"));
				task.setName(rs.getString("task_name"));
				task.setStart_date(rs.getDate("start_date"));
				task.setEnd_date(rs.getDate("end_date"));
				task.setUser_id(rs.getInt("user_id"));
				task.setJob_id(rs.getInt("job_id"));
				task.setStatus_id(rs.getInt("status_id"));

				// Gán thêm các field từ JOIN
				task.setUserName(rs.getString("user_name") != null ? rs.getString("user_name") : "Chưa gán");
				task.setJobName(rs.getString("job_name"));
				task.setStatusName(rs.getString("status_name"));

				list.add(task);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
