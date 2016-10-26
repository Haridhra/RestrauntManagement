
public class FoodItem {

	private String dishName;
	private Integer price;

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "FoodItem [dishName=" + dishName + ", price=" + price + "]";
	}
	
	

}
