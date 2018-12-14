package checkers;

class MoveLegalCheck {
	public boolean isMoveLegal(int pieceType, int fromRow, int fromCol, int rowTo, int colTo, int boardData) {

		// If move is out of bounds, it's illegal
		if (isInBounds(fromRow, fromCol, rowTo, colTo))
			return false;

		// If move is made to an non-empty tile, it's illegal
		if (isTileEmpty(rowTo, colTo, boardData))
			return false;

		// Are the pieces moving in the right direction?
		if (isMovingRightDirection(fromRow, fromCol, rowTo, colTo, pieceType, boardData)) {
			return false;
		}
		// If move is made to the wrong direction (up or down), it's illegal
		// Since red starts at the lower 3 rows, unless it's a king, it cannot move back
		// down. Moving down means destination row is bigger than the origin row
		if (pieceType == MoveLogic.Red) {
			if (boardData == MoveLogic.Red && rowTo > fromRow)
				return false;
			// Otherwise, return true
			return true;
			// non-king black pieces cannot move back up.
		} else {
			if (boardData == MoveLogic.Black && rowTo < fromRow)
				return false;
			// Otherwise, return true
			return true;
		}
	}

	public boolean isJumpLegal(int pieceType, int fromRow, int fromCol, int jumpedRow, int jumpedCol, int rowTo,
			int colTo, int boardData) {

		// If move is out of bounds, it's illegal
		if (isInBounds(fromRow, fromCol, rowTo, colTo))
			return false;

		// If move is made to an non-empty tile, it's illegal
		if (isTileEmpty(rowTo, colTo, boardData))
			return false;

		// Are the pieces moving in the right direction?
		if (isMovingRightDirection(fromRow, fromCol, rowTo, colTo, pieceType, boardData)) {
			return false;
		}

		// If move is made to the wrong direction (up or down), it's illegal
		if (pieceType == MoveLogic.Red) {
			if (boardData == MoveLogic.Red && rowTo > fromRow)
				return false;
			// You can't jump over your own piece
			if (boardData != MoveLogic.Black && boardData != MoveLogic.BlackKing)
				return false;
			// If jump is true, red killed a piece so count up
			return true;
		} else {
			// If move is made to the wrong direction (up or down), it's illegal
			if (boardData == MoveLogic.Black && rowTo < fromRow)
				return false;
			// You can't jump over your own piece
			if (boardData != MoveLogic.Red && boardData != MoveLogic.RedKing)
				return false;
			// If jump is true, black killed a piece so count up
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
	private boolean isTileEmpty(int toRow, int toCol, int boardData) {
		if (boardData != MoveLogic.Neutral) {
			System.out.println("destination non-empty");
			return false;
		} else {
			return true;
		}
	}

	private boolean isMovingRightDirection(int fromRow, int fromCol, int toRow, int toCol, int pieceType,
			int boardData) {
		// Since red starts at the lower 3 rows, unless it's a king, it cannot move back
		// down. Moving down means destination row is bigger than the origin row
		if (pieceType == MoveLogic.Red) {
			if ((toRow > fromRow) && (boardData == MoveLogic.Red)) {
				System.out.println("moving backwards");
				return false;
			}
			// But other piece types can move down
			return true;
		} else {
			// non-king black pieces cannot move back up.
			if ((toRow < fromRow) && (boardData == MoveLogic.Black)) {
				System.out.println("moving backwards");
				return false;
			}
			return true;
		}
	}

}