
/*
javac -encoding utf8  TestRandomM.java
*/

import com.cloud.manga.henryTing.tool.RandomM;

import java.util.Scanner;

public class TestRandomM {
	public static void main(String[] arg) {
		Scanner scanner = new Scanner(System.in);
		int numbersNeeded, bound; Integer[] eles;
		while(true) {
			numbersNeeded = scanner.nextInt();
			bound = scanner.nextInt();
			if (bound < numbersNeeded) {
				int t = bound; bound = numbersNeeded; numbersNeeded = t;
			}
			eles = RandomM.randomWitNoDuplicates(numbersNeeded, bound);
			for (Integer ele: eles) {
				System.out.print(String.format("%d ", ele));
			}
			System.out.println("");	
		}
	}
}