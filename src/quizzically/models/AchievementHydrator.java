package quizzically.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles the mapping of Achievement objects to database query results.
 */
public class AchievementHydrator extends Hydrator {
	private static final int NULL_VALUE = -1;

	/**
	 * Populates an Achievement object with data from a database ResultSet.
	 *
	 * @param rs ResultSet object containing data retrieved from a database.
	 *
	 * @returns a new instance of the `Achievement` class.
	 */
	@Override
	protected Model hydrate(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		int type = rs.getInt("type");
		int userId = rs.getInt("user_id");
		return new Achievement(id, type, userId);
	}

	/**
	 * Populates a `PreparedStatement` with data from an `Achievement` object. It sets
	 * two integer parameters: the achievement type and the user ID, incrementing the
	 * offset after each set operation.
	 *
	 * @param stmt PreparedStatement object used to execute SQL queries.
	 *
	 * @param m model being dehydrated, which is cast to an `Achievement` object.
	 *
	 * @param offset current position in the prepared statement where the next parameter
	 * value should be set.
	 */
	@Override
	protected void dehydrate(PreparedStatement stmt, Model m, int offset)
			throws SQLException {
		Achievement ach = (Achievement) m;
		stmt.setInt(offset++, ach.type());
		stmt.setInt(offset++, ach.userId());
	}

}
