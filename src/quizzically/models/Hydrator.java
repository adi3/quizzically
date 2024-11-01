package quizzically.models;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * It provides a framework for converting database data into objects and vice versa,
 * using a model abstraction.
 * The class handles database errors and allows for single or multiple object hydration.
 * It is an abstract class that must be extended to provide concrete implementation
 * for hydrating and dehydrating models.
 */
public abstract class Hydrator {
	/**
	 * Extracts a single Model object from a ResultSet, assuming a list of Models is
	 * returned by the function called with the same parameters.
	 *
	 * @param rs ResultSet object that contains the data to be processed by the function.
	 *
	 * @returns an array containing a single `Model` object.
	 */
	public Model fromResultSet(ResultSet rs) {
		return fromResultSet(rs, false)[0];
	}
	
	/**
	 * Converts a database result set into a Model array, handling both single and multiple
	 * result sets. It hydrates each row into a Model object, either returning an array
	 * of models for multiple results or a single model for a single result.
	 *
	 * @param rs ResultSet object that contains the data to be hydrated into Model objects.
	 *
	 * Get its type, which is a `ResultSet` object.
	 * ResultSet is an interface that provides methods to access and manipulate the data
	 * contained in the result set, which is returned by a database query.
	 *
	 * @param many nature of the query result, indicating whether it is expected to return
	 * multiple records or a single record.
	 *
	 * @returns an array of `Model` objects, or a single `Model` object if `many` is false.
	 *
	 * The output is an array of `Model` objects. If multiple models are expected, the
	 * array contains multiple `Model` objects. If a single model is expected, the array
	 * contains a single `Model` object.
	 */
	public Model[] fromResultSet(ResultSet rs, boolean many) {
		try {
			if (many) {
				ArrayList<Model> results = new ArrayList<Model>();
				while (rs.next()) {
					results.add(hydrate(rs));
				}
				return results.toArray(new Model[0]);
			} else {
				rs.first();
				return new Model[] { hydrate(rs) };
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("DB Error: Problem re-hydrating model");
		}
	}

	/**
	 * Prepares a database query by dehydrating a model, which involves setting parameters
	 * in a prepared statement. It catches any SQL exceptions that occur during this
	 * process and throws a runtime exception with a descriptive error message.
	 *
	 * @param stmt PreparedStatement object to which data is dehydrated.
	 *
	 * @param m Model object that is being dehydrated by the `dehydrate` method.
	 *
	 * @param offset position at which to begin de-hydrating the model.
	 */
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
