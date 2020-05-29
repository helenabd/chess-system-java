package application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class UI {

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	// C�digos especiais das cores para imprimir no console
	// Cores do texto
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	// Cores do fundo
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	
	// https://stackoverflow.com/questions/2979383/java-clear-the-console
	//Limpar a tela
	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	// Vai receber a leitura da posi��o utilizando o scanner instanciado no programa principal
	public static ChessPosition readChessPosition(Scanner sc) {
		try {
			String s = sc.nextLine(); //Vai ler a posi��o como uma string
			char column = s.charAt(0); //A coluna da posi��o de xadrez � o primeiro caracter da minha string
			int row = Integer.parseInt(s.substring(1)); //Eu vou recortar a String a partir da posi��o 1 e converter o resultado para inteiro
			return new ChessPosition(column, row);
		}
		catch (RuntimeException e) {
			throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8.");
		}
	}
	
	//M�todo para imprimir a partida
	public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
		printBoard(chessMatch.getPieces());
		System.out.println();
		System.out.println();
		printCapturedPieces(captured);
		System.out.println();
		System.out.println("Turn: " + chessMatch.getTurn());
		System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());
		
		//Acrescentar a informa��o xeque caso seja necess�ria
		if(chessMatch.getCheck()) {
			System.out.println("CHECK!");
		}
		
	}

	// Recebe a matriz de pe�as da minha partida, para imprimir as pe�as no tabuleiro
	public static void printBoard(ChessPiece[][] pieces) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], false);
			}
			System.out.println();
		}
		System.out.print("  a b c d e f g h");
	}
	
	public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], possibleMoves[i][j]);
			}
			System.out.println();
		}
		System.out.print("  a b c d e f g h");
	}

	// Imprimir uma �nica pe�a do tabuleiro
	private static void printPiece(ChessPiece piece, boolean background) {
		if(background) {
			System.out.print(ANSI_BLUE_BACKGROUND);//imprimir o fundo da pe�a
		}
		if (piece == null) {
			System.out.print("-" + ANSI_RESET);
		} else {
			if (piece.getColor() == Color.WHITE) {
				System.out.print(ANSI_WHITE + piece + ANSI_RESET);
			} else {
				System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
			}
		}
		System.out.print(" ");
	}
	
	//M�todo para receber uma lista de pe�as de xadrez e vai ser respons�vel por imprimir na tela as pe�as capturadas
	private static void printCapturedPieces(List<ChessPiece> captured) {
		//Filtrando na minha lista todas as pe�as cuja a cor � branca
		List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE).collect(Collectors.toList());
		//Filtrando na minha lista todas as pe�as cuja a cor � preta
		List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK).collect(Collectors.toList());
		System.out.println("Captured pieces: ");
		System.out.print("White: ");
		System.out.print(ANSI_WHITE);
		System.out.println(Arrays.toString(white.toArray()));//Jeito padr�o de imprimir um array de valores
		System.out.print(ANSI_RESET);
		System.out.print("Black: ");
		System.out.print(ANSI_YELLOW);
		System.out.println(Arrays.toString(black.toArray()));//Jeito padr�o de imprimir um array de valores
		System.out.print(ANSI_RESET);
	}
}
