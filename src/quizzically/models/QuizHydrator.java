package quizzically.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QuizHydrator extends Hydrator {
		@Override
		public Model hydrate(ResultSet rs) 
				throws java.sql.SQLException{
			int id, ownerId;
			String name, description;
			java.util.Date createdAt;
			int pageFormat, order;

			rs.first();
			id = rs.getInt("id");
			ownerId = rs.getInt("owner_id");
			name = rs.getString("name");
			description = rs.getString("description");
			createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
			pageFormat = rs.getInt("page_format");
			order = rs.getInt("order");

			return new Quiz(id, name, ownerId, description, 
					createdAt, pageFormat, order);
		}

		@Override
		public void dehydrate(PreparedStatement stmt, Model m, int offset) 
				throws java.sql.SQLException {
			Quiz quiz = (Quiz) m;
			stmt.setString(offset++, quiz.name());
			stmt.setInt(offset++, quiz.ownerId());
			stmt.setString(offset++, quiz.description());
			stmt.setTimestamp(offset++, 
					new java.sql.Timestamp(quiz.createdAt().getTime()));
			stmt.setInt(offset++, quiz.pageFormat());
			stmt.setInt(offset++, quiz.order());
		}
	}
