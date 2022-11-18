package com.cloud.manga.henryTing.tool;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/tool/RandomM.java
*/

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class RandomM {
	// https://stackoverflow.com/questions/4040001/creating-random-numbers-with-no-duplicates
	private static Integer[] _randomWitNoDuplicates(int numbersNeeded, int bound) {
		assert numbersNeeded <= bound: "RandomM randomWitNoDuplicates fail numbersNeeded <= bound";
		Random rng = new Random(); // Ideally just create one instance globally
		// Note: use LinkedHashSet to maintain insertion order
		Set<Integer> generated = new LinkedHashSet<>();
		while (generated.size() < numbersNeeded)
		{
			Integer next = rng.nextInt(bound);
			// As we're adding to a set, this will automatically do a containment check
			generated.add(next);
		}
		return generated.toArray(new Integer[0]);
	}
	// https://stackoverflow.com/questions/4040001/creating-random-numbers-with-no-duplicates
	public static Integer[] randomWitNoDuplicates(int numbersNeeded, int bound) {
		assert numbersNeeded <= bound: "RandomM randomWitNoDuplicates fail numbersNeeded <= bound";
		if (numbersNeeded*2 < bound) {
			return _randomWitNoDuplicates(numbersNeeded, bound);
		}
		
        ArrayList<Integer> list = new ArrayList<>(bound);
        for(int i = 0; i < bound; i++) {
            list.add(i);
        }
		final int remain_size = bound - numbersNeeded;
		List<Integer> buf = new ArrayList<>();
        Random rand = new Random();
        while(list.size() > remain_size) {
            int index = rand.nextInt(list.size());
			buf.add(list.remove(index));
        }
		return buf.toArray(new Integer[0]);
	}
	public static int getNextInt(int bound) {
		return java.util.concurrent.ThreadLocalRandom.current().nextInt(0, bound);
	}
	// public static int getNextInt(int bound) {
		// return java.util.concurrent.ThreadLocalRandom.current().nextInt(0, bound);
	// }
}