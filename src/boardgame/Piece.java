package boardgame;

public abstract class Piece {

	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		//A posição de uma peça não criada vai ser inicialmente nula, não foi colocada no tabuleiro ainda
		this.position = null;
	}

	//Somente classes dentro do mesmo pacote e subclasses poderam acessar ao tabuleiro de uma peça
	//Tabuleiro de uso interno da camada de tabuleiro
	 protected Board getBoard() {
		return board;
	}
	
	// Método de operação abstrata para possiveis movimentos de cada peça, que será implementada dentro de cada peça específica
	public abstract boolean[][] possibleMoves();
	
	//Método que recebe uma posição e que vai retornar verdadeiro ou falso se é possível essa peça mover para essa dada posição
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];//utilizando o método abstrato implementado acima
	}
	
	//Método para ver se existe pelo menos um movimento possível na matriz chamada para determinada peça
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		//percorrer a matriz chamada acima
		for(int i=0; i<mat.length; i++) {
			for(int j=0; j<mat.length; j++) {
				if(mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
}
