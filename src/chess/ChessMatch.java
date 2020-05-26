package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	
	public ChessMatch() {
		//Quem tem que saber a dimens�o de um tabuleiro de xadrez � a classe da partida de xadrez
		board = new Board(8, 8);
		initialSetup();
	}
	
	//Retorna uma matriz de pe�as de xadrez correspondentes a essa partida
	public ChessPiece[][] getPieces() {
		//N�o quero liberar as pe�as do tipo Piece e sim as pe�as do m�todo ChessPiece
		//O programa s� vai conseguir enxergar a pe�a de xadrez e n�o a pe�a do tabuleiro
		ChessPiece [][] mat = new ChessPiece[this.board.getRows()][this.board.getColumns()];//vari�vel auxiliar
		//Irei percorrer a matriz do meu tabuleiro e para cada pe�a irei fazer um downcasting para ChessPiece, para interpretar como uma pe�a de xadrez e n�o uma comum
		for(int i=0; i<this.board.getRows(); i++) {
			for(int j=0; j<this.board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) this.board.piece(i, j);
			}
		}
		return mat;
	}
	
	private void initialSetup() {
		board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));
		board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
		board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
	}
}
