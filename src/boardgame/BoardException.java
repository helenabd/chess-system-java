package boardgame;

public class BoardException extends RuntimeException {
	private static final long serialVersionUID = 1L; //N�mero de vers�o

	public BoardException(String msg) { //Construtor que recebe e repassa a mensagem para o construtor da super classe
		super(msg);
	}
}
