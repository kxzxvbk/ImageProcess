import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageInterface {
    public int[][][] get_image_3channel(String path) throws IOException {

        File file = new File(path);
        BufferedImage image = ImageIO.read(file);

        int width = image.getWidth();
        int height = image.getHeight();

        int[][][] answer = new int[width][height][3];

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int p = image.getRGB(i, j);

                int a = (p >> 24) & 0xff;
                if (a != 255) {
                    throw new IOException("Alpha channel doesn't equal 255!");
                }

                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                answer[i][j][0] = r;
                answer[i][j][1] = g;
                answer[i][j][2] = b;
            }
        }
        return answer;
    }

    public int[][] get_image_2channel(String path) throws IOException {
        File file = new File(path);
        BufferedImage image = ImageIO.read(file);

        int width = image.getWidth();
        int height = image.getHeight();

        int[][] answer = new int[width][height];

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int p = image.getRGB(i, j);

                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                int a = (p >> 24) & 0xff;
                if (a != 255) {
                    throw new IOException("Alpha channel doesn't equal 255!");
                }

                answer[i][j] = (int) (r * 0.299 + g * 0.587 + b * 0.114);
            }
        }
        return answer;
    }

    public void write_image(int[][][] img, String path, String originModel, String format)
            throws IOException {
        File file = new File(originModel);
        BufferedImage image = ImageIO.read(file);

        int height = img[0].length;
        int width = img.length;

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int r = img[i][j][0];
                int g = img[i][j][1];
                int b = img[i][j][2];
                int a = 255;

                int p = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(i, j, p);
            }
        }
        file = new File(path);
        ImageIO.write(image, format, file);
    }

    public void write_image(int[][] img, String path, String originModel, String format)
            throws IOException {
        File file = new File(originModel);
        BufferedImage image = ImageIO.read(file);

        int height = img[0].length;
        int width = img.length;

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                int r = img[i][j];
                int g = img[i][j];
                int b = img[i][j];
                int a = 255;

                int p = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(i, j, p);
            }
        }
        file = new File(path);
        ImageIO.write(image, format, file);
    }
}
