package boardgame;

public class Piece {

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
	
	
}
