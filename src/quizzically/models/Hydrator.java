package quizzically.models;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Hydrator {
	public Model fromResultSet(ResultSet rs) {
		try {
			return hydrate(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Problem re-hydrating model");
		}
	}

	public void prepareStatement(PreparedStatement stmt,
			Model m, int offset) {
		try {
			dehydrate(stmt, m, offset);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Problem de-hydrating model");
		}
	}
	

	/**
	 * "Hydrate" a model from the ResultSet
	 * @param rs The ResultSet to hydrate
	 * @return The Model generated
	 */
	protected abstract Model hydrate(ResultSet rs)
		throws SQLException;

	/**
	 * "Dehydrate" a model into the PreparedStatement
	 * @param stmt The PreparedStatement to dehydrate to
	 * @param model The Model to dehydrate
	 * @param offset The field offset to start at in stmt
	 */
	protected abstract void dehydrate(PreparedStatement stmt, 
			Model m, int offset) throws SQLException;
}
