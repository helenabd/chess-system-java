package boardgame;

public class Board {

	private int rows;//quantidade de linhas
	private int columns;//quantidade de colunas
	private Piece [][] pieces;//matriz de peças
	
	public Board(int rows, int columns) {
		//implementando exceção no construtor para que aja pelo menos uma linha e uma coluna
		if(rows < 1 || columns < 1) {
			throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
		}
		this.rows = rows;
		this.columns = columns;
		this.pieces = new Piece[rows][columns];//será instanciada na quantidade de linhas e colunas informadas
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	//Método para retornar a peça localizada na linha e coluna recebidas
	public Piece piece(int row, int column) {
		//programaçao defensiva, testar se a posiçao existe
		if(!positionExists(row, column)) {
			throw new BoardException("Position not on the board");
		}
		return this.pieces[row][column];
	}
	
	//Método para retornar a peça localizada na posição indicada
	public Piece piece(Position position) {
		//programaçao defensiva, testar se a posiçao existe
		if(!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return this.pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		//programação defensiva, testar se já existe uma peça nessa posição
		if(thereIsAPiece(position)) {
			throw new BoardException("There is already a piece on position " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	//Dentro da classe vai ter um momento que vai ser mais fácil testar se a posição existe pela linha e pela coluna
	private boolean positionExists(int row, int column) {
		//Quando uma posição numa dada linha e coluna existe, quando a posição esta dentro do tabuleiro
		//rows == altura do tabuleiro - columns == quantidade de colunas do meu tabuleiro
		return row >=0 && row < rows && column >= 0 && column < columns;
	}
	
	//Reaproveitando o cógico acima, só que inserindo a posição
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	//Testar para ver se tem uma peça na posição indicada
	public boolean thereIsAPiece(Position position) {
		//programaçao defensiva, testar se a posiçao existe
		if(!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return piece(position) != null;
	}
}
