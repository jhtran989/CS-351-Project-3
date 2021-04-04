package gui;

import javafx.scene.image.ImageView;

public class TileGUIPiece {
    private double x;
    private double y;
    private double tileSize;
    private ImageView tileImage;

    public TileGUIPiece(double x, double y, double tileSize,
                        ImageView tileImage) {
        this.x = x;
        this.y = y;
        this.tileSize = tileSize;
        this.tileImage = tileImage;
        tileImage.setPreserveRatio(true); // assuming the original image of
        // the tile was a square...
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
}
