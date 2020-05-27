package chess;

import boardgame.Board;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	
	public ChessMatch() {
		//Quem tem que saber a dimens�o de um tabuleiro de xadrez � a classe da partida de xadrez
		board = new Board(8, 8);
		initialSetup();//iniciar a partida
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
	
	//Esse m�todo vai receber as coordenadas do xadrez e converter para as posi��es do tabuleiro
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		this.board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	//Respons�vel por iniciar a partida de xadrez, colocando as pe�as no tabuleiro
	private void initialSetup() {
		placeNewPiece('b', 6, new Rook(board, Color.WHITE));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
	}
}
