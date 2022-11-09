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
			//prüfe ob pixel sich am Bildrand befindet
			if (i >= 0 && i < width) {
				//pixel aus Zeile 1
				gradientMagnitude[i] = Integer.MAX_VALUE;
			} else if (i >= (height - 1) * width && i <= width * height - 1) {
				//pixel aus unterster Zeile
				gradientMagnitude[i] = Integer.MAX_VALUE;
			} else if (i % width == 0) {
				//pixel aus erster spalte
				gradientMagnitude[i] = Integer.MAX_VALUE;
			} else if ((i + 1) % width == 0) {
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
		Color px_color;
		int r, g, b;
		int sum = 0;
		//int maxed = 0;

		//image.length = mask.length, also i = j
		for (int i = 0; i < mask.length; i++) {
			//zerlege int in rbg Komponenten
			px_color = new Color(mask[i]);
			r = px_color.getRed();
			g = px_color.getGreen();
			b = px_color.getBlue();
			sum = r + g + b;

			//falls alle 3 Kanäle = 0, dann pixel auf Maximalwert setzen
			if (sum == 0) {
				gradientMagnitude[i] = Integer.MAX_VALUE;
				//maxed++;
			}
		}
		//return maxed;
	}

	public void buildSeams(int[][] seams, long[] seamWeights, int[] gradientMagnitude, int width, int height) {
		int px1; //px1 ist linker Pixel unter target Pixel, px2 direkt darunter, px3 rechts drunter
		int px2;
		int px3;
		int current_px = 0; //veränderbare Position des jetzigen Pixels

		//für jeden Pixel aus der ersten Zeile
		for (int i = 0; i < width; i++) {
			//falls erster Pixel oder letzter Pixel

			//für die gesamte Höhe, ein ganzes vertikales Seam
			current_px = i;
			//Gewicht des Startpixels zu seamWeights hinzufügen
			seamWeights[i] = gradientMagnitude[current_px];
			for (int r = 1; r < height; r++) {
				//prüfe falls am rand
				if (current_px % width == 0 || (current_px + 1) % width == 0) {
					//falls am rand
					if (current_px % width == 0) {
						//linker Rand
						px2 = current_px + width;
						px3 = current_px + width + 1;
						if (find_lowest_weight_border(px2, px3) == 1) {
							//px2 wählen
							current_px = px2;
						} else if (find_lowest_weight_border(px2, px3) == 0) {
							//px2 wählen
							current_px = px2;
						} else {
							//px3 wählen
							current_px = px3;
						}
					} else {
						//rechter Rand
						px1 = current_px + width - 1;
						px2 = current_px + width;
						if (find_lowest_weight_border(px1, px2) == 1) {
							//px2 wählen
							current_px = px2;
						} else if (find_lowest_weight_border(px1, px2) == 0) {
							//px1 wählen
							current_px = px1;
						} else {
							//px2 wählen
							current_px = px2;
						}
					}
				} else {
					//alle Pixel, die nicht am Rand sind
					px1 = current_px + width - 1;
					px2 = current_px + width;
					px3 = current_px + width + 1;

					if (find_lowest_weight(px1, px2, px3) == 0) {
						//px1 wählen
						current_px = px1;
					} else if (find_lowest_weight(px1, px2, px3) == 1) {
						//px2 wählen
						current_px = px2;
					} else {
						//px3 wählen
						current_px = px3;
					}
				}
				seamWeights[i] = seamWeights[i] + gradientMagnitude[current_px];
			} //MUSS SCHAUEN OB ein Seam lang genug ist
		}
		System.out.println(Arrays.toString(seamWeights));
	}

	public void removeSeam(int[] seam, int[] image, int height, int oldWidth) {
	}

	public static int find_lowest_weight(int p1, int p2, int p3) {
		int lowest = 0;

		if (p1 != p2 && p2 != p3 && p1 != p3) {
			//falls alle Gewichtungen verschieden
			if (p1 < p2 && p1 < p3) {
				//falls p1 am kleinsten
				lowest = 0;
			} else if (p2 < p1 && p2 < p3) {
				//falls p2 am kleinsten
				lowest = 1;
			} else if (p3 < p1 && p3 < p2) {
				//falls p3 am kleinsten
				lowest = 2;
			}
		} else if (p2 <= p1 && p2 <= p3) {
			//falls mittleres Pixel gleich oder kleiner als die anderen
			lowest = 1;
		} else if (p3 < p1) {
			//falls rechtes Pixel kleiner als linkes Pixel
			lowest = 2;
		} else {
			//Rest, also falls p1 == p3 oder p1 < p3
			lowest = 0;
		}
		return lowest;
	}

	public static int find_lowest_weight_border(int p1, int p2) {
		int lowest = 0;
		if (p1 == p2) {
			lowest = 1;
		} else if (p1 < p2) {
			lowest = 0;
		} else {
			lowest = 2;
		}
		return lowest;
	}

	public int[] shrink(int[] image,int[] mask, int width, int height, int newWidth) {
		//int[] gradientMagnitude = new int[image.length];
		//toGradientMagnitude(image, gradientMagnitude, width, height);
		//combineMagnitudeWithMask(gradientMagnitude ,mask, width, height);

		//System.out.println(mask.length);
		//System.out.println(image.length);
		//System.out.println(Arrays.toString(mask));

		//System.out.println(Arrays.toString(gradientMagnitude)); //gradientMagnitude beinhaltet 2816 mal Integer.MAX_VALUE, also 2816 Randpixel
		//System.out.println(combineMagnitudeWithMask(gradientMagnitude, mask, width, height)); //62642 pixel wurden mit Maske gesichert
		//System.out.println(image.length - height); //Ausgabe: 467250, also man soll einen vertikalen Seam löschen

		//buildSeams(new int[0][0], new long[width], gradientMagnitude, width, height);
		return image;
	}

}
