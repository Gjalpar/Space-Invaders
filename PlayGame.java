package SpaceInvaders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PlayGame extends JFrame{
	private JTextField textField;
	private JPasswordField passwordField;
	private String username;
	private String password;

	public PlayGame(){
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

		JButton btnNewButton = new JButton("Sign In");
		btnNewButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				username = textField.getText();
				password = new String(passwordField.getPassword());

				if(hasUser(username, password)){
					setVisible(false);
					SpaceInvaders.newLabel.setIcon(new ImageIcon("health.png"));
					SpaceInvaders.newLabel.setText("x" + Account.account.getHealth() + "        |        " + "Level: " + Account.account.getLevel() + "        |        " + "Score: " + Account.account.getScore() + "        |        " + "Username: " + username);
					SpaceInvaders.newLabel.setFont(new Font("Terminal", Font.BOLD, 30));
					SpaceInvaders.panel.setState(1);
				}

				else
					newLabel_3.setText("Username or password is incorrect");
			}

			private boolean hasUser(String username, String password){
				for(int i = 0; i < Account.accounts.size(); i++){
					if(username.equals(Account.accounts.get(i).getUsername()) && password.equals(Account.accounts.get(i).getPassword())){
						Account.account = Account.accounts.get(i);
						return true;
					}
				}

				return false;
			}
		});

		btnNewButton.setFont(new Font("Terminal", Font.BOLD, 15));
		btnNewButton.setBounds(250, 200, 175, 75);
		add(btnNewButton);
	}
}