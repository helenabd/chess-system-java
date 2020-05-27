package chess;

import boardgame.Position;

public class ChessPosition {

	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
	//Programação defensiva, se a coluna for menor que o caracter a e maior que o caracter h
		if(column < 'a' || column > 'h' || row <1 || row > 8) {
			throw new ChessExeption("Error instantiating ChessPosition. Valid values are from a1 to h8.");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return this.column;
	}

	public int getRow() {
		return this.row;
	}
	
	//Método para converter a ChessPosition para uma Position
	protected Position toPosition() {
		return new Position(8 - this.row, this.column - 'a');
	}
	
	//Método para converter uma Position para uma ChessPosition
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' - position.getColumn()), 8 - position.getRow());
	}
	
	@Override
	public String toString() {
		return "" + this.column + this.row;
	}
}
