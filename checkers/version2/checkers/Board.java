package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Board extends JPanel implements MouseListener {
	// buttons to be used except for 'previous' and newGame, previous not
	// implemented
	// newGame not wanted by assignment standards
	JButton newGame;
	JButton hint;

	// Variables to keep track of logic of moves and data
	MoveLogic boardLogic;
	Move[] allMoves;
	int lineNumber;
	int whosTurn;
	int trackedRow, trackedCol;

	// Variables to change size/shape of the graphics
	int offset = 4;
	int diameter = 36;
	int squareOffset = 2;
	int squareSize = 40;
	int kingOffsetRow = 25;
	int kingOffsetCol = 15;

	boolean gameStarted;

	// Board constructor
	public Board() {
		addMouseListener(this);
		// newGame JButton allows user to start a new Game. Will NOT be on the GUI
		newGame = new JButton("New Game");
		hint = new JButton("Hint");
		// Connecting ActionListeners to the buttons
//		newGame.addActionListener(this);

		// Class that has all legal checks and logic
		boardLogic = new MoveLogic();

		// Start a new game
		newGame();
	}

	public void newGame() {
		boardLogic.newBoard();
		// No pieces are chosen at the very beginning of the game
		trackedRow = -1;
		// Red always starts first
		// whosTurn = boardLogic.Red;
		whosTurn = boardLogic.Red;
		// allMoves = boardLogic.allMoves(boardLogic.Red);
		allMoves = boardLogic.legalMoves(boardLogic.Red);
		// You can now click the new game and the next button
		newGame.setEnabled(true);
		hint.setEnabled(true);
		// Set gameStarted boolean to true after new game starts
		gameStarted = true;
		// Repaint the board
		repaint();
	}

	public void paintComponent(Graphics boardElement) {
		// Paint a black border around the board
		boardElement.setColor(Color.BLACK);
		boardElement.fillRect(0, 0, 325, 325);

		// Creating a checkered pattern for the board
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				// Draw the checkered board
				if (row % 2 == col % 2)
					boardElement.setColor(Color.gray);
				else
					boardElement.setColor(Color.DARK_GRAY);
				boardElement.fillRect(squareOffset + col * squareSize, squareOffset + row * squareSize, squareSize,
						squareSize);

				// painting the pieces depending on to which player the square belongs to
				switch (boardLogic.tileStatus(row, col)) {
				// If the square is owned by the red player, place a red piece
				case MoveLogic.Red:
					boardElement.setColor(Color.RED);
					boardElement.fillOval(offset + col * squareSize, offset + row * squareSize, diameter, diameter);
					break;
				// If the square is owned by the black player, place a black piece
				case MoveLogic.Black:
					boardElement.setColor(Color.BLACK);
					boardElement.fillOval(offset + col * squareSize, offset + row * squareSize, diameter, diameter);
					break;
				// If the square is owned by the red player's King, place a red piece and draw
				// a "K"
				case MoveLogic.RedKing:
					boardElement.setColor(Color.RED);
					boardElement.fillOval(offset + col * squareSize, offset + row * squareSize, diameter, diameter);
					boardElement.setColor(Color.white);
					boardElement.drawString("K", kingOffsetCol + col * squareSize, kingOffsetRow + row * squareSize);
					break;
				// If the square is owned by the black player's King, place a black piece and
				// draw a "K"
				case MoveLogic.BlackKing:
					boardElement.setColor(Color.BLACK);
					boardElement.fillOval(offset + col * squareSize, offset + row * squareSize, diameter, diameter);
					boardElement.setColor(Color.white);
					boardElement.drawString("K", kingOffsetCol + col * squareSize, kingOffsetRow + row * squareSize);
					break;
				}
			}
		}

		// If game is in progress,
		if (gameStarted) {
			boardElement.setColor(Color.CYAN);
			// Highlight all legal moves for current player with a cyan box
			for (int i = 0; i < allMoves.length; i++) {
				boardElement.drawRect(squareOffset + allMoves[i].colFrom * squareSize,
						squareOffset + allMoves[i].rowFrom * squareSize, squareSize - 1, squareSize - 1);
			}

			// If there is a piece chosen for move,
			if (trackedRow >= 0) {
				// Highlight the selected piece with a white box
				boardElement.setColor(Color.white);
				boardElement.drawRect(squareOffset + trackedCol * squareSize, squareOffset + trackedRow * squareSize,
						squareSize - 1, squareSize - 1);
				// Highlight the legal moves the chosen piece can make with a green box
				boardElement.setColor(Color.green);
				for (int i = 0; i < allMoves.length; i++) {
					if (allMoves[i].colFrom == trackedCol && allMoves[i].rowFrom == trackedRow) {
						boardElement.drawRect(squareOffset + allMoves[i].colTo * squareSize,
								squareOffset + allMoves[i].rowTo * squareSize, squareSize - 1, squareSize - 1);
						// If clicked elsewhere, the square should be highlighted with a transparent box
					} 
//					else if (allMoves[i].rowFrom != trackedRow && allMoves[i].colFrom != trackedCol) {
//						int alpha = 127;
//						Color invisi = new Color(250, 255, 255, alpha);
//						boardElement.setColor(invisi);
//						boardElement.drawRect(squareOffset + trackedCol * squareSize,
//								squareOffset + trackedRow * squareSize, squareSize - 1, squareSize - 1);
//					}
				}
			}
		}
	}

	public void processLegalMoves(int row, int col) {
		try {
			if (gameStarted) {
				// Iterate through all legal moves
				for (int i = 0; i < allMoves.length; i++) {
					// If movable piece is clicked, track this piece's row and col values and
					// repaint
					if (allMoves[i].rowFrom == row && allMoves[i].colFrom == col) {
						trackedRow = row;
						trackedCol = col;
						// Repaint highlights the selected piece with a white box, legal moves with
						// green
						repaint();
					}
				}
				// If legally movable piece is selected, make the move happen
				for (int i = 0; i < allMoves.length; i++) {
					if (allMoves[i].rowFrom == trackedRow && allMoves[i].colFrom == trackedCol
							&& allMoves[i].rowTo == row && allMoves[i].colTo == col) {
						// calling method for making the move
						movePiece(allMoves[i]);
					}
				}
			}
			// Preventing exception that's thrown at the end of the game
		} catch (NullPointerException e) {

		}
	}

	// Making the move for the selected piece
	public void movePiece(Move move) {
		// Make the move in the logic portion of the code as well
		boardLogic.tileStatusChange(move.rowFrom, move.colFrom, move.rowTo, move.colTo);

		// For storing data of the first jump destination
		int jump1row;
		int jump1col;

		// If the move is a jump, check for another jump possible because double jumps
		// should be allowed.
		if (move.isJump()) {
			allMoves = boardLogic.legalJumps(whosTurn, move.rowTo, move.colTo);
			if (allMoves != null) {
				// trackedRow = move.rowTo;
				// trackedCol = move.colTo;
				jump1row = move.rowTo;
				jump1col = move.colTo;
				// TODO: Test Double jump
				allMoves = boardLogic.legalJumps(whosTurn, jump1row, jump1col);
				if (allMoves != null) {
					trackedRow = jump1row;
					trackedCol = jump1col;
					// repaint();
				}
				repaint();
			}
		}

		if (whosTurn == boardLogic.Red) {
			// If it was Red's turn, now it's Black's turn
			whosTurn = boardLogic.Black;
			// Get all legal moves for the Black pieces
			allMoves = boardLogic.legalMoves(whosTurn);
			if (allMoves == null) {
				gameOver();
			}
		} else {
			whosTurn = boardLogic.Red;
			// Get all legal moves for the red pieces
			allMoves = boardLogic.legalMoves(whosTurn);
			if (allMoves == null) {
				gameOver();
			}
		}
		// No squares are selected anymore
		trackedRow = -1;
		// Repaint the board
		repaint();
	}

	// If game is over, show a pop-up window indicating game is over and start a new
	// game on pop-up close
	public void gameOver() {
		JOptionPane.showMessageDialog(null, "Game Over!");
		newGame();
	}

//	public void actionPerformed(ActionEvent event) {
//		if (event.getSource() == newGame) {
//			lineNumber = 0;
//			newGame();
//		}
//	}

	@Override
	public void mouseClicked(MouseEvent clickEvent) {
		// Get the x and y coordinates of the clicked piece
		int col = (clickEvent.getX() - squareOffset) / squareSize;
		int row = (clickEvent.getY() - squareOffset) / squareSize;
		if (row < 8 && row >= 0 && col < 8 && col >= 0) {
			processLegalMoves(row, col);

		}
	}

	// Mouse event listeners not used
	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}
}