import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GreyImageProcessor {
    private int[][] image;
    private int width;
    private int height;

    /*
    Calculate the graph histogram according to this.image.
    Return an array with size: 256.
    Each element represent the number of corresponding pixel in the image.
     */
    private int[] cal_histogram() {
        int[] his = new int[256];
        for (int i = 0;i < 256;i++) { his[i] = 0; }

        for (int i = 0;i < width;i++) {
            for (int j = 0;j < height;j++) {
                if (image[i][j] > 255) { System.out.println(image[i][j]); }
                his[this.image[i][j]]++;
            }
        }

        return his;
    }

    public GreyImageProcessor(String imgPath) throws IOException {
        ImageInterface imageInterface = new ImageInterface();
        int[][] image = imageInterface.get_image_2channel(imgPath);

        this.image = image;
        this.width = image.length;
        this.height = image[0].length;
    }

    /*
    Calculate histogram according to self.image.
    Dump the histogram to file: fileName
     */
    public void generate_histogram(String fileName) throws IOException {
        File file = new File(fileName);
        FileOutputStream out = new FileOutputStream(file);
        int[] his = this.cal_histogram();

        for (int i = 0;i < 256;i++) {
            String s = Integer.toString(i);
            for (int j = 0;j < s.length();j++) {
                out.write(s.charAt(j));
            }
            out.write(',');

            s = Integer.toString(his[i]);
            for (int j = 0;j < s.length();j++) {
                out.write(s.charAt(j));
            }
            out.write('\n');
        }
        out.close();
    }

    /*
    Implementation of histogram balance algorithm.
    self.image will change.
     */
    public void histogram_balance() {
        int[] his = cal_histogram();
        double[] freq = new double[256];
        for (int i = 0;i < 256;i++) { freq[i] = (1.0 * his[i]) / (1.0 * width * height); }

        double[] s = new double[256];
        double sum = 0.0;
        for (int i = 0;i < 256;i++) {
            sum += freq[i];
            s[i] = 255 * sum;
        }

        for (int i = 0;i < width;i++) {
            for (int j = 0;j < height;j++) {
                image[i][j] = (int) s[image[i][j]];
            }
        }
    }

    public void dump_to_picture(String fileName, String oldName) throws IOException {
        ImageInterface imageInterface = new ImageInterface();
        imageInterface.write_image(image, fileName, oldName, "jpg");
    }

    /*
    linear focus
    f(x) = low                                           ,x < x1
           (y1 - y2) * (x - x1) / (x1 - x2) + y1         ,x1 <= x <= x2
           high                                          ,x > x2
     */
    public void linear_focus(int x1, int y1, int x2, int y2, int low, int high) {
        int k = (int) (1.0 * (y1 - y2) / (x1 - x2));
        int b = -k * x1 + y1;

        for (int i = 0;i < width;i++) {
            for (int j = 0;j < height;j++) {
                if (image[i][j] < x1) { image[i][j] = low; }
                else if (image[i][j] > x2) { image[i][j] = high; }
                else { image[i][j] = k * image[i][j] + b; }
            }
        }
    }

    /*
    f(x) = (y1 / x1) * (x - x1) + y1                     ,x < x1
           (y1 - y2) * (x - x1) / (x1 - x2) + y1         ,x1 <= x <= x2
           ((y2 - 255) / (x2 - 255))(x - x2) + y2        ,x > x2
     */
    public void piecewise_linear_focus(int x1, int y1, int x2, int y2) {
        int k1 = (int) (1. * y1 / x1);
        int b1 = -k1 * x1 + y1;

        int k2 = (int) (1.0 * (y1 - y2) / (x1 - x2));
        int b2 = -k2 * x1 + y1;

        int k3 = (int) (1. * (y2 - 255.) / (x2 - 255.));
        int b3 = -k3 * x2 + y2;

        for (int i = 0;i < width;i++) {
            for (int j = 0;j < height;j++) {
                if (image[i][j] < x1) { image[i][j] = k1 * image[i][j] + b1; }
                else if (image[i][j] > x2) { image[i][j] = k3 * image[i][j] + b3; }
                else { image[i][j] = k2 * image[i][j] + b2; }
            }
        }
    }

    /*
    f(x) = 0        , x < threshold
           255      ,x >= threshold
     */
    public void binarization(int threshold) {
        for (int i = 0;i < width;i++) {
            for (int j = 0;j < height;j++) {
                if (image[i][j] < threshold) { image[i][j] = 0; }
                else { image[i][j] = 255; }
            }
        }
    }
}
