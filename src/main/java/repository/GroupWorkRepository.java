package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import config.MySQLConfig;
import entity.Jobs;

public class GroupWorkRepository {
	public List<Jobs> findAll(){
		List<Jobs> listJobs = new ArrayList<Jobs>();
		
		String query = "SELECT * FROM jobs";
		
		Connection connection = MySQLConfig.getConnection();
		
		try {
			PreparedStatement statement = connection.prepareStatement(query);
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
			System.out.println("Lỗi truy vấn" + e.getMessage());
		}
		
		return listJobs;
	}
}
