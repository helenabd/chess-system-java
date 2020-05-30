package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {

	private Color color;
	private int moveCount;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public void increaseMoveCount() {
		moveCount++;
	}
	
	public void decreaseMoveCount() {
		moveCount--;
	}
	
	//Método que pega a posição que foi herdada da classe Piece e converte-la para ChessPosition
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	//Método para verificar se existe uma peça adversária na posição de destino
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p != null && p.getColor() != color;//testando se a cor da peça dessa posição é a cor diferente da cor da minha peça
	}
}
