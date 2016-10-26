import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBItems {

	public ArrayList<FoodItem> getMenuFromDB(String cusine) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList<FoodItem> menuItems = new ArrayList<>();

		try {

			// DB parameters\
			String url = "jdbc:sqlite:/home/harid/workspace_java/RestaurantMine/db/restaurantDB.db";
			// Create a connection to the database
			conn = DriverManager.getConnection(url);
			stmt = conn.createStatement();
			String where = "";
			if (cusine != null) {
				where = "WHERE Cuisine LIKE '%" + cusine + "%'";
			}

			String query = "SELECT * FROM foodMenu " + where;
			rs = stmt.executeQuery(query);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {

					while (rs.next()) {
						FoodItem foodItem = new FoodItem();
						foodItem.setDishName(rs.getString("Dish"));
						foodItem.setPrice(rs.getInt("Price"));
						menuItems.add(foodItem);
					}

					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}

		return menuItems;
	}

}