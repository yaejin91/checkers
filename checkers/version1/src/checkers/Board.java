package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

class Board extends JPanel implements ActionListener {
	MoveLogic boardLogic;
	MoveData allMoves;
	ParsedMovesFile parsedMovesArrayList;

	// ******CHANGE FILE NAME IF YOU WANT TO USE A DIFFERENT GAME FILE******
	String fileName = "src/samplemoves2.txt";
	// Create a new list of strings from parsed text file
	List<String> movesArrayList = parsedMovesArrayList.listOfMoves(fileName);

	// buttons to be used except for 'previous' and newGame, previous not
	// implemented
	// newGame not wanted by assignment standards
	JButton newGame;
	JButton next;
	JButton previous;

	int lineNumber;
	int whosTurn;

	// Board constructor
	public Board() {
		// newGame JButton allows user to start a new Game. Will NOT be on the GUI
		newGame = new JButton("New Game");
		previous = new JButton("Previous");
		next = new JButton("Next");

		// Connecting ActionListeners to the buttons
		newGame.addActionListener(this);
		next.addActionListener(this);

		// calling on classes that contain all moves rules and data
		boardLogic = new MoveLogic();
		allMoves = new MoveData();

		// start a new game with the file you want
		newGame(fileName);
	}

	public void newGame(String fileName) {
		boardLogic.newBoard();
		// Red always starts first
		whosTurn = MoveLogic.Red;

		// You can now click the new game and the next button
		newGame.setEnabled(true);
		next.setEnabled(true);

		repaint();
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == next) {
			if (lineNumber < movesArrayList.size()) {
				// Read the current move from-to tile pair from the text file
				String currentMove = movesArrayList.get(lineNumber);
				try {
					getMoveCoord(lineNumber, currentMove);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "That's not a valid move!");
				}
				lineNumber++;
			} else {
				gameOver();
			}

		} else if (event.getSource() == newGame) {
			lineNumber = 0;
			newGame(fileName);
		}
	}

	// Changing to and from tile number format into coordinates for those tiles
	public void getMoveCoord(int lineNumber, String currentMove) {
		int[] currentLine = allMoves.getFromToTile(lineNumber, currentMove);
		// Array of coordinates [row, col] for a tile coordinate
		int[] fromTileCoord = allMoves.getTileCoord(currentLine[0]);
		int[] toTileCoord = allMoves.getTileCoord(currentLine[1]);

		// Assign each integers
		int fromRow = fromTileCoord[0];
		int fromCol = fromTileCoord[1];
		int toRow = toTileCoord[0];
		int toCol = toTileCoord[1];

		// Getting the potential next move line

		movePiece(fromRow, fromCol, toRow, toCol, whosTurn);
	}

	public void movePiece(int fromRow, int fromCol, int toRow, int toCol, int pieceType) {
		// Check if move is legal
		int[] legalNextMove = boardLogic.nextMove(fromRow, fromCol, toRow, toCol, pieceType);

		// If legal, set the new destination tile to the legally approved row and column
		int legalNextRow = legalNextMove[0];
		int legalNextCol = legalNextMove[1];

		// Call tile status change with the legal move
		boardLogic.tileStatusChange(fromRow, fromCol, legalNextRow, legalNextCol);

		// Switch turns after move is made
		if (whosTurn == MoveLogic.Red) {
			whosTurn = MoveLogic.Black;
		} else if (whosTurn == MoveLogic.Black) {
			whosTurn = MoveLogic.Red;
		}

		repaint();
	}

	public void gameOver() {
		String winner;
		// If the counter for killed black pieces is 12,
		// red wins
		if (boardLogic.blackKillCounter == 12) {
			winner = "Red";
			// If the counter for killed black pieces is 12,
			// black wins
		} else if (boardLogic.redKillCounter == 12) {
			winner = "Black";
		} else {
			winner = "No one";
		}
		System.out.println("Game Over! " + winner + " wins!");
		repaint();
		JOptionPane.showMessageDialog(null, "Game Over! " + winner + " wins!");
	}

	public void paintComponent(Graphics boardElement) {
		// Paint a black border around the board
		boardElement.setColor(Color.BLACK);
		boardElement.fillRect(0, 0, 325, 325);

		// variables to change size/shape of the graphics
		int offset = 4;
		int spaceBetweenPieces = 40;
		int diameter = 35;
		int squareOffset = 2;
		int squareSize = 40;
		int kingOffsetRow = 25;
		int kingOffsetCol = 15;

		// Creating a checkered pattern for the board
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (row % 2 == col % 2) {
					boardElement.setColor(Color.GRAY);
				} else {
					boardElement.setColor(Color.DARK_GRAY);
				}

				// Draw the checkered board
				boardElement.fillRect(squareOffset + col * squareSize, squareOffset + row * squareSize, squareSize,
						squareSize);

				// painting the pieces depending on to which player the square belongs to
				switch (boardLogic.tileStatus(row, col)) {
				// If the square is owned by the red player, place a red piece
				case MoveLogic.Red:
					boardElement.setColor(Color.RED);
					boardElement.fillOval(offset + col * spaceBetweenPieces, offset + row * spaceBetweenPieces,
							diameter, diameter);
					break;

				// If the square is owned by the black player, place a black piece
				case MoveLogic.Black:
					boardElement.setColor(Color.BLACK);
					boardElement.fillOval(offset + col * spaceBetweenPieces, offset + row * spaceBetweenPieces,
							diameter, diameter);
					break;

				// If the square is owned by the red player's King, place a red piece and draw
				// a "K"
				case MoveLogic.RedKing:
					boardElement.setColor(Color.RED);
					boardElement.fillOval(offset + col * spaceBetweenPieces, offset + row * spaceBetweenPieces,
							diameter, diameter);
					// Draw String K for King
					boardElement.setColor(Color.WHITE);
					boardElement.drawString("K", kingOffsetCol + col * spaceBetweenPieces,
							kingOffsetRow + row * spaceBetweenPieces);
					break;

				// If the square is owned by the black player's King, place a black piece and
				// draw a "K"
				case MoveLogic.BlackKing:
					boardElement.setColor(Color.BLACK);
					boardElement.fillOval(offset + col * spaceBetweenPieces, offset + row * spaceBetweenPieces,
							diameter, diameter);
					// Draw String K for King
					boardElement.setColor(Color.WHITE);
					boardElement.drawString("K", kingOffsetCol + col * spaceBetweenPieces,
							kingOffsetRow + row * spaceBetweenPieces);
					break;
				}
			}
		}
	}
}