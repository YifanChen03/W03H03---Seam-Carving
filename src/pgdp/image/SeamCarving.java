package pgdp.image;

import java.util.Arrays;
import java.util.List;
import java.awt.Color;

public class SeamCarving {

	public static int n1 = 0;
	public static int n2 = 0;
	public static int n3 = 0;
	public static int total_n = 0;
	public static int check1 = 0;
	public static int check2 = 0;
	public static int check3 = 0;
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
		//System.out.println(Arrays.toString(gradientMagnitude));
		//return maxed;
	}

	public void buildSeams(int[][] seams, long[] seamWeights, int[] gradientMagnitude, int width, int height) {
		int px1; //px1 ist linker Pixel unter target Pixel, px2 direkt darunter, px3 rechts drunter
		int px2;
		int px3;
		//int n1 = 0, n2 = 0, n3 = 0;
		int current_px = 0; //veränderbare Position des jetzigen Pixels

		//für jeden Pixel aus der ersten Zeile
		for (int i = 0; i < width; i++) {
			//falls erster Pixel oder letzter Pixel

			//für die gesamte Höhe, ein ganzes vertikales Seam
			current_px = i;
			//Gewicht des Startpixels zu seamWeights hinzufügen
			seamWeights[i] = gradientMagnitude[current_px];
			//Startpixel in 2-dimensionalem Array speichern
			seams[i][0] = current_px;
			//total_n++;

			for (int r = 1; r < height; r++) {
				//prüfe falls am rand
				if (current_px % width == 0 || (current_px + 1) % width == 0) {
					//falls am rand
					if (current_px % width == 0) {
						//linker Rand
						px2 = current_px + width;
						px3 = current_px + width + 1;
						if (find_lowest_weight_border(gradientMagnitude, px2, px3) == 1) {
							//px2 wählen
							current_px = px2;
							//n2++;
						} else if (find_lowest_weight_border(gradientMagnitude, px2, px3) == 0) {
							//px2 wählen
							current_px = px2;
							//n2++;
						} else {
							//px3 wählen
							current_px = px3;
							//n3++;
						}
					} else {
						//rechter Rand
						px1 = current_px + width - 1;
						px2 = current_px + width;
						if (find_lowest_weight_border(gradientMagnitude, px1, px2) == 1) {
							//px2 wählen
							current_px = px2;
							//n2++;
						} else if (find_lowest_weight_border(gradientMagnitude, px1, px2) == 0) {
							//px1 wählen
							current_px = px1;
							//n1++;
						} else {
							//px2 wählen
							current_px = px2;
							//n2++;
						}
					}
				} else {
					//alle Pixel, die nicht am Rand sind
					px1 = current_px + width - 1;
					px2 = current_px + width;
					px3 = current_px + width + 1;

					if (find_lowest_weight(gradientMagnitude, px1, px2, px3) == 0) {
						//px1 wählen
						current_px = px1;
						//n1++;
					} else if (find_lowest_weight(gradientMagnitude, px1, px2, px3) == 1) {
						//px2 wählen
						current_px = px2;
						//n2++;
					} else {
						//px3 wählen
						current_px = px3;
						//n3++;
					}
				}
				seamWeights[i] = seamWeights[i] + gradientMagnitude[current_px];
				//seams[i][r] wird zur Pixelnummer pro Reihe
				//seams[i][r] = current_px % width;
				seams[i][r] = current_px;
				//total_n++;
			}
		}
		//System.out.println(Arrays.deepToString(seams));
		/*System.out.println("n1: " + n1);
		System.out.println("n2: " + n2);
		System.out.println("n3: " + n3);*/
		//System.out.println(Arrays.toString(seams[342]));
		//System.out.println(seams[0].length);
		//System.out.println(seams.length);
	}

	public void removeSeam(int[] seam, int[] image, int height, int oldWidth) {
		int d_px = 0;
		//für jeden Pixel des Seams
		for (int i = 0; i < seam.length; i++){
			//zu entfernendes Pixel identifizieren
			//d_px = seam[i] + oldWidth * i; //mit Pixelnummer pro Reihe
			d_px = seam[i];

			//alle Pixel nach diesem Pixel im Array verschieben
			for (int r = d_px; r < image.length - 1; r++) {
				image[r] = image[r + 1];
			}
			//weiß nicht ob man die nachfolgenden Pixel löschen muss
		}
	}

	public static int find_lowest_weight(int[] gradientMagnitude, int p1, int p2, int p3) {
		int lowest = 0;
		int c1 = gradientMagnitude[p1];
		int c2 = gradientMagnitude[p2];
		int c3 = gradientMagnitude[p3];
		//check1 = p1;
		//check2 = p2;
		//check3 = p3;

		if (c1 != c2 && c2 != c3 && c1 != c3) {
			//falls alle Gewichtungen verschieden
			if (c1 < c2 && c1 < c3) {
				//falls p1 am kleinsten
				lowest = 0;
				//n1++;
			} else if (c2 < c1 && c2 < c3) {
				//falls p2 am kleinsten
				lowest = 1;
				//n2++;
			} else if (c3 < c1 && c3 < c2) {
				//falls p3 am kleinsten
				lowest = 2;
				//n3++;
			}
		} else if (c2 <= c1 && c2 <= c3) {
			//falls mittleres Pixel gleich oder kleiner als die anderen
			lowest = 1;
			//n2++;
		} else if (c3 < c1) {
			//falls rechtes Pixel kleiner als linkes Pixel
			lowest = 2;
			//n3++;
		} else {
			//Rest, also falls p1 == p3 oder p1 < p3
			lowest = 0;
			//n1++;
		}
		return lowest;
	}

	public static int find_lowest_weight_border(int[] gradientMagnitude, int p1, int p2) {
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

	public static int find_index_array(long array[], long n) {
		//Methode findet index von Seam mit kleinstem Gewicht
		int index = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == n) {
				index = i;
				break;
			}
		}
		return index;
	}

	public static long find_lowest_n_array(long array[]) {
		//Methode gibt kleinstes Gewicht eines Seams zurück
		long lowest_n = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] < lowest_n) {
				lowest_n = array[i];
			}
		}
		return lowest_n;
	}

	public int[] shrink(int[] image,int[] mask, int width, int height, int newWidth) {
		int index_seam = 0;
		int[] newImage = new int[image.length - height];
		while (newWidth < width) {
			int[][] seams = new int[width][height];
			long[] seamWeights = new long[width];
			int[] gradientMagnitude = new int[image.length];
			toGradientMagnitude(image, gradientMagnitude, width, height);
			combineMagnitudeWithMask(gradientMagnitude ,mask, width, height);
			buildSeams(seams, seamWeights, gradientMagnitude, width, height);

			//seam mit kleinstem Gewicht und kleinstem Index
			index_seam = find_index_array(seamWeights, find_lowest_n_array(seamWeights));
			removeSeam(seams[index_seam], image, height, width);
			//Werte von image in neues kleineres Array geben
			/*for (int i = 1; i < image.length; i++) {
				newImage[i - 1] = image[i - 1];
			}*/

			removeSeam(seams[index_seam], mask, height, width);
			//System.out.println(index_seam);
			//System.out.println(Arrays.toString(seams[index_seam]));
			width--;
		}
		//width wird verändert
		//width und newWidth bedeuten vermutlich etwas anderes als ich denke
		//seamRemove funktioniert bei mir nicht richtig


		//System.out.println(mask.length);
		//System.out.println(image.length);
		//System.out.println(Arrays.toString(mask));

		//System.out.println(Arrays.toString(gradientMagnitude)); //gradientMagnitude beinhaltet 2816 mal Integer.MAX_VALUE, also 2816 Randpixel
		//System.out.println(combineMagnitudeWithMask(gradientMagnitude, mask, width, height)); //62642 pixel wurden mit Maske gesichert
		//System.out.println(image.length - height); //Ausgabe: 467250, also man soll einen vertikalen Seam löschen

		//buildSeams(new int[width][height], new long[width], gradientMagnitude, width, height);
		//System.out.println("n1: " + n1);
		//System.out.println("n2: " + n2);
		//System.out.println("n3: " + n3);
		//System.out.println(height + " " + width);
		//System.out.println("total_n: " + total_n);
		//System.out.println("c1: " + check1);
		//System.out.println("c2: " + check2);
		//System.out.println("c3: " + check3);
		return image;
	}

}
