package pgdp.image;

import java.util.Arrays;

public class SeamCarving {

	public int computeGradientMagnitude(int v1, int v2) {
		return (int) Math.pow((v1 - v2), 2);
	}

	public void toGradientMagnitude(int[] picture, int[] gradientMagnitude, int width, int height) {

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
