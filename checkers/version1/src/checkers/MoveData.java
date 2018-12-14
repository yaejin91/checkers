package checkers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

//MoveData should extend Jump Data
class MoveData {
	
	//Geting the tile numbers as an integer from the textfile
	public int[] getFromToTile(int lineNumber, String oneMove) {
		// Getting and splitting a parsed nth line from text file
		String[] parsedSplitLine = oneMove.split("-");
		// If there are more or less than 2 tiles selected, it's invalid
		if (parsedSplitLine.length != 2) {
			return null;
		}
		// Getting square number as strings
		int fromTile = Integer.parseInt(parsedSplitLine[0]);
		int toTile = Integer.parseInt(parsedSplitLine[1]);

		// Do a legal check on the square pair and if illegal, return null
		if (!isInBound(fromTile, toTile)) {
			return null;
		} else {
			// if legal,add the tile pair to the integer array that contains the numbers
			// of the original and destination tile
			int[] fromToTileNumber = new int[2];
			fromToTileNumber[0] = fromTile;
			fromToTileNumber[1] = toTile;

			// Return the array
			return fromToTileNumber;
		}
	}

	//Get the coordinates (row, col) of a tile when given its number
	public int[] getTileCoord(int tileNum) {
		// All of the row coordinates for tiles can be expressed with this formula
		int row = (tileNum - 1) / 4; // Even if the resulting number is a decimal, it will round down
		int col = 0;

		// All of the columns coordinates for tiles can be expressed with this formula
		// if row is even
		if (row % 2 == 0) {
			col = (tileNum - 1) % 4 * 2;
			// if row is odd
		} else if (row % 2 == 1) {
			col = (tileNum - 1) % 4 * 2 + 1;
		}

		// assign the row and col values as array elements
		int[] tileCoord = new int[2];
		tileCoord[0] = row;
		tileCoord[1] = col;

		// return coordinate array
		return tileCoord;
	}

	//Is the chosen tile in the right boundaries?
	public boolean isInBound(int fromTile, int toTile) {
		// If square number is out of bounds (not 1-32), move is invalid
		if ((fromTile < 1) || (toTile < 1) || (fromTile > 32) || (toTile > 32)) {
			return false;
		}
		return true;
	}
}