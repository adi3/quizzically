package quizzically.lib;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import quizzically.config.MyDBInfo;

public class QueryBuilder {
	public enum Type {
		TYPE_SELECT, TYPE_INSERT, TYPE_UPDATE, TYPE_DELETE
	}

	public enum Order {
		ORDER_ASCENDING, ORDER_DESCENDING
	}

	public enum Operator {
		EQUALS, GREATER_THAN, LESS_THAN, IN, NOT_IN;
		public String toString() {
			switch (this) {
				case EQUALS:
					return "=";
				case GREATER_THAN:
					return ">";
				case LESS_THAN:
					return "<";
				case IN:
					return "IN";
				case NOT_IN:
					return "NOT IN";
				default:
					throw new RuntimeException("Invalid operator");
			}
		}
	}

	/**
	 * Get a QueryBuilder for a select query
	 */
	public static QueryBuilder selectInstance(String table, String[] cols) {
		return new QueryBuilder(Type.TYPE_SELECT, table, cols);
	}

	/**
	 * Get a QueryBuilder for an insert query
	 */
	public static QueryBuilder insertInstance(String table, String[] cols) {
		return new QueryBuilder(Type.TYPE_INSERT, table, cols);
	}

	/**
	 * Get a QueryBuilder for an update query
	 */
	public static QueryBuilder updateInstance(String table, String[] cols) {
		return new QueryBuilder(Type.TYPE_UPDATE, table, cols);
	}

	/**
	 * Get a QueryBuilder for a delete query
	 */
	public static QueryBuilder deleteInstance(String table, String[] cols) {
		return new QueryBuilder(Type.TYPE_DELETE, table, cols);
	}

	private Type type;
	private String orderBy;
	private Order order;
	private ArrayList<Constraint> constraints;
	private String table;
	private String[] cols;

	private QueryBuilder(Type type, String table, String[] cols) {
		this.type = type;
		this.table = table;
		this.cols = cols;
		constraints = new ArrayList<Constraint>();
	}

	/**
	 * Set the sort order of this query
	 * @param field Field to order by
	 * @param order QueryBuilder.ASCENDING|QueryBuilder.DESCENDING
	 */
	public void setOrder(String field, Order order) {
		assert(type == Type.TYPE_SELECT);
		orderBy = field;
		this.order = order;
	}

	public void addConstraint(String field, Operator op, Object value) {
		constraints.add(new Constraint(field, op, value));
	}

	/**
	 * Add a constraint - for IN/NOT IN operators
	 */
	public void addConstraint(String field, Operator op, Object[] value) {
		assert(op == Operator.IN || op == Operator.NOT_IN);
		constraints.add(new Constraint(field, op, value));
	}

	private String db() {
		return MyDBInfo.MYSQL_DATABASE_NAME;
	}

	private String table() {
		return table;
	}

	public String sql() {
		String sql = "";
		switch (type) {
			case TYPE_SELECT:
				sql += "SELECT * FROM ";
				sql += db() + "." + table();
				sql += " WHERE " + where();
				// TODO support order by, limit
				break;
			case TYPE_INSERT:
			case TYPE_UPDATE:
				// TODO impl
				throw new RuntimeException("QueryBuilder type not implemented yet");
//				break;
			case TYPE_DELETE:
				sql += "DELETE FROM ";
				sql += db() + "." + table();
				sql += " WHERE " + where();
				// TODO support order by, limit
				break;
		}
		return sql;
	}

	/**
	 * Must be called right after sql() (no intermediary update calls)
	 * @throws SQLException if the PreparedStatement throws a SQLException
	 */
	public void prepareStatement(PreparedStatement stmt) throws SQLException {
		int constraintOffset = 0;
		switch (type) {
			case TYPE_SELECT:
				constraintOffset = 0; // TODO modify to support non * column queries
				break;
			case TYPE_INSERT:
			case TYPE_UPDATE:
				constraintOffset = cols().length;
				break;
			case TYPE_DELETE:
				constraintOffset = 0;
				break;
		}
		for (int i = 0; i < constraints.size(); i++) {
			Constraint c = constraints.get(i);
			for (Object value : c.values()) {
				if (value instanceof Integer) {
					stmt.setInt(1 + constraintOffset + i, (Integer) value);
				}
				// TODO impl
	//			else if (c instanceof Date) {
	//			}
				else {
					throw new RuntimeException("QueryBuilder value type not implemented yet");
				}
				i++;
			}
		}
	}

	private String[] cols() {
		return cols;
	}

	/**
	 * Get the where clause with placeholders for PreparedStatement
	 */
	private String where() {
		String where = "";
		if (constraints.size() != 0) {
			where += constraints.get(0).toString();
		}
		for (int i = 1; i < constraints.size(); i++) {
			Constraint c = constraints.get(i);
			where += " AND " + c;
		}
		return where;
	}

	private class Constraint {
		private String field;
		private Operator op;
		private Object[] values;

		public Constraint(String field, Operator op, Object value) {
			this(field, op, new Object[] { value });
		}

		public Constraint(String field, Operator op, Object[] values) {
			this.field = field;
			this.op = op;
			this.values = values;
		}

		public Object[] values() {
			return values;
		}

		public String toString() {
			String placeholder = "?";
			if (op == Operator.NOT_IN || op == Operator.IN) {
				placeholder = "(";
				for (int i = 0; i < values.length;) {
					placeholder += "?";
					if (i++ < values.length) {
						placeholder += ", ";
					}
				}
				placeholder += ")";
			}
			return "`" + field + "` " + op + " " + placeholder;
		}
	}
}
