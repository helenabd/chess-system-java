package boardgame;

public class Board {

	private int rows;//quantidade de linhas
	private int columns;//quantidade de colunas
	private Piece [][] pieces;//matriz de pe�as
	
	public Board(int rows, int columns) {
		//implementando exce��o no construtor para que aja pelo menos uma linha e uma coluna
		if(rows < 1 || columns < 1) {
			throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
		}
		this.rows = rows;
		this.columns = columns;
		this.pieces = new Piece[rows][columns];//ser� instanciada na quantidade de linhas e colunas informadas
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	//M�todo para retornar a pe�a localizada na linha e coluna recebidas
	public Piece piece(int row, int column) {
		//programa�ao defensiva, testar se a posi�ao existe
		if(!positionExists(row, column)) {
			throw new BoardException("Position not on the board");
		}
		return this.pieces[row][column];
	}
	
	//M�todo para retornar a pe�a localizada na posi��o indicada
	public Piece piece(Position position) {
		//programa�ao defensiva, testar se a posi�ao existe
		if(!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return this.pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		//programa��o defensiva, testar se j� existe uma pe�a nessa posi��o
		if(thereIsAPiece(position)) {
			throw new BoardException("There is already a piece on position " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	//Dentro da classe vai ter um momento que vai ser mais f�cil testar se a posi��o existe pela linha e pela coluna
	private boolean positionExists(int row, int column) {
		//Quando uma posi��o numa dada linha e coluna existe, quando a posi��o esta dentro do tabuleiro
		//rows == altura do tabuleiro - columns == quantidade de colunas do meu tabuleiro
		return row >=0 && row < rows && column >= 0 && column < columns;
	}
	
	//Reaproveitando o c�gico acima, s� que inserindo a posi��o
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	//Testar para ver se tem uma pe�a na posi��o indicada
	public boolean thereIsAPiece(Position position) {
		//programa�ao defensiva, testar se a posi�ao existe
		if(!positionExists(position)) {
			throw new BoardException("Position not on the board");
		}
		return piece(position) != null;
	}
}
