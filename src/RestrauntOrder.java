import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class RestrauntOrder {

	public static void main(String[] args) throws SQLException {

		JFrame window = new JFrame();

		window.setExtendedState(window.MAXIMIZED_BOTH);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout(0, 50));

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel title = new JLabel();
		title.setText("Restaurant Name");
		titlePanel.add(title);
		window.add(BorderLayout.NORTH, titlePanel);

		JPanel cuisineSelectorPanel = new JPanel();
		JPanel menuPanel = new JPanel();
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new GridLayout(0, 2));
		containerPanel.add(cuisineSelectorPanel);

		JScrollPane scroll = new JScrollPane(menuPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(null);

		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
		ButtonGroup radioButtonGroup = new ButtonGroup();
		JPanel radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new GridLayout(0, 2));
		radioButtonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		JRadioButton radioButton1 = new JRadioButton();
		JRadioButton radioButton2 = new JRadioButton();
		JRadioButton radioButton3 = new JRadioButton();
		JRadioButton radioButton4 = new JRadioButton();

		JPanel goButtonPanel = new JPanel();
		goButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton goButton = new JButton();
		goButton.setText("Go");
		goButtonPanel.add(goButton);

		radioButton1.setText("Indian");
		radioButton2.setText("Chinese");
		radioButton3.setText("Italian");
		radioButton4.setText("American");
		radioButtonPanel.add(radioButton1);
		radioButtonPanel.add(radioButton2);
		radioButtonPanel.add(radioButton3);
		radioButtonPanel.add(radioButton4);
		radioButtonGroup.add(radioButton1);
		radioButtonGroup.add(radioButton2);
		radioButtonGroup.add(radioButton3);
		radioButtonGroup.add(radioButton4);

		filterPanel.add(radioButtonPanel);
		filterPanel.add(goButtonPanel);
		filterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.blue), "Filters",
				TitledBorder.CENTER, TitledBorder.TOP));

		String selectedRadioButton[] = { null };

		JPanel menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.blue), "Menu",
				TitledBorder.CENTER, TitledBorder.TOP));
		JPanel menu_item = null;

		DBItems dbItems = new DBItems();
		ArrayList<String> cart = new ArrayList();

		goButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Enumeration<AbstractButton> buttons = radioButtonGroup.getElements(); buttons.hasMoreElements();) {

					AbstractButton button = buttons.nextElement();

					if (button.isSelected()) {
						selectedRadioButton[0] = button.getText();
					}

				}
				try {
					loadMenuItems(window, cuisineSelectorPanel, menuPanel, containerPanel, filterPanel, scroll,
							selectedRadioButton[0], menu, dbItems, cart);
				} catch (SQLException exception) {
					exception.printStackTrace();
				}
			}
		});

		loadMenuItems(window, cuisineSelectorPanel, menuPanel, containerPanel, filterPanel, scroll,
				selectedRadioButton[0], menu, dbItems, cart);

	}

	private static void loadMenuItems(JFrame window, JPanel cuisineSelectorPanel, JPanel menuPanel,
			JPanel containerPanel, JPanel filterPanel, JScrollPane scroll, String selectedRadioButton, JPanel menu,
			DBItems dbItems, ArrayList<String> cart) throws SQLException {

		JPanel menu_item;
		ArrayList<FoodItem> foodItems = null;
		menuPanel.removeAll();
		menu.removeAll();
		containerPanel.remove(scroll);
		containerPanel.add(scroll);

		foodItems = dbItems.getMenuFromDB(selectedRadioButton);

		for (FoodItem foodItem : foodItems) {
			menu_item = new JPanel();
			JLabel item = new JLabel(foodItem.getDishName());
			item.setPreferredSize(new Dimension(210, 20));
			menu_item.add(item);

			Integer price = foodItem.getPrice();
			JLabel priceLabel = new JLabel(price.toString());
			menu_item.add(priceLabel);

			JPanel Quantity = new JPanel();
			JButton decQty = new JButton("-");
			JButton incQty = new JButton("+");

			JTextField qty = new JTextField(" 0 ");
			int qty2 = Integer.parseInt(qty.getText().trim());
			int totalPrice = price * qty2;

			Quantity.add(decQty);
			Quantity.add(qty);
			Quantity.add(incQty);
			menu_item.add(Quantity);

			JLabel totalCost = new JLabel(Integer.toString(totalPrice));
			menu_item.add(totalCost);

			JButton addToCart = new JButton("Order");
			menu_item.add(addToCart);

			addToCart.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					cart.add(item.getText());
				}
			});

			menu_item.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));

			menu.add(menu_item);

			decQty.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String quantity = qty.getText();
					quantity = quantity.trim();
					int quty = Integer.parseInt(quantity);
					if (quty == 0) {
						// do nothing
					} else {
						quty--;
					}
					String finalQ = " " + quty + " ";
					qty.setText(finalQ);
					Integer totalPrice = price * quty;
					totalCost.setText(totalPrice.toString());
				}
			});

			incQty.setText("+");
			incQty.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					String quantity = qty.getText();
					quantity = quantity.trim();
					int quty = Integer.parseInt(quantity);
					quty++;
					String finalQ = " " + quty + " ";
					qty.setText(finalQ);
					Integer totalPrice = price * quty;
					totalCost.setText(totalPrice.toString());
				}
			});

			menuPanel.add(menu);
		}

		window.add(BorderLayout.CENTER, containerPanel);

		cuisineSelectorPanel.add(filterPanel);
		window.setVisible(true);
	}

}
