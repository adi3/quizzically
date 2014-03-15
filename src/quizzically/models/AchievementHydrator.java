package quizzically.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AchievementHydrator extends Hydrator {
	private static final int NULL_VALUE = -1;

	@Override
	protected Model hydrate(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		int type = rs.getInt("type");
		int userId = rs.getInt("user_id");
		return new Achievement(id, type, userId);
	}

	@Override
	protected void dehydrate(PreparedStatement stmt, Model m, int offset)
			throws SQLException {
		Achievement ach = (Achievement) m;
		stmt.setInt(offset++, ach.type());
		stmt.setInt(offset++, ach.userId());
	}

}
