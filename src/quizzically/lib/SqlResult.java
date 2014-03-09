package quizzically.lib;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SqlResult extends ArrayList<HashMap<String, String>> {

	public SqlResult(ResultSet rs) {
		try {
			ResultSetMetaData md = rs.getMetaData();
			int count = md.getColumnCount();
			String[] cols = new String[count];
			for (int i = 1; i <= count; i++) cols[i-1] = md.getColumnName(i);
			
			while(rs.next()) {
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < count; i++) map.put(cols[i], rs.getString(cols[i]));
				this.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
