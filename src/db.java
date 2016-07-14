import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class db {
	private static String db = "";
	private static String user = "";
	private static String pwd = "";
	static final String DATABASE_URL = "jdbc:mysql://localhost";
	private static java.sql.Statement statement;
	private static java.sql.Connection connection;
	private static String sql_query;
	private static ResultSet resultSet;

	public static void setuser(String usern) {
		user = usern;
	}

	public static void setpassword(String pwdn) {
		pwd = pwdn;
	}

	public static void setconnection(String username, String password) {
		try {
			connection = DriverManager.getConnection(DATABASE_URL, username,
					password);
			statement = connection.createStatement();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void setdatabase(String dbs) {
		try {
			setconnection(user, pwd);
			sql_query = "USE " + dbs + ";";
			statement.execute(sql_query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void insert_into_table(String table_name, String[] cols,
			String[] vals) {
		sql_query = insert_query(table_name, cols, vals);
		System.out.println(sql_query);
		try {
			statement.execute(sql_query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void insert_into_table(String table_name, String[] cols,
			String[][] vals) throws SQLException {
		for (int i = 0; i < vals.length; i++) {

			sql_query = insert_query(table_name, cols, vals[i]);
			System.out.println(sql_query);
			try {
				statement.execute(sql_query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
	}

	public static String insert_into_table_getID(String table_name,
			String[] cols, String[] vals) throws SQLException {

		sql_query = insert_query(table_name, cols, vals);

		System.out.println(sql_query);
		try {
			statement.execute(sql_query);
			sql_query = "select MAX(id) from " + table_name + ";";
			String val = returnselectresponse(sql_query);
			return val;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public static String[] Select_query_allwhere_string(String[] para,
			String col, String db_name, String page_id) {
		String query = "SELECT * FROM " + db_name + " where ";
		int count = 0;
		for (int i = 1; i < para.length; i++) {
			if (para[i].length() >= 1 && !para[i].equals(" ")) {
				if (count == 0) {
					query = query + col + " like '%" + para[i] + "%' ";
					count++;
				} else {
					query = query + " AND " + col + " like '%" + para[i]
							+ "%' ";
				}
			}
		}
		query = query + ";";

		ResultSet r = null;
		List<String> list = new LinkedList<String>();
		try {
			statement.executeUpdate("USE " + db_name);
			sql_query = query;
			r = statement.executeQuery(query);

			while (r.next()) {
				String add = "";
				try {
					add = r.getObject(1).toString().trim() + ":"
							+ r.getObject(2).toString().trim() + ":"
							+ r.getObject(3).toString().trim() + ":"
							+ r.getObject(4).toString().trim();
				} catch (Exception e) {
				}
				list.add(add);
			}
			String[] array = list.toArray(new String[0]);
			return array;

		} catch (SQLException sqlException) {
			System.out.println(sql_query);
			sqlException.printStackTrace();
		} finally {
			try {

			} catch (Exception exception) {
				System.out.println(sql_query);
				exception.printStackTrace();
			}
		}
		return null;
	}

	public static String insert_query(String tn, String[] col, String[] val) {

		// char ch=(int);
		String query = "insert into " + tn + " (" + col[0];
		for (int i = 1; i < col.length; i++) {
			val[i] = val[i].replace("'", "\\'");

			query += ",";
			query += col[i];
		}
		query += " ) values (\'" + val[0] + "'";
		for (int i = 1; i < val.length; i++) {
			{
				query += "," + "\'";
				query += val[i];
				query += "\'";
			}
		}
		query += ");";
		return query;
		/*
		 * try{ statement.executeUpdate(query); }catch(Exception e) {
		 * e.printStackTrace(); }
		 */

	}

	public static String returnselectresponse(String sqlstmt) {
		try {
			resultSet = statement.executeQuery(sqlstmt);
			String value = "";
			while (resultSet.next()) {
				value = resultSet.getString(1);
			}

			return value;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
