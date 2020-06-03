package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<Piece>();
	private List<Piece> capturedPieces = new ArrayList<Piece>();

	public ChessMatch() {
		// Quem tem que saber a dimens�o de um tabuleiro de xadrez � a classe da partida de xadrez
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
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
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
		
		//Testar se esse movimento colocou o pr�prio jogador em xeque
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessExeption("You can't put yourself in check");
		}
		
		//Fazer o movimento e a pe�a movida foi a de destino
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		//Testar se o oponente ficou em xeque
		check = (testCheck(opponent(currentPlayer))) ? true : false;	
		
		//testar se a jogada que eu fiz deixou meu oponente em xeque mate, se sim o jogo termina
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();//Para trocar o jogador
		}
		
		//Testar se a pe�a movida foi um pe�o que moveu duas casas, marcado como vulner�vel
		//En Passant
		if(movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		}
		else {
			enPassantVulnerable = null;
		}
		
		
		return (ChessPiece) capturedPiece; //downcasting para converter a Piece em ChessPiece
	}
	
	//M�todo para realizar o movimento, recebendo a posi��o de origem e a posi��o de destino
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);//remove a pe�a da sua posi��o de origem
		p.increaseMoveCount();
		Piece capturedPiece = this.board.removePiece(target);//remove a pe�a capturada, que estava na sua posi��o de destino
		this.board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		//Movimento roque lado do rei e mover a torre manualmente
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);//peguei a posi��o da torre
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);//uma posi��o antes do rei para mover a torre
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);//retirar a torre de onde ela est�
			board.placePiece(rook, targetT);//colocar a torre na posi��o de destino dela
			rook.increaseMoveCount();
		}
		
		//Movimento roque lado da dama e mover torre manualmente
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);// peguei a posi��o da torre
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);// uma posi��o antes do rei para mover a torre
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT);// retirar a torre de onde ela est�
			board.placePiece(rook, targetT);// colocar a torre na posi��o de destino dela
			rook.increaseMoveCount();
		}
		
		//En Passant
		if(p instanceof Pawn) {
			//Testar se meu pe�o andou na diagonal e n�o capturou pe�a
			if(source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if(p.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				}
				else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece;//retorna a pe�a capturada
	}
	
	//M�todo para desfazer um movimento
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target); //tirar a pe�a que moveu do destino e devolve-l� para posi��o de origem
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		//Se tiver sido capturada uma pe�a, tenho que voltar ela para o tabuleiro na posi��o de destino
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			//Tenho que tirar essa pe�a da lista de pe�as capturadas e coloca-l� novamente na lista de pe�as do tabuleiro
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		//Movimento roque lado do rei e mover a torre manualmente
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);// peguei a posi��o da torre
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);// uma posi��o antes do rei para mover a torre
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);// retirar a torre da posi��o de destino
			board.placePiece(rook, sourceT);// colocar a torre na posi��o de origem dela
			rook.decreaseMoveCount();
		}

		// Movimento roque lado da dama e mover torre manualmente
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);// peguei a posi��o da torre
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);// uma posi��o antes do rei para mover a torre
			ChessPiece rook = (ChessPiece) board.removePiece(targetT);// retirar a torre da posi��o de destino
			board.placePiece(rook, sourceT);// colocar a torre na posi��o de origem
			rook.decreaseMoveCount();
		}
		
		//En Passant
		if (p instanceof Pawn) {
			// Testar se meu pe�o andou na diagonal e n�o capturou pe�a
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				} else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
		}
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
	
	//M�todo para devolver o oponente da cor
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	//M�todo para localizar o rei de uma determinada cor, varrer as pe�as do jogo para localizar o rei
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			if(p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("There is no" + color + " king on the board");
	}
	
	//M�todo para testar se o rei de uma determinada cor est� em xeque
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition(); //pego a posi��o do meu rei em formato de matriz
		//pe�as no tabuleiro filtradas com a cor do oponente desse rei
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		//peguei a lista de pe�as do oponente e para cada pe�a contida nessa lista, eu vou ter que testar
		//se existe algum movimento poss�vel que leva a posi��o do meu rei
		for(Piece p : opponentPieces) {
			//matriz de movimentos poss�veis dessa pe�a advers�ria p, se nessa matriz a posi��o correspondente
			//a posi��o do rei for true, significa que o rei est� em xeque
			boolean[][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {//se verdadeiro, o rei est� em xeque
				return true;
			}
		}
		return false;
	}
	
	//M�todo para testar se ocorreu xeque mate
	private boolean testCheckMate(Color color) {
		//teste para eliminar a possibilidade do rei n�o estar em cheque
		if(!testCheck(color)) {
			return false;
		}
		//testar todas as pe�as da cor informada se n�o tiverem um movimento poss�vel para ela que tire o rei do xeque, esta em xeque mate
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		//irei percorrer a lista e ver se existe alguma pe�a que tira o rei do xeque
		for(Piece p : list) {
			boolean[][] mat = p.possibleMoves();//ver os movimentos poss�veis dessa pe�a p
			for(int i=0; i<board.getRows(); i++) {
				for(int j=0; j<board.getColumns(); j++) {
					if(mat[i][j]) {
						//testar se essa posi��o tira do xeque
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);//fiz o movimento da pe�a na posi��o de origem para posi��o de destino
						boolean testCheck = testCheck(color);//testar se o rei da minha cor ainda est� em xeque
						undoMove(source, target, capturedPiece);//para desfazer o movimento depois do teste
						if(!testCheck) {//se n�o estava em xeque esse movimento tirou o meu rei do xeque
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	// Esse m�todo vai receber as coordenadas do xadrez e converter para as posi��es do tabuleiro
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		this.board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);//adicionar pe�as do tabuleiro na lista
	}

	// Respons�vel por iniciar a partida de xadrez, colocando as pe�as no tabuleiro
	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE,this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE,this));
		
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK,this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK,this));
	}
}
