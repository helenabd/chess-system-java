package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessExeption;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		ChessMatch chessMatch = new ChessMatch();
		
		while(true) {
			try {
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces());
				System.out.println();
				System.out.println();
				System.out.print("Source: ");//solicitar ao usuário a posição de origem
				ChessPosition source = UI.readChessPosition(sc);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves);
				
				System.out.println();
				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);//solicitar ao usuário a posição de destino
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
			}
			catch (ChessExeption e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		
	}

}
