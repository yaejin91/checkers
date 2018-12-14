package checkers;

class MoveLogic {

	// 5 types of status a board square can have, but only one each time.
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

	int[][] boardData;

	public MoveLogic() {
		newBoard();
	}

	//Initial setup for data of the board
	public void newBoard() {
		boardData = new int[8][8];

		// Taking all 64 tiles and giving them default values
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				// Assign the numbered tiles to players
				if (row % 2 == col % 2) {
					// Assign the top 3 rows to black
					if (row < 3) {
						boardData[row][col] = Black;
						// Assign the bottom 3 rows to red
					} else if (row > 4) {
						boardData[row][col] = Red;
						// The remaining tiles are blank
					} else {
						boardData[row][col] = Neutral;
					}
					// The remaining tiles are
				} else {
					boardData[row][col] = Neutral;
				}
			}
		}
	}

	// Method where tile statuses change from a move, jump, or being a king
	public void tileStatusChange(int fromRow, int fromCol, int toRow, int toCol) {
		// When piece arrives at its destination tile, change destination tile to that
		// piece's color integer
		boardData[toRow][toCol] = boardData[fromRow][fromCol];
		// Empty the old origin tile
		boardData[fromRow][fromCol] = Neutral;

		// If the tile contained a jumped piece and has been jumped,
		if (fromRow - toRow == 2 || fromRow - toRow == -2) {
			int jumpedRow = (toRow + fromRow) / 2;
			int jumpedCol = (toCol + fromCol) / 2;
			// the tile is now neutral
			boardData[jumpedRow][jumpedCol] = Neutral;
		}

		// King declaration for Red, if Red reaches the topmost row
		if (toRow == 0 && boardData[toRow][toCol] == Red) {
			boardData[toRow][toCol] = RedKing;
		}
		// King declaration for Black, if Black reaches the bottom most row
		if (toRow == 7 && boardData[toRow][toCol] == Black) {
			boardData[toRow][toCol] = BlackKing;
		}
	}

	// return who owns the square (who has their piece on it)
	// It will return 1-5 depending on who's piece is on the tile
	public int tileStatus(int row, int col) {
		return boardData[row][col];
	}

	// processing legalMoves
	public int[] nextMove(int fromRow, int fromCol, int toRow, int toCol, int pieceType) {
		// Get who the current king is if the chosen piece is a king
		int moveArray[] = new int[2];

		// If the move is a non-jump move,
		if ((fromRow - toRow == 2 || fromRow - toRow == -2) == false) {
			// check if the move is legal
			if (isMoveLegal(fromRow, fromCol, toRow, toCol, pieceType) == true) {
				// if legal, store the next move row and col into an array
				moveArray[0] = toRow;
				moveArray[1] = toCol;
			} else {
				return null;
			}
			// if the move is a jump move,
		} else {
			// check if the jump is legal
			int jumpedRow = (fromRow + toRow) / 2;
			int jumpedCol = (fromCol + toCol) / 2;
			// if legal, store the next move row and col into an array
			if (isJumpLegal(fromRow, fromCol, jumpedRow, jumpedCol, toRow, toCol, pieceType) == true) {
				moveArray[0] = toRow;
				moveArray[1] = toCol;
			} else {
				return null;
			}
		}
		return moveArray;
	}

	public boolean isMoveLegal(int fromRow, int fromCol, int toRow, int toCol, int pieceType) {
		// If move is out of bounds, it's illegal
		if (isInBounds(fromRow, fromCol, toRow, toCol) == false) {
			return false;
		}
		// If move is made to an non-empty tile, it's illegal
		if (isTileEmpty(toRow, toCol) == false) {
			return false;
		}
		// If move is made to the wrong direction (up or down), it's illegal
		if (isMovingRightDirection(fromRow, fromCol, toRow, toCol, pieceType) == false) {
			return false;
		}
		// If a move (non-jump) does not consist of 1 move each from col and row, then
		// it's illegal
		if (fromRow - toRow > 1 || fromRow - toRow < -1) {
			System.out.println("move too big/too small");
			return false;
		}
		// Otherwise, return true
		return true;
	}

	public boolean isJumpLegal(int fromRow, int fromCol, int jumpedRow, int jumpedCol, int toRow, int toCol,
			int pieceType) {
		// If move is out of bounds, it's illegal
		if (isInBounds(fromRow, fromCol, toRow, toCol) == false) {
			return false;
		}
		// If move is made to an non-empty tile, it's illegal
		if (isTileEmpty(toRow, toCol) == false) {
			return false;
		}
		// If move is made to the wrong direction (up or down), it's illegal
		if (isMovingRightDirection(fromRow, fromCol, toRow, toCol, pieceType) == false) {
			return false;
		}
		// If a move (non-jump) does not consist of 1 move each from col and row, then
		// it's illegal
		if (fromRow - toRow > 2 || fromRow - toRow < -2 || (fromRow - toRow == 0 && fromCol - toCol == 0)) {
			System.out.println("jump too big/too small");
			return false;
		}

		if (pieceType == Red) {
			// You can't jump over your own piece
			if (boardData[jumpedRow][jumpedCol] != Black && boardData[jumpedRow][jumpedCol] != BlackKing) {
				return false;
			}
			// If jump is true, red killed a piece so count up
			blackKillCounter = blackKillCounter + 1;
			System.out.println("blackKillCounter: " + blackKillCounter);

			// In other cases, jump is legal for Red
			return true;
		} else {
			// You can't jump over your own piece
			if (boardData[jumpedRow][jumpedCol] != Red && boardData[jumpedRow][jumpedCol] != RedKing) {
				return false;
			}
			// If jump is true, black killed a piece so count up
			redKillCounter = redKillCounter + 1;
			System.out.println("redKillCounter: " + redKillCounter);
			// In other cases, jump is legal
			return true;
		}
	}

	// A move is out of bounds if the row and col values are not in the board
	private boolean isInBounds(int fromRow, int fromCol, int toRow, int toCol) {
		if (toRow < 0 || toCol < 0 || toRow >= 8 || toCol >= 8) {
			System.out.println("bounds");
			return false;
		} else {
			return true;
		}
	}

	// A move cannot be made if the destination tile is occupied
	private boolean isTileEmpty(int toRow, int toCol) {
		if (boardData[toRow][toCol] != Neutral) {
			System.out.println("destination non-empty");
			return false;
		} else {
			return true;
		}
	}

	private boolean isMovingRightDirection(int fromRow, int fromCol, int toRow, int toCol, int pieceType) {
		// Since red starts at the lower 3 rows, unless it's a king, it cannot move back
		// down. Moving down means destination row is bigger than the origin row
		if (pieceType == Red) {
			if ((toRow > fromRow) && (boardData[fromRow][fromCol] == Red)) {
				System.out.println("moving backwards");
				
				return false;
			}
			// But other piece types can move down
			return true;
		} else {
			// non-king black pieces cannot move back up.
			if ((toRow < fromRow) && (boardData[fromRow][fromCol] == Black)) {
				System.out.println("moving backwards");
				return false;
			}
			return true;
		}
	}
}