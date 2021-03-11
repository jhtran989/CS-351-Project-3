package gamePieces;

public class BoardSquare {
    private String square;

    public BoardSquare(String square) {
        this.square = square;
    }

    @Override
    public String toString() {
        return square;
    }
}
