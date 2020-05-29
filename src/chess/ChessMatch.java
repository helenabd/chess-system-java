package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;

	public ChessMatch() {
		// Quem tem que saber a dimensão de um tabuleiro de xadrez é a classe da partida
		// de xadrez
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
		nextTurn();//Para trocar o jogador
		return (ChessPiece) capturedPiece; //downcasting para converter a Piece em ChessPiece
	}
	
	//Método para realizar o movimento, recebendo a posição de origem e a posição de destino
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);//remove a peça da sua posição de origem
		Piece capturedPiece = this.board.removePiece(target);//remove a peça capturada, que estava na sua posição de destino
		this.board.placePiece(p, target);
		return capturedPiece;//retorna a peça capturada
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
	
	// Esse método vai receber as coordenadas do xadrez e converter para as posições do tabuleiro
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		this.board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

	// Responsável por iniciar a partida de xadrez, colocando as peças no tabuleiro
	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
