package render_engine;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class Texture {
    private Image image;
    private PixelReader reader;

    public Texture(String path) {
        this.image = new Image("file:" + path);
        this.reader = image.getPixelReader();
    }

    public Color getPixel(float u, float v) {
        if (image == null) return Color.WHITE;

        u = Math.abs(u % 1);
        v = Math.abs(v % 1);

        int x = (int) (u * (image.getWidth() - 1));
        int y = (int) (v * (image.getHeight() - 1));

        return reader.getColor(x, y);
    }
}