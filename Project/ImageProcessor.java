import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.nio.Buffer;
import java.util.*;

public class ImageProcessor {
    private static BufferedImage bi;
    private static int blurRadius;

    // The BufferedImage class describes an Image with an accessible buffer of image data
    public static BufferedImage convert(Image img) {
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics bg = bi.getGraphics();
        bg.drawImage(img, 0, 0, null);
        bg.dispose();
        return bi;
    }

    // A method to clone a BufferedImage
    public static BufferedImage cloneImage(BufferedImage img) {
        BufferedImage resultImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        WritableRaster WR1 = Raster.createWritableRaster(img.getSampleModel(), null);
        WritableRaster WR2 = img.copyData(WR1);
        resultImg.setData(WR2);
        return resultImg;
    }


    public static BufferedImage brighten(BufferedImage bi, double brightenFactor) {

        // TASK 2A //

        BufferedImage result = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < bi.getHeight(); ++y) {
            for (int x = 0; x < bi.getWidth(); ++x) {
                Color color = new Color(bi.getRGB(x,y));
                int red = (int)(color.getRed()*brightenFactor);
                if (red > 255) {
                    red = 255;
                }
                int green = (int)(color.getGreen()*brightenFactor);
                if (green > 255) {
                    green = 255;
                }
                int blue = (int)(color.getBlue()*brightenFactor);
                if (blue > 255) {
                    blue = 255;
                }
                Color newColor = new Color(red, green, blue);
                result.setRGB(x,y,newColor.getRGB());
            }
        }
        return result;
    }

    public static BufferedImage sharpen(BufferedImage bi) {

        // TASK 6 //

        // USING THE BUILTIN CONVOLVE() METHOD //

        BufferedImage result2 = null;
        float[] sharpenArr = new float[] {-1.0f, -1.0f, -1.0f, -1.0f, 9.0f, -1.0f, -1.0f, -1.0f,-1.0f};
        Kernel kernel2 = new Kernel(3, 3, sharpenArr);
        ConvolveOp cop = new ConvolveOp(kernel2, ConvolveOp.EDGE_NO_OP,null);
        result2 = cop.filter(bi, null);
        return result2;

        /* USING THE MANUAL FOR-LOOP METHOD (result is exactly the same) //

        BufferedImage result = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);

        int sumColorRed, sumColorGreen, sumColorBlue;
        int red1, green1, blue1;
        int red2, green2, blue2;
        int avgRed, avgGreen, avgBlue;

        int kernel = 3;

        for (int y = 0; y < bi.getHeight(); ++y) {
            for (int x = 0; x < bi.getWidth(); ++x) {
                sumColorRed = sumColorGreen = sumColorBlue = 0;
                red1 = green1 = blue1 = 0;
                red2 = green2 = blue2 = 0;
                avgRed = avgGreen = avgBlue = 0;
                for (int i = 0; i < kernel; ++i) {
                    for (int j = 0; j < kernel; ++j) {
                        if (y < kernel || x < kernel || y > bi.getHeight()-1-kernel || x > bi.getWidth()-1-kernel) {
                            Color color = new Color(bi.getRGB(x,y));
                            int red = color.getRed();
                            int green = color.getGreen();
                            int blue = color.getBlue();
                            Color newColor1 = new Color(red, green, blue);
                            result.setRGB(x, y, newColor1.getRGB());
                        }
                        else {
                            Color color = new Color(bi.getRGB(x+kernel - j, y+kernel-i));

                            if (j == 1 && i == 1) {
                                // pixel * 9
                                red1 = (color.getRed()*9);
                                green1 = (color.getGreen()*9);
                                blue1 = (color.getBlue()*9);

                            }
                            else {
                                // pixel * -1
                                red2 += (color.getRed()*-1);
                                green2 += (color.getGreen()*-1);
                                blue2 += (color.getBlue()*-1);
                            }
                        }
                    }
                }

                avgRed = red1 + red2;
                if (avgRed > 255) {
                    avgRed = 255;
                }
                else if (avgRed < 0) {
                    avgRed = 0;
                }
                avgGreen = green1 + green2;
                if (avgGreen > 255) {
                    avgGreen = 255;
                }
                else if (avgGreen < 0) {
                    avgGreen = 0;
                }
                avgBlue = blue1 + blue2;
                if (avgBlue > 255) {
                    avgBlue = 255;
                }
                else if (avgBlue < 0) {
                    avgBlue = 0;
                }

                Color newColor2 = new Color(avgRed, avgGreen, avgBlue);
                if (!(y < kernel || x < kernel || y > bi.getHeight()-1-kernel || x > bi.getWidth()-1-kernel)) {
                    result.setRGB(x, y, newColor2.getRGB());
                }
            }
        }
        return result;
         */
    }


    public static BufferedImage flip(BufferedImage bi) {

        // TASK 1 //

        BufferedImage result = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < bi.getHeight(); ++y) {
            for (int x = 0; x < bi.getWidth(); ++x) {
                Color color = new Color(bi.getRGB(bi.getWidth()-1-x,y));
                result.setRGB(x,y,color.getRGB());
            }
        }
        return result;
    }


    public static BufferedImage blurring(BufferedImage bi, int blurRadius) {

        // TASK 2B //

        BufferedImage result = bi;

        int sumRed, sumGreen, sumBlue;
        int avgRed, avgGreen, avgBlue;
        int kernel = (blurRadius*2+1);

        for (int y = 0; y < bi.getHeight(); ++y) {
            for (int x = 0; x < bi.getWidth(); ++x) {
                sumRed = sumGreen = sumBlue = 0;
                avgRed = avgGreen = avgBlue = 0;
                for (int i = 0; i < kernel; ++i) {
                    for (int j = 0; j < kernel; ++j) {
                        if (y < blurRadius || x < blurRadius || y > bi.getHeight()-1-blurRadius || x > bi.getWidth()-1-blurRadius) {
                            Color color = new Color(bi.getRGB(x,y));
                            int red = color.getRed();
                            int green = color.getGreen();
                            int blue = color.getBlue();
                            Color newColor1 = new Color(red, green, blue);
                            result.setRGB(x, y, newColor1.getRGB());
                        }
                        else {
                            Color color = new Color(bi.getRGB(x+blurRadius - j, y+blurRadius-i));
                            sumRed += color.getRed();
                            sumGreen += color.getGreen();
                            sumBlue += color.getBlue();
                        }
                    }
                }
                avgRed = (int) (sumRed / Math.pow(blurRadius * 2 + 1, 2));
                avgGreen = (int) (sumGreen / Math.pow(blurRadius * 2 + 1, 2));
                avgBlue = (int) (sumBlue / Math.pow(blurRadius * 2 + 1, 2));

                Color newColor2 = new Color(avgRed, avgGreen, avgBlue);
                if (!(y < blurRadius || x < blurRadius || y > bi.getHeight()-1-blurRadius || x > bi.getWidth()-1-blurRadius)) {
                    result.setRGB(x, y, newColor2.getRGB());
                }
            }
        }
        return result;
    }


    public static BufferedImage beautify(BufferedImage bi, int blurRadius, double brightenFactor) {

        // TASK 2C //

        BufferedImage result = brighten(bi,brightenFactor);

        return blurring(result,blurRadius);
    }


    public static BufferedImage darken(BufferedImage bi, double darkenFactor) {

        // TASK 3 //

        BufferedImage result = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < bi.getHeight(); ++y) {
            for (int x = 0; x < bi.getWidth(); ++x) {
                Color color = new Color(bi.getRGB(x,y));
                int red = (int)(color.getRed()*darkenFactor);
                if (red < 0) {
                    red = 0;
                }
                int green = (int)(color.getGreen()*darkenFactor);
                if (green < 0) {
                    green = 0;
                }
                int blue = (int)(color.getBlue()*darkenFactor);
                if (blue < 0) {
                    blue = 0;
                }
                Color newColor = new Color(red, green, blue);
                result.setRGB(x,y,newColor.getRGB());
                }
            }
        return result;
        }

    public static BufferedImage channelMask(BufferedImage bi, String colorOption) {

        // TASK 4 //

        BufferedImage result = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);

        switch (colorOption) {
            case "red":
                for (int y = 0; y < bi.getHeight(); ++y) {
                    for (int x = 0; x < bi.getWidth(); ++x) {
                        Color color = new Color(bi.getRGB(x, y));
                        int red = (color.getRed());
                        Color newColor = new Color(red, 0, 0);
                        result.setRGB(x, y, newColor.getRGB());
                    }
                }
                break;
            case "green":
                for (int y = 0; y < bi.getHeight(); ++y) {
                    for (int x = 0; x < bi.getWidth(); ++x) {
                        Color color = new Color(bi.getRGB(x, y));
                        int green = (color.getGreen());
                        Color newColor = new Color(0, green, 0);
                        result.setRGB(x, y, newColor.getRGB());
                    }
                }
                break;
            case "blue":
                for (int y = 0; y < bi.getHeight(); ++y) {
                    for (int x = 0; x < bi.getWidth(); ++x) {
                        Color color = new Color(bi.getRGB(x, y));
                        int blue = (color.getBlue());
                        Color newColor = new Color(0, 0, blue);
                        result.setRGB(x, y, newColor.getRGB());
                    }
                }
                break;
            default:
                return null;
        }
        return result;
    }


    public static BufferedImage[] disco(BufferedImage bi) {

        // TASK 5 //

        String[] colorArray = new String[] {"red", "blue", "green", "blue"};
        BufferedImage[] imageArray = new BufferedImage[4];

        for (int i = 0; i < 4; ++i) {
            if (i == 0 || i == 2) {
                imageArray[i] = channelMask(bi,colorArray[i]);
            }
            else {
                imageArray[i] = channelMask(flip(bi),colorArray[i]);
            }
        }
        return imageArray;
    }
}