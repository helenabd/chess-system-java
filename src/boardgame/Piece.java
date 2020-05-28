package boardgame;

public abstract class Piece {

	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		//A posi��o de uma pe�a n�o criada vai ser inicialmente nula, n�o foi colocada no tabuleiro ainda
		this.position = null;
	}

	//Somente classes dentro do mesmo pacote e subclasses poderam acessar ao tabuleiro de uma pe�a
	//Tabuleiro de uso interno da camada de tabuleiro
	 protected Board getBoard() {
		return board;
	}
	
	// M�todo de opera��o abstrata para possiveis movimentos de cada pe�a, que ser� implementada dentro de cada pe�a espec�fica
	public abstract boolean[][] possibleMoves();
	
	//M�todo que recebe uma posi��o e que vai retornar verdadeiro ou falso se � poss�vel essa pe�a mover para essa dada posi��o
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];//utilizando o m�todo abstrato implementado acima
	}
	
	//M�todo para ver se existe pelo menos um movimento poss�vel na matriz chamada para determinada pe�a
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
