package checkers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParsedMovesFile {
	// This ArrayList will store parsed movement lines into an array
	static List<String> moves = new ArrayList<String>();

	// Parse file
	public static List<String> listOfMoves(String fileName) {
		// try and catch needed for filenotfoundException
		try {
			// Grab the file of movements I made
			File file = new File(fileName);
			// Get the absolute path for the file because I keep getting
			String fileAbsPath = file.getAbsolutePath();
			FileReader fileReader = new FileReader(fileAbsPath);
			Scanner fileScanner = new Scanner(fileReader);

			BufferedReader bufferedReader;
			bufferedReader = new BufferedReader(fileReader);
			// Create a new line number reader
			LineNumberReader lineNumReader = new LineNumberReader(fileReader);

			// Read the text file of moves line by line
			int lineCount = 0;

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				moves.add(line);
				lineCount++;
			}

			// Close the file after reading the file
			bufferedReader.close();
		}
		// In case file not found
		catch (FileNotFoundException except) {
			System.out.println("File Not Found!");
		}
		// for buffered reader
		catch (IOException except) {
			System.out.println("Error in buffered reading");
		}
		System.out.println(moves);
		return moves;
	}
	// Now check each element in the list array and see if the moves are valid

}