package quizzically.models;

import java.util.Arrays;

import quizzically.config.MyDBInfo;
import quizzically.lib.MySql;

public abstract class Model {
	private int id;
	private String table;
	private Hydrator hydrator;

	public Model(int id, String table, Hydrator hydrator) {
		this.id = id;
		this.table = table;
		this.hydrator = hydrator;
	}

	protected static Model retrieve(String table, int id, 
			Hydrator hydrator) {
		MySql sql = MySql.getInstance();
		return sql.get(table, id, hydrator);
	}

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

	public void save() {
		save(false);
	}

	protected void save(boolean create) {
		if (create) {
			insert();
		} else {
			update();
		}
	}

	private void insert() {
		MySql sql = MySql.getInstance();
		int id = sql.insert(table(), this, hydrator);
		this.id = id;
	}

	private void update() {
		MySql sql = MySql.getInstance();
		sql.update(table(), this, hydrator);
	}

}
