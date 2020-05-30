package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<Piece>();
	private List<Piece> capturedPieces = new ArrayList<Piece>();

	public ChessMatch() {
		// Quem tem que saber a dimensão de um tabuleiro de xadrez é a classe da partida de xadrez
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();// iniciar a partida
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}

	// Retorna uma matriz de peças de xadrez correspondentes a essa partida
	public ChessPiece[][] getPieces() {
		// Não quero liberar as peças do tipo Piece e sim as peças do método ChessPiece
		// O programa só vai conseguir enxergar a peça de xadrez e não a peça do
		// tabuleiro
		ChessPiece[][] mat = new ChessPiece[this.board.getRows()][this.board.getColumns()];// variável auxiliar
		// Irei percorrer a matriz do meu tabuleiro e para cada peça irei fazer um
		// downcasting para ChessPiece, para interpretar como uma peça de xadrez e não
		// uma comum
		for (int i = 0; i < this.board.getRows(); i++) {
			for (int j = 0; j < this.board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) this.board.piece(i, j);
			}
		}
		return mat;
	}
	
	//Método para implementar os movimentos posiveis dada uma posição, para colorir o fundo de cada posição
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	//Método para validar o movimento das peças e capturas
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source); //Validação da posição de origem, se havia uma peça
		validateTargetPosition(source, target);//Validação da posição de destino
		Piece capturedPiece = makeMove(source, target);//Realizar o movimento da peça para captura
		
		//Testar se esse movimento colocou o próprio jogador em xeque
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessExeption("You can't put yourself in check");
		}
		
		//Testar se o oponente ficou em xeque
		check = (testCheck(opponent(currentPlayer))) ? true : false;	
		
		//testar se a jogada que eu fiz deixou meu oponente em xeque mate, se sim o jogo termina
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();//Para trocar o jogador
		}
		
		return (ChessPiece) capturedPiece; //downcasting para converter a Piece em ChessPiece
	}
	
	//Método para realizar o movimento, recebendo a posição de origem e a posição de destino
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);//remove a peça da sua posição de origem
		p.increaseMoveCount();
		Piece capturedPiece = this.board.removePiece(target);//remove a peça capturada, que estava na sua posição de destino
		this.board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		return capturedPiece;//retorna a peça capturada
	}
	
	//Método para desfazer um movimento
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target); //tirar a peça que moveu do destino e devolve-lá para posição de origem
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		//Se tiver sido capturada uma peça, tenho que voltar ela para o tabuleiro na posição de destino
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			//Tenho que tirar essa peça da lista de peças capturadas e coloca-lá novamente na lista de peças do tabuleiro
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
	
	
	//Método para validar se existe uma peça na posição informada
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessExeption("There is no piece on source position");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessExeption("The chosen piece is not yours");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessExeption("There is no possible moves for the chosen piece");
		}
	}
	
	//Validar se a posição de destino é valida em relação a posição de origem
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessExeption("The chosen piece can't move to target position");
		}
	}
	
	//Método para trocar o turno do jogador
	private void nextTurn() {
		turn++;
		//Troca de turno, se a cor atual for branca, troca para preto, se a posição atual não for branca troca para branco
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	//Método para devolver o oponente da cor
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	//Método para localizar o rei de uma determinada cor, varrer as peças do jogo para localizar o rei
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			if(p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("There is no" + color + " king on the board");
	}
	
	//Método para testar se o rei de uma determinada cor está em xeque
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition(); //pego a posição do meu rei em formato de matriz
		//peças no tabuleiro filtradas com a cor do oponente desse rei
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		//peguei a lista de peças do oponente e para cada peça contida nessa lista, eu vou ter que testar
		//se existe algum movimento possível que leva a posição do meu rei
		for(Piece p : opponentPieces) {
			//matriz de movimentos possíveis dessa peça adversária p, se nessa matriz a posição correspondente
			//a posição do rei for true, significa que o rei está em xeque
			boolean[][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {//se verdadeiro, o rei está em xeque
				return true;
			}
		}
		return false;
	}
	
	//Método para testar se ocorreu xeque mate
	private boolean testCheckMate(Color color) {
		//teste para eliminar a possibilidade do rei não estar em cheque
		if(!testCheck(color)) {
			return false;
		}
		//testar todas as peças da cor informada se não tiverem um movimento possível para ela que tire o rei do xeque, esta em xeque mate
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		//irei percorrer a lista e ver se existe alguma peça que tira o rei do xeque
		for(Piece p : list) {
			boolean[][] mat = p.possibleMoves();//ver os movimentos possíveis dessa peça p
			for(int i=0; i<board.getRows(); i++) {
				for(int j=0; j<board.getColumns(); j++) {
					if(mat[i][j]) {
						//testar se essa posição tira do xeque
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);//fiz o movimento da peça na posição de origem para posição de destino
						boolean testCheck = testCheck(color);//testar se o rei da minha cor ainda está em xeque
						undoMove(source, target, capturedPiece);//para desfazer o movimento depois do teste
						if(!testCheck) {//se não estava em xeque esse movimento tirou o meu rei do xeque
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	// Esse método vai receber as coordenadas do xadrez e converter para as posições do tabuleiro
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		this.board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);//adicionar peças do tabuleiro na lista
	}

	// Responsável por iniciar a partida de xadrez, colocando as peças no tabuleiro
	private void initialSetup() {
		placeNewPiece('h', 7, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));

		placeNewPiece('b', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 8, new King(board, Color.BLACK));
	}
}
