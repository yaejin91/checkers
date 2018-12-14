package checkers;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Class where the GUI is set up
public class Checkers extends JFrame{
	public void runCheckersGame() {
		//Add a JFrame that will contain all of the swing elements
		JFrame checkersGame = new JFrame();
		
		//Create panels for buttons
        JPanel buttonPanel = new JPanel();
        JPanel buttonPanel2 = new JPanel();
        
        checkersGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set the background color of the JFrame into dark green
        checkersGame.getContentPane().setBackground(new Color(34,139,34));;
        checkersGame.setSize(700,500);
        checkersGame.setLayout(null);

        //Creates board graphics components
        Board board = new Board();
        //Setting a boundary around the board and its border
        board.setBounds(30,80,325,325);
        checkersGame.add(board);
        checkersGame.add(buttonPanel);
        checkersGame.add(buttonPanel2);

        //Hidden because assignment said I shouldn't show a refresh button
        //buttonPanel.add(board.startNewGame);
        
        buttonPanel.add(board.next);
        
        //**Previous button does not work yet
        buttonPanel2.add(board.previous);
        
        //Set the background color of the buttons' JPanel into dark green to match
        buttonPanel.setBackground(new Color(34,139,34));
        buttonPanel2.setBackground(new Color(34,139,34));

        //Place the button on the right of the game board
        buttonPanel2.setBounds(373,150,150,30);
        buttonPanel.setBounds(400,300,150,30);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        checkersGame.setVisible(true);
	}
}