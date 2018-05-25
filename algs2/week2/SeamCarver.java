import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int[][] rgb;
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new java.lang.IllegalArgumentException("picture is null");
        this.picture = new Picture(picture);

        boolean transpose = false;
        int h = height(transpose);
        int w = width(transpose);
        rgb = new int[h][w];

        for (int i = 0; i < h; ++i)
            for (int j = 0; j < w; ++j)
                rgb[i][j] = this.picture.getRGB(j, i);
    }   

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width(false);
    }

    private int width(boolean transpose) {
        if (transpose)
            return picture.height();
        else
            return picture.width();
    }

    // height of current picture
    public int height() {
        return height(false);
    }

    private int height(boolean transpose) {
        if (transpose)
            return picture.width();
        else
            return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height())
            throw new java.lang.IllegalArgumentException("out of bound");
        return computeEnergy(x, y);
    }

    private double computeEnergy(int x, int y) {
        if (x == 0 || y == 0 || y == height() -1 || x == width() -1)
            return 1000.00;
        else {
            return Math.sqrt(diff(x, y +1, x, y -1) + diff(x +1, y, x -1, y));
        }
    }

    private double diff(int x1, int y1, int x2, int y2) {
        Color color1 = picture.get(x1, y1);
        Color color2 = picture.get(x2, y2);

        double gradient = 0.0;
        gradient += Math.pow(Math.abs(color1.getRed() - color2.getRed()), 2);
        gradient += Math.pow(Math.abs(color1.getGreen() - color2.getGreen()), 2);
        gradient += Math.pow(Math.abs(color1.getBlue() - color2.getBlue()), 2);

        return gradient;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        boolean transpose = true;

        transposeImage(false, false);
        int[] res = findSeam(transpose);
        transposeImage(true, false);

        return res;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        boolean transpose = false;

        return findSeam(transpose);
    }

    private int[] findSeam(boolean transpose) {
        int h = height(transpose);
        int w = width(transpose);

        double[][] accu = new double[h][w];
        int[][] from = new int[h][w];

        for (int i = h -2; i >= 0; --i) {
            for (int j = 0; j < w; ++j) {
                accu[i][j] = Double.POSITIVE_INFINITY;

                double cached;
                if (transpose)
                    cached = computeEnergy(i, j);
                else
                    cached = computeEnergy(j, i);
                for (int k = j -1; k <= j +1; ++k) {
                    if (k < 0 || k >= w)  continue;

                    if (accu[i +1][k] + cached < accu[i][j]) {
                        accu[i][j] = accu[i +1][k] + cached;
                        from[i][j] = k;
                    }
                }
            }
        }

        int start = 0;
        for (int i = 0; i < w; ++i)
            if (accu[0][i] < accu[0][start])
                start = i;

        int[] res = new int[h];
        for (int i = 0; i < h; ++i) {
            res[i] = start;
            start = from[i][start];
        }

        return res;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new java.lang.IllegalArgumentException("seam is null");
        else if (height() == 1)
            throw new java.lang.IllegalArgumentException("height is 1");
        else if (seam.length != width())
            throw new java.lang.IllegalArgumentException("wrong length of seam");
        else {
            if (seam[0] < 0 || seam[0] >= height())
                throw new java.lang.IllegalArgumentException("illegal content of seam");
            for (int i = 1; i < width(); ++i) {
                if (Math.abs(seam[i] - seam[i -1]) > 1)
                    throw new java.lang.IllegalArgumentException("illegal content of seam");
                else if (seam[i] < 0 || seam[i] >= height())
                    throw new java.lang.IllegalArgumentException("illegal content of seam");
            }
        }

        boolean transpose = true;

        transposeImage(false, false);
        removeSeam(seam, transpose);
        transposeImage(true, true);

        initPicture(height() -1, width());
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new java.lang.IllegalArgumentException("seam is null");
        else if (width() == 1)
            throw new java.lang.IllegalArgumentException("width is 1");
        else if (seam.length != height())
            throw new java.lang.IllegalArgumentException("wrong length of seam");
        else {
            if (seam[0] < 0 || seam[0] >= width())
                throw new java.lang.IllegalArgumentException("illegal content of seam");
            for (int i = 1; i < height(); ++i) {
                if (Math.abs(seam[i] - seam[i -1]) > 1)
                    throw new java.lang.IllegalArgumentException("illegal content of seam");
                else if (seam[i] < 0 || seam[i] >= width())
                    throw new java.lang.IllegalArgumentException("illegal content of seam");
            }
        }
        boolean transpose = false;

        removeSeam(seam, transpose);

        initPicture(height(), width() -1);
    }

    private void removeSeam(int[] seam, boolean transpose) {
        int h = height(transpose);
        int w = width(transpose) -1;

        int[][] nextrgb = new int[h][w];

        for (int i = 0; i < h; ++i)
            System.arraycopy(rgb[i], 0, nextrgb[i], 0, seam[i]);
        for (int i = 0; i < h; ++i)
            System.arraycopy(rgb[i], seam[i] +1, nextrgb[i], seam[i], rgb[0].length - seam[i] -1);

        rgb = nextrgb;
    }

    private void transposeImage(boolean transposed, boolean removed) {
        int h = height(transposed);
        int w = width(transposed);

        if (transposed && removed)
            --w;
        int[][] rgbT = new int[w][h];

        for (int i = 0; i < h; ++i)
            for (int j = 0; j < w; ++j) {
                rgbT[j][i] = rgb[i][j];
            }

        rgb = rgbT;
    }

    private void initPicture(int h, int w) {
        Picture nextPicture = new Picture(w, h);

        for (int i = 0; i < h; ++i)
            for (int j = 0; j < w; ++j) {
                nextPicture.setRGB(j, i, rgb[i][j]);
            }
        picture = nextPicture;
    }

}
