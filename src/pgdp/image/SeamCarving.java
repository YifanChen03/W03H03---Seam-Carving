package pgdp.image;

import java.util.Arrays;

public class SeamCarving {

	public int computeGradientMagnitude(int v1, int v2) {
		return (int) Math.pow(v1 - v2, 2);
	}

	public void toGradientMagnitude(int[] picture, int[] gradientMagnitude, int width, int height) {
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
				//in x-Richtung gradientMagnitude berechnen
				gradientMagnitude[i] = computeGradientMagnitude(picture[i - 1], picture[i + 1]);
				//in y-Richtung gradient Magnitude berechnen und addieren
				gradientMagnitude[i] = gradientMagnitude[i] + computeGradientMagnitude(picture[i - width], picture[i + width]);
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
