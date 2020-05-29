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
		// Quem tem que saber a dimens�o de um tabuleiro de xadrez � a classe da partida
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

	// Retorna uma matriz de pe�as de xadrez correspondentes a essa partida
	public ChessPiece[][] getPieces() {
		// N�o quero liberar as pe�as do tipo Piece e sim as pe�as do m�todo ChessPiece
		// O programa s� vai conseguir enxergar a pe�a de xadrez e n�o a pe�a do
		// tabuleiro
		ChessPiece[][] mat = new ChessPiece[this.board.getRows()][this.board.getColumns()];// vari�vel auxiliar
		// Irei percorrer a matriz do meu tabuleiro e para cada pe�a irei fazer um
		// downcasting para ChessPiece, para interpretar como uma pe�a de xadrez e n�o
		// uma comum
		for (int i = 0; i < this.board.getRows(); i++) {
			for (int j = 0; j < this.board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) this.board.piece(i, j);
			}
		}
		return mat;
	}
	
	//M�todo para implementar os movimentos posiveis dada uma posi��o, para colorir o fundo de cada posi��o
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	//M�todo para validar o movimento das pe�as e capturas
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source); //Valida��o da posi��o de origem, se havia uma pe�a
		validateTargetPosition(source, target);//Valida��o da posi��o de destino
		Piece capturedPiece = makeMove(source, target);//Realizar o movimento da pe�a para captura
		nextTurn();//Para trocar o jogador
		return (ChessPiece) capturedPiece; //downcasting para converter a Piece em ChessPiece
	}
	
	//M�todo para realizar o movimento, recebendo a posi��o de origem e a posi��o de destino
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);//remove a pe�a da sua posi��o de origem
		Piece capturedPiece = this.board.removePiece(target);//remove a pe�a capturada, que estava na sua posi��o de destino
		this.board.placePiece(p, target);
		return capturedPiece;//retorna a pe�a capturada
	}
	
	//M�todo para validar se existe uma pe�a na posi��o informada
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
	
	//Validar se a posi��o de destino � valida em rela��o a posi��o de origem
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessExeption("The chosen piece can't move to target position");
		}
	}
	
	//M�todo para trocar o turno do jogador
	private void nextTurn() {
		turn++;
		//Troca de turno, se a cor atual for branca, troca para preto, se a posi��o atual n�o for branca troca para branco
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	// Esse m�todo vai receber as coordenadas do xadrez e converter para as posi��es do tabuleiro
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		this.board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

	// Respons�vel por iniciar a partida de xadrez, colocando as pe�as no tabuleiro
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
