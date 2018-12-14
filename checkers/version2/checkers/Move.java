package checkers;

//Use this as a type like int, String, etc.
public class Move {
	int rowFrom, colFrom, rowTo, colTo;

	// Constructor for a move made
	public Move(int fromRow, int fromCol, int toRow, int toCol) {
		rowFrom = fromRow;
		colFrom = fromCol;
		rowTo = toRow;
		colTo = toCol;
	}

	public boolean isJump() {
		// If the difference between origin and destination row is 1 or -1, it's a
		// non-jump move. Return false.
		if (rowFrom - rowTo == 2 || rowFrom - rowTo == -2) {
			return true;
		} else {
			return false;
		}
	}
}