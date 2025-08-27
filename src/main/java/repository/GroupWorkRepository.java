package repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.MySQLConfig;
import entity.Jobs;

public class GroupWorkRepository {

    // Lấy tất cả job
    public List<Jobs> findAll() {
        List<Jobs> listJobs = new ArrayList<>();
        String query = "SELECT * FROM jobs";

        try (Connection connection = MySQLConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Jobs job = new Jobs();
                job.setId(resultSet.getInt("id"));
                job.setName(resultSet.getNString("name"));
                job.setStart_date(resultSet.getDate("start_date"));
                job.setEnd_date(resultSet.getDate("end_date"));
                listJobs.add(job);
            }
        } catch (Exception e) {
            System.out.println("Lỗi truy vấn findAll: " + e.getMessage());
        }
        return listJobs;
    }

    // Thêm job
    public boolean insert(Jobs job) {
        String query = "INSERT INTO jobs(name, start_date, end_date) VALUES(?, ?, ?)";
        try (Connection connection = MySQLConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setNString(1, job.getName());
            statement.setDate(2, job.getStart_date());
            statement.setDate(3, job.getEnd_date());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi insert job: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật job
    public boolean update(Jobs job) {
        String query = "UPDATE jobs SET name = ?, start_date = ?, end_date = ? WHERE id = ?";
        try (Connection connection = MySQLConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setNString(1, job.getName());
            statement.setDate(2, job.getStart_date());
            statement.setDate(3, job.getEnd_date());
            statement.setInt(4, job.getId());
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi update job: " + e.getMessage());
        }
        return false;
    }

    // Xóa job
    public boolean delete(int id) {
        String query = "DELETE FROM jobs WHERE id = ?";
        try (Connection connection = MySQLConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi delete job: " + e.getMessage());
        }
        return false;
    }

    // Tìm job theo id
    public Jobs findById(int id) {
        String query = "SELECT * FROM jobs WHERE id = ?";
        try (Connection connection = MySQLConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Jobs job = new Jobs();
                job.setId(rs.getInt("id"));
                job.setName(rs.getNString("name"));
                job.setStart_date(rs.getDate("start_date"));
                job.setEnd_date(rs.getDate("end_date"));
                return job;
            }
        } catch (Exception e) {
            System.out.println("Lỗi findById job: " + e.getMessage());
        }
        return null;
    }
	
}
