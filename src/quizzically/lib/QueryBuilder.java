package quizzically.lib;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import quizzically.config.MyDBInfo;

public class QueryBuilder {
	public enum Type {
		SELECT, INSERT, UPDATE, DELETE
	}

	public enum Order {
		ASCENDING, DESCENDING;
		public String toString() {
			switch (this) {
				case ASCENDING:
					return "ASC";
				case DESCENDING:
					return "DESC";
				default:
					throw new RuntimeException("Invalid Order");
			}
		}
	}

	public enum Operator {
		EQUALS, NOT_EQUAL, GREATER_THAN, LESS_THAN, IN, NOT_IN,
			GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL,
			NOT_NULL;
		public String toString() {
			switch (this) {
				case EQUALS:
					return "=";
				case NOT_EQUAL:
					return "!=";
				case GREATER_THAN:
					return ">";
				case LESS_THAN:
					return "<";
				case GREATER_THAN_OR_EQUAL:
					return ">=";
				case LESS_THAN_OR_EQUAL:
					return "<=";
				case IN:
					return "IN";
				case NOT_IN:
					return "NOT IN";
				case NOT_NULL:
					return "IS NOT NULL";
				default:
					throw new RuntimeException("Invalid Operator");
			}
		}
	}

	/**
	 * Get a QueryBuilder for a select query
	 */
	public static QueryBuilder selectInstance(String table, String[] cols) {
		return new QueryBuilder(Type.SELECT, table, cols);
	}

	/**
	 * Get a QueryBuilder for an insert query
	 */
	public static QueryBuilder insertInstance(String table, String[] cols) {
		return new QueryBuilder(Type.INSERT, table, cols);
	}

	/**
	 * Get a QueryBuilder for an update query
	 */
	public static QueryBuilder updateInstance(String table, String[] cols) {
		return new QueryBuilder(Type.UPDATE, table, cols);
	}

	/**
	 * Get a QueryBuilder for a delete query
	 */
	public static QueryBuilder deleteInstance(String table, String[] cols) {
		return new QueryBuilder(Type.DELETE, table, cols);
	}

	private Type type;
	private String orderBy;
	private Order order;
	int limit;
	private ArrayList<Constraint> constraints;
	private String table;
	private String[] cols;

	private QueryBuilder(Type type, String table, String[] cols) {
		this.type = type;
		this.table = table;
		this.cols = cols;
		this.orderBy = null;
		this.limit = -1;
		constraints = new ArrayList<Constraint>();
	}

	/**
	 * Set the sort order of this query
	 * @param field Field to order by
	 * @param order QueryBuilder.ASCENDING|QueryBuilder.DESCENDING
	 */
	public void setOrder(String field, Order order) {
		assert(type == Type.SELECT);
		this.orderBy = field;
		this.order = order;
	}

	/**
	 * Set the limit on the query
	 * Note: should be > 0
	 * @param limit the limit
	 */
	public void setLimit(int limit) {
		assert(limit > 0);
		this.limit = limit;
	}

	public void addConstraint(String field, Operator op) {
		assert(op == Operator.NOT_NULL);
		addConstraint(field, op, new NullType());
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
			case SELECT:
				sql += "SELECT * FROM ";
				sql += db() + "." + table();
				sql += " WHERE " + where();
				if (orderBy != null) {
					sql += " ORDER BY `" + orderBy + "` " + order;
				}
				if (limit != -1) {
					sql += " LIMIT " + limit;
				}
				break;
			case INSERT:
			case UPDATE:
				// TODO impl
				throw new RuntimeException("QueryBuilder type not implemented yet");
//				break;
			case DELETE:
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
			case SELECT:
				constraintOffset = 0; // TODO modify to support non * column queries
				break;
			case INSERT:
			case UPDATE:
				constraintOffset = cols().length;
				break;
			case DELETE:
				constraintOffset = 0;
				break;
		}
		int max = constraints.size();
		int inset = 0;
		for (int i = 0; i < max;) {
			Constraint c = constraints.get(i);
			for (Object value : c.values()) {
				int ind = 1 + constraintOffset + i + inset;
				if (value instanceof Integer) {
					stmt.setInt(ind, (Integer) value);
				}
				else if (value instanceof java.util.Date) {
					stmt.setTimestamp(ind, new java.sql.Timestamp(((java.util.Date) value).getTime()));
				}
				else if (value instanceof NullType) {
					// do nothing
					inset--;
				}
				// TODO impl String, etc...
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
			else if (op == Operator.NOT_NULL) {
				return "`" + field + "` " + op;
			}
			return "`" + field + "` " + op + " " + placeholder;
		}
	}

	/**
	 * Used for Constraints with no value
	 */
	private class NullType {
	}
}
