package checkers;

import java.util.ArrayList;

class MoveLogic {
	// 5 types of status a boardData square can have, but only one each time.
	// Neutral means there's nothing on the square
	// I chose int instead of string because string leaves room for errors due to
	// typos
	public static final int Neutral = 0;
	public static final int Red = 1;
	public static final int RedKing = 2;
	public static final int Black = 3;
	public static final int BlackKing = 4;

	int redKillCounter = 0;
	int blackKillCounter = 0;

	private int[][] boardData;

	MoveLegalCheck legalCheck;

	public MoveLogic() {
		boardData = new int[8][8];
		newBoard();
	}

	// Initial setup for data of the board
	public void newBoard() {
		// Taking all 64 tiles and giving them default values
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				// Assign the numbered tiles to players
				if (row % 2 == col % 2) {
					// Assign the top 3 rows to black
					if (row < 3)
						boardData[row][col] = Black;
					// Assign the bottom 3 rows to red
					else if (row > 4)
						boardData[row][col] = Red;
					// The remaining tiles are blank
					else
						boardData[row][col] = Neutral;
				} else
					boardData[row][col] = Neutral;
			}
		}
	}

	// Method where tile statuses change from a move, jump, or being a king
	public void tileStatusChange(int rowFrom, int colFrom, int rowTo, int colTo) {
		// When piece arrives at its destination tile, change destination tile to that
		// piece's color integer
		boardData[rowTo][colTo] = boardData[rowFrom][colFrom];
		// Empty the old origin tile
		boardData[rowFrom][colFrom] = Neutral;

		// If the tile contained a jumped piece and has been jumped,
		if (rowFrom - rowTo == 2 || rowFrom - rowTo == -2) {
			int jumpRow = (rowFrom + rowTo) / 2;
			int jumpCol = (colFrom + colTo) / 2;
			// the tile is now neutral
			boardData[jumpRow][jumpCol] = Neutral;
		}
		// King declaration for Red, if Red reaches the topmost row
		if (rowTo == 0 && boardData[rowTo][colTo] == Red) {
			boardData[rowTo][colTo] = RedKing;
		}
		// King declaration for Black, if Black reaches the bottom most row
		if (rowTo == 7 && boardData[rowTo][colTo] == Black) {
			boardData[rowTo][colTo] = BlackKing;
		}
	}

	public Move[] legalJumps(int player, int row, int col) {
		// New array list to store legal jumps throughout this method
		ArrayList potentialJumps = scanLegalJumps(player, row, col);

		// If there are no legal jumps that can be made, return null
		if (potentialJumps.size() == 0) {
			return null;
		} else {
			// If legal jumps array is not null, store them in an array and return the array
			Move[] moveArray = new Move[potentialJumps.size()];
			for (int i = 0; i < potentialJumps.size(); i++) {
				moveArray[i] = (Move) potentialJumps.get(i);
			}
			return moveArray;
		}
	}

	public Move[] legalMoves(int player) {
		if (player != Red && player != Black)
			return null;

		int playerKing = findKingColor(player);

		ArrayList potentialMoves = new ArrayList();

		// Check all pieces for any jumps
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				// Check and add any legal jumps first because if there is a jump available,
				// jump must be made.
				if (boardData[row][col] == player || boardData[row][col] == playerKing) {
					if (isJumpLegal(player, row, col, row + 1, col + 1, row + 2, col + 2)) {
						// If legal, add to legalJumps array
						potentialMoves.add(new Move(row, col, row + 2, col + 2));
						scanLegalJumps(player, row + 2, col + 2);
					}				
					// Check the tile on downward left diagonal of the current tile for possible
					// jump
					if (isJumpLegal(player, row, col, row - 1, col + 1, row - 2, col + 2)) {
						// If legal, add to legalJumps array
						potentialMoves.add(new Move(row, col, row - 2, col + 2));
						scanLegalJumps(player, row - 2, col + 2);
					}
					// Check the tile on upward right diagonal of the current tile for possible jump
					if (isJumpLegal(player, row, col, row + 1, col - 1, row + 2, col - 2)) {
						// If legal, add to legalJumps array
						potentialMoves.add(new Move(row, col, row + 2, col - 2));
						scanLegalJumps(player, row + 2, col - 2);
					}
					// Check the tile on upward left diagonal of the current tile for possible jump
					if (isJumpLegal(player, row, col, row - 1, col - 1, row - 2, col - 2)) {
						// If legal, add to legalJumps array
						potentialMoves.add(new Move(row, col, row - 2, col - 2));
						scanLegalJumps(player, row - 2, col - 2);
					}				
				}
			}
		}
		// If there are no jumps that can be made, check for legal moves for all pieces
		// of current player
		if (potentialMoves.size() == 0) {
			// Check all pieces for moves
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					if (boardData[row][col] == player || boardData[row][col] == playerKing) {
						// Check tile downward diagonal right
						if (isMoveLegal(player, row, col, row + 1, col + 1))
							potentialMoves.add(new Move(row, col, row + 1, col + 1));
						// Check tile downward diagonal left
						if (isMoveLegal(player, row, col, row - 1, col + 1))
							potentialMoves.add(new Move(row, col, row - 1, col + 1));
						// Check tile upward diagonal right
						if (isMoveLegal(player, row, col, row + 1, col - 1))
							potentialMoves.add(new Move(row, col, row + 1, col - 1));
						// Check tile upward diagonal left
						if (isMoveLegal(player, row, col, row - 1, col - 1))
							potentialMoves.add(new Move(row, col, row - 1, col - 1));
					}
				}
			}
		}
		// if legalMoves array is still null, no moves can be made
		if (potentialMoves.size() == 0) {
			return null;
		} else {
			Move[] moveArray = new Move[potentialMoves.size()];
			for (int i = 0; i < potentialMoves.size(); i++) {
				moveArray[i] = (Move) potentialMoves.get(i);
			}
			return moveArray;
		}

	}

	public ArrayList scanLegalJumps(int player, int row, int col) {
		if (player != Red && player != Black)
			return null;

		int playerKing = findKingColor(player);

		// New array list to store legal jumps throughout this method
		ArrayList potentialJumps = new ArrayList();

		if (boardData[row][col] == player || boardData[row][col] == playerKing) {
			// Check the tile on downward right diagonal of the current tile for possible
			// jump
			if (isJumpLegal(player, row, col, row + 1, col + 1, row + 2, col + 2)) {
				potentialJumps.add(new Move(row, col, row + 2, col + 2));
				scanLegalJumps(player, row + 2, col + 2);
			}

			// Check the tile on downward left diagonal of the current tile for possible
			// jump
			if (isJumpLegal(player, row, col, row - 1, col + 1, row - 2, col + 2)) {
				potentialJumps.add(new Move(row, col, row - 2, col + 2));
				scanLegalJumps(player, row - 2, col + 2);
			}

			// Check the tile on upward right diagonal of the current tile for possible jump
			if (isJumpLegal(player, row, col, row + 1, col - 1, row + 2, col - 2)) {
				potentialJumps.add(new Move(row, col, row + 2, col - 2));
				scanLegalJumps(player, row + 2, col - 2);

			}
			// Check the tile on upward left diagonal of the current tile for possible jump
			if (isJumpLegal(player, row, col, row - 1, col - 1, row - 2, col - 2)) {
				potentialJumps.add(new Move(row, col, row - 2, col - 2));
				scanLegalJumps(player, row - 2, col - 2);
			}
		}
		return potentialJumps;
	}

	private boolean isMoveLegal(int pieceType, int fromRow, int fromCol, int rowTo, int colTo) {

		// If move is out of bounds, it's illegal
		if (rowTo < 0 || rowTo >= 8 || colTo < 0 || colTo >= 8)
			return false;
		// If move is made to an non-empty tile, it's illegal

		if (boardData[rowTo][colTo] != Neutral)
			return false;
		// If move is made to the wrong direction (up or down), it's illegal
		// Since red starts at the lower 3 rows, unless it's a king, it cannot move back
		// down. Moving down means destination row is bigger than the origin row
		if (pieceType == Red) {
			if (boardData[fromRow][fromCol] == Red && rowTo > fromRow)
				return false;
			// Otherwise, return true
			return true;
			// non-king black pieces cannot move back up.
		} else {
			if (boardData[fromRow][fromCol] == Black && rowTo < fromRow)
				return false;
			// Otherwise, return true
			return true;
		}
	}

	private boolean isJumpLegal(int pieceType, int fromRow, int fromCol, int jumpedRow, int jumpedCol, int toRow,
			int toCol) {
		// If move is out of bounds, it's illegal
		if (toRow < 0 || toRow >= 8 || toCol < 0 || toCol >= 8)
			return false;

		// If move is made to an non-empty tile, it's illegal
		if (boardData[toRow][toCol] != Neutral)
			return false;

		// If move is made to the wrong direction (up or down), it's illegal
		if (pieceType == Red) {
			if (boardData[fromRow][fromCol] == Red && toRow > fromRow)
				return false;
			// You can't jump over your own piece
			if (boardData[jumpedRow][jumpedCol] != Black && boardData[jumpedRow][jumpedCol] != BlackKing)
				return false;
			// If jump is true, red killed a piece so count up
			blackKillCounter = blackKillCounter + 1;
			return true;
		} else {
			// If move is made to the wrong direction (up or down), it's illegal
			if (boardData[fromRow][fromCol] == Black && toRow < fromRow)
				return false;
			// You can't jump over your own piece
			if (boardData[jumpedRow][jumpedCol] != Red && boardData[jumpedRow][jumpedCol] != RedKing)
				return false;
			// If jump is true, black killed a piece so count up
			redKillCounter = redKillCounter + 1;
			// In other cases, jump is legal
			return true;
		}
	}

	// return who owns the square (who has their piece on it)
	// It will return 1-5 depending on who's piece is on the tile
	public int tileStatus(int row, int col) {
		return boardData[row][col];
	}

	// Return who's king it is depending on who's turn
	public int findKingColor(int player) {
		int playerKing;

		if (player == Red) {
			playerKing = RedKing;
		} else {
			playerKing = BlackKing;
		}
		return playerKing;
	}

}