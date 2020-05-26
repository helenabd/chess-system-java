package boardgame;

public class Piece {

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
	
	
}
