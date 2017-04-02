package syllablecounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Main {

	public static void main(String[] args) throws IOException {
		WordCounter counter = new WordCounter();
		// System.out.println(counter.countSyllables("home-brew"));
		final String DICT_URL = "https://se.cpe.ku.ac.th/dictionary.txt";
		final double NANO = Math.pow(10, -9);
		URL url = new URL(DICT_URL);
		InputStream input = url.openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		int sum = 0;
		int words = 0;
		String word;
		long start = System.nanoTime();
		while ((word = reader.readLine()) != null) {
			int syll = counter.countSyllables(word);
			if (syll > 0)
				words++;
			sum += syll;
		}
		System.out.println("Reading words from " + DICT_URL);
		System.out.printf("Counted %d syllables in %d words\n", sum, words);
		System.out.printf("Elapse time: %.3f sec\n", (System.nanoTime() - start) * NANO);
	}
}
