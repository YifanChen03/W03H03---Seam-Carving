package pgdp.image;

import java.util.Arrays;
import java.awt.Color;

public class SeamCarving {

	public int computeGradientMagnitude(int v1, int v2) {
		return (int) Math.pow(v1 - v2, 2);
	}

	public void toGradientMagnitude(int[] picture, int[] gradientMagnitude, int width, int height) {
		int r1, r2;
		int g1, g2;
		int b1, b2;
		int gradient_sum_x = 0;
		int gradient_sum_y = 0;
		int gradient_sum = 0;
		Color px_color1, px_color2, px_color3, px_color4;

		for (int i = 0; i < picture.length; i++) {
			//mehrere if statements für mehr Klarheit
			if (i >= 0 && i < width) {
				//pixel aus Zeile 1
				gradientMagnitude[i] = Integer.MAX_VALUE;
			} else if (i >= (height - 1) * width && i <= width * height - 1) {
				//pixel aus unterster Zeile
				gradientMagnitude[i] = Integer.MAX_VALUE;
			} else if (i % width == 0) {
				//pixel aus erster spalte
				gradientMagnitude[i] = Integer.MAX_VALUE;
			} else if ((i + width - 1) % width == 0) {
				//pixel aus letzter spalte
				gradientMagnitude[i] = Integer.MAX_VALUE;
			} else {
				//zerlege int in rgb Werte mit Java class Color
				//in x-Richtung gradient berechnen
				//rgb Werte von pixel eins links
				px_color1 = new Color(picture[i - 1]);
				r1 = px_color1.getRed();
				g1 = px_color1.getGreen();
				b1 = px_color1.getBlue();

				//rgb Werte von pixel eins rechts
				px_color2 = new Color(picture[i + 1]);
				r2 = px_color2.getRed();
				g2 = px_color2.getGreen();
				b2 = px_color2.getBlue();

				//gradient_sum_x ist Summe der drei Farbbänder in x-Richtung
				gradient_sum_x = computeGradientMagnitude(r1, r2) + computeGradientMagnitude(g1, g2) + computeGradientMagnitude(b1, b2);

				//in y-Richtung gradient berechnen
				//rgb Werte von pixel eins drüber
				px_color3 = new Color(picture[i - width]);
				r1 = px_color3.getRed();
				g1 = px_color3.getGreen();
				b1 = px_color3.getBlue();

				//rgb Werte von pixel eins drunter
				px_color4 = new Color(picture[i + width]);
				r2 = px_color4.getRed();
				g2 = px_color4.getGreen();
				b2 = px_color4.getBlue();

				//gradient_sum_y ist Summe der drei Farbbänder in y-Richtung
				gradient_sum_y = computeGradientMagnitude(r1, r2) + computeGradientMagnitude(g1, g2) + computeGradientMagnitude(b1, b2);

				//x-Richtung mit y-Richtung addieren
				gradient_sum = gradient_sum_x + gradient_sum_y;

				//gradient_sum in neues Array gradientMagnitude[] schreiben
				gradientMagnitude[i] = gradient_sum;
			}
		}
	}

	public void combineMagnitudeWithMask(int[] gradientMagnitude, int[] mask, int width, int height){

	}

	public void buildSeams(int[][] seams, long[] seamWeights, int[] gradientMagnitude, int width, int height) {
	}

	public void removeSeam(int[] seam, int[] image, int height, int oldWidth) {
	}

	public int[] shrink(int[] image,int[] mask, int width, int height, int newWidth) {



		return image;
	}

}
