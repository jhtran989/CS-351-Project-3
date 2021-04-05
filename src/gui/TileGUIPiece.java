package gui;

import gamePieces.Tile;
import javafx.scene.image.ImageView;

public class TileGUIPiece {
    private double x;
    private double y;
    private double tileSize;
    private Tile tile;
    private ImageView tileImage;
    private int rowIndex;
    private int columnIndex;

    public TileGUIPiece(double x, double y, double tileSize,
                        Tile tile, ImageView tileImage) {
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.tile = tile;
        this.tileImage = tileImage;
        tileImage.setPreserveRatio(true); // assuming the original image of
        // the tile was a square...

        // some default values
        rowIndex = -1;
        columnIndex = -1;
    }

    public Tile getTile() {
        return tile;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public ImageView getTileImage() {
        return tileImage;
    }

    public void draw() {
        tileImage.setFitWidth(tileSize);
        tileImage.setTranslateX(x);
        tileImage.setTranslateY(y);
    }

    @Override
    public String toString() {
        return tile.toString();
    }
}
