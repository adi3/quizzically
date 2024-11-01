package quizzically.models;

import java.util.Arrays;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;

/**
 * Defines a base class for database models, providing methods for retrieval, saving,
 * and CRUD operations, with support for abstraction and extension. It uses a Hydrator
 * to manage data and MySql for database interactions.
 */
public abstract class Model {
	private int id;
	private String table;
	private Hydrator hydrator;

	public Model(int id, String table, Hydrator hydrator) {
		this.id = id;
		this.table = table;
		this.hydrator = hydrator;
	}

	/**
	 * Retrieves a model from a database table based on a given ID, utilizing a hydrator
	 * for data mapping. It leverages the `MySql` instance to execute a query and returns
	 * the retrieved model. The `table` parameter specifies the database table to query.
	 *
	 * @param table database table from which a record with the specified `id` is to be
	 * retrieved.
	 *
	 * @param id primary key value used to retrieve a specific record from the specified
	 * database table.
	 *
	 * @param hydrator object responsible for mapping database results to a Model object.
	 *
	 * @returns a Model object hydrated by the provided Hydrator instance.
	 */
	protected static Model retrieve(String table, int id, 
			Hydrator hydrator) {
		MySql sql = MySql.getInstance();
		return sql.get(table, id, hydrator);
	}

	/**
	 * Returns the value of the instance variable `id`.
	 *
	 * @returns the value of the instance variable `id`.
	 */
	public int id() {
		return id;
	}

	/**
	 * Get the table used by this model
	 */
	protected String table() {
		return table;
	}
	
	public abstract String[] cols();

	/**
	 * Calls the `save` function with a parameter of `false`.
	 */
	public void save() {
		save(false);
	}

	/**
	 * Determines whether to insert or update a record based on the `create` boolean
	 * parameter. If `create` is true, it calls the `insert` method to create a new record.
	 * If `create` is false, it calls the `update` method to modify an existing record.
	 *
	 * @param create operation mode, where a true value indicates an insert operation and
	 * a false value indicates an update operation.
	 */
	protected void save(boolean create) {
		if (create) {
			insert();
		} else {
			update();
		}
	}

	/**
	 * Inserts a new record into a database table, retrieves the generated ID, and assigns
	 * it to the object's `id` field. It uses a singleton instance of `MySql` to interact
	 * with the database. The `table`, `this`, and `hydrator` parameters are used to
	 * determine the table and data to be inserted.
	 */
	private void insert() {
		MySql sql = MySql.getInstance();
		int id = sql.insert(table(), this, hydrator);
		this.id = id;
	}

	/**
	 * Fetches a MySQL instance, uses it to update a database table with data from the
	 * current object, and hydrates the object with the updated data.
	 */
	private void update() {
		MySql sql = MySql.getInstance();
		sql.update(table(), this, hydrator);
	}

}
