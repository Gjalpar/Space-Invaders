package SpaceInvaders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.util.ArrayList;

public class Account extends JFrame{
	public static ArrayList<Account> accounts = new ArrayList<Account>();
	public static ArrayList<Score> scores = new ArrayList<Score>();
	public static Account account;
	private JTextField textField;
	private JPasswordField passwordField;
	private String username;
	private String password;
	private int health = 3;
	private int score = 0;
	private int level = 1;

	public Account(){
		setBounds(100, 100, 480, 360);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setVisible(true);

		JLabel newLabel_1 = new JLabel("Username: ");
		newLabel_1.setFont(new Font("Terminal", Font.BOLD, 15));
		newLabel_1.setBounds(10, 45, 100, 15);
		getContentPane().add(newLabel_1);

		textField = new JTextField();
		textField.setBounds(100, 38, 350, 25);
		getContentPane().add(textField);
		textField.setColumns(10);

		JLabel newLabel_2 = new JLabel("Password: ");
		newLabel_2.setFont(new Font("Terminal", Font.BOLD, 15));
		newLabel_2.setBounds(10, 90, 100, 15);
		getContentPane().add(newLabel_2);

		passwordField = new JPasswordField();
		passwordField.setBounds(100, 83, 350, 25);
		getContentPane().add(passwordField);

		JLabel newLabel_3 = new JLabel("");
		newLabel_3.setBounds(10, 135, 350, 25);
		getContentPane().add(newLabel_3);

		JButton newButton = new JButton("Create Account");
		newButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				username = textField.getText();
				password = new String(passwordField.getPassword());
				String string = "";

				if(password.length() == 0 && username.length() == 0)
					string = "Password and username are empty!";

				else if(password.length() == 0)
					string = "Password is empty!";

				else if(username.length() == 0)
					string = "Username is empty!";

				else if(hasBlank(password) && hasBlank(username))
					string  = "Password and username cannot contain spaces!";

				else if(hasBlank(password))
					string  = "Password cannot contain spaces!";

				else if(hasBlank(username))
					string = "Username cannot contain spaces!";

				else if(usernameTaken(username))
					string = "This username taken!";

				if (string.equals(""))
					setVisible(false);

				newLabel_3.setText(string);
			}

			private boolean usernameTaken(String username){
				for(int i = 0; i < accounts.size() - 1; i++){
					if(username.equals(accounts.get(i).getUsername()))
						return true;
				}

				return false;
			}

			private boolean hasBlank(String string){
				for(int i = 0; i < string.length(); i++){
					if(string.charAt(i) == ' ')
						return true;
				}

				return false;
			}
		});

		newButton.setFont(new Font("Terminal", Font.BOLD, 15));
		newButton.setBounds(250, 200, 175, 75);
		add(newButton);
	}

	public String getUsername(){
		return username;
	}

	public String getPassword(){
		return password;
	}

	public int getHealth(){
		return health;
	}

	public int getScore(){
		return score;
	}

	public int getLevel(){
		return level;
	}

	public void setHealth(int health){
		this.health = health;
	}

	public void setScore(int score){
		this.score = score;
	}

	public void setLevel(int level){
		this.level = level;
	}

	public void decreaseHealth(){
		health--;
	}

	public void addScore(int add){
		score = score + add;
	}

	public void increaseLevel(){
		level++;
	}
}