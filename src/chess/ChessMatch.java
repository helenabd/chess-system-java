package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	
	public ChessMatch() {
		//Quem tem que saber a dimensão de um tabuleiro de xadrez é a classe da partida de xadrez
		board = new Board(8, 8);
		initialSetup();
	}
	
	//Retorna uma matriz de peças de xadrez correspondentes a essa partida
	public ChessPiece[][] getPieces() {
		//Não quero liberar as peças do tipo Piece e sim as peças do método ChessPiece
		//O programa só vai conseguir enxergar a peça de xadrez e não a peça do tabuleiro
		ChessPiece [][] mat = new ChessPiece[this.board.getRows()][this.board.getColumns()];//variável auxiliar
		//Irei percorrer a matriz do meu tabuleiro e para cada peça irei fazer um downcasting para ChessPiece, para interpretar como uma peça de xadrez e não uma comum
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
