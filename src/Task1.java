import java.io.IOException;

public class Task1 {
    public static void main(String[] args) throws IOException {
        // 加载图片origin.jpeg
        // 生成相应灰度图，保存值grey_origin.jpeg
        // 计算相应颜色直方图，并保存至origin_histogram.csv
        GreyImageProcessor processor = new GreyImageProcessor("origin.jpeg");
        processor.dump_to_picture("output/grey_origin.jpeg", "origin.jpeg");
        processor.generate_histogram("output/origin_histogram.csv");

        // 运行直方图平衡算法
        // 将结果保存至new.jpeg
        // 计算新的颜色直方图，并保存至new_histogram.csv
        processor.histogram_balance();
        processor.generate_histogram("output/new_histogram.csv");
        processor.dump_to_picture("output/new.jpeg", "origin.jpeg");

        // 运行二值化算法
        // 将结果保存至bi.jpeg
        // 计算新的颜色直方图，并保存至bi_histogram.csv
        processor.binarization(127);
        processor.generate_histogram("output/bi_histogram.csv");
        processor.dump_to_picture("output/bi.jpeg", "origin.jpeg");
    }
}
