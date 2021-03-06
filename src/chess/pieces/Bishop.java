package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Bishop extends ChessPiece {

	public Bishop(Board board, Color color) {
		super(board, color);
	}
		
	@Override
	public String toString() {
		return "B";
	}
		
	@Override
	public boolean[][] possibleMoves() {
		// criei uma matriz de booleanos da mesma dimens�o do tabuleiro
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

		Position p = new Position(0, 0);// posi��o auxiliar somente para se ter um valor inicial

		// PARA NOROESTE DA MINHA PE�A
		p.setValues(position.getRow() - 1, position.getColumn() - 1);// linha acima e a esquerda da minha pe�a

		// enquanto a posi��o p existir e n�o tiver uma pe�a l�, ou seja, enquanto a
		// posi��o estiver vaga eu vou marca-la como verdadeira

		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() - 1);//verificar na diagonal noroeste
		}
		// verificar se a proxima casa � ocupada por uma pe�a advers�ria
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// PARA NORDESTE DA MINHA PE�A
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() + 1);//verificar na diagonal nordeste
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// PARA SUDESTE DA MINHA PE�A
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() + 1);//verificar na diagonal sudeste
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// PARA SUDOESTE DA MINHA PE�A
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() - 1);//para verificar na diagonal sudoeste
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		return mat;
	}
}
