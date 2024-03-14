package SpaceInvaders;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SpaceInvaders extends JFrame{
	JMenuItem item1 = new JMenuItem("Register");
	JMenuItem item2 = new JMenuItem("Play Game");
	JMenuItem item3 = new JMenuItem("High Score");
	JMenuItem item4 = new JMenuItem("Quit");
	JMenuItem item5 = new JMenuItem("About");
	private static final JMenuBar menuBar = new JMenuBar();
	private static final JMenu newMenu_1 = new JMenu("File");
	private static final JMenu newMenu_2 = new JMenu("Help");
	public static Panel panel = new Panel();
	public static JLabel newLabel = new JLabel("                                                                                          Not logged in");

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					SpaceInvaders frame = new SpaceInvaders();
					frame.setVisible(true);
				}

				catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	public SpaceInvaders(){
		super("Space Invaders");
		setResizable(false);
		setFocusable(false);
		setSize(1600, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(newLabel, BorderLayout.NORTH);
		newLabel.setFont(new Font("Terminal", Font.BOLD, 30));

		panel.requestFocus();
		panel.addKeyListener(panel);
		panel.setFocusable(true);
		panel.setFocusTraversalKeysEnabled(false);

		add(panel);

		setJMenuBar(menuBar);

		menuBar.add(newMenu_1);
		newMenu_1.add(item1);
		newMenu_1.add(item2);
		newMenu_1.add(item3);
		newMenu_1.add(item4);

		menuBar.add(newMenu_2);
		newMenu_2.add(item5);

		Handler handler = new Handler();
		item1.addActionListener(handler);
		item2.addActionListener(handler);
		item3.addActionListener(handler);
		item4.addActionListener(handler);
		item5.addActionListener(handler);
	}

	private class Handler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event){
			if(event.getSource() == item1){
				EventQueue.invokeLater(new Runnable(){
					public void run(){
						try{
							Account account = new Account();
							Account.accounts.add(account);
						}

						catch (Exception e){
							e.printStackTrace();
						}
					}
				});
			}

			if(event.getSource() == item2){
				EventQueue.invokeLater(new Runnable(){
					public void run(){
						PlayGame game = new PlayGame();
						panel.setPlayGame();
					}
				});
			}

			if(event.getSource() == item3){
				String score = "";

				for (int i = 0; i < Account.scores.size(); i++)
					score = score + (i + 1) + "    " + Account.scores.get(i).getUsername() + "        |        " + Account.scores.get(i).getScore() + "\n";

				JOptionPane.showMessageDialog(SpaceInvaders.this, score);
			}

			if (event.getSource() == item4)
				System.exit(1);

			if (event.getSource() == item5)
				JOptionPane.showMessageDialog(SpaceInvaders.this, "SPACE INVADERS INC., ALL RIGHTS RESERVED, TM, ® & COPYRIGHT © 2023 BY EREN DUMLUPINAR");
		}
	}
}