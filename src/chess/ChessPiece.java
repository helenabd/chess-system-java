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
	
	//M�todo que pega a posi��o que foi herdada da classe Piece e converte-la para ChessPosition
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	//M�todo para verificar se existe uma pe�a advers�ria na posi��o de destino
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p != null && p.getColor() != color;//testando se a cor da pe�a dessa posi��o � a cor diferente da cor da minha pe�a
	}
}
