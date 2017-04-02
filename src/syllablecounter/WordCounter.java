package syllablecounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class WordCounter {
	private final State START = new StartState();
	private final State CONSONANT = new ConsonantState();
	private final State SINGLE_VOWEL = new SingleVowelState();
	private final State MULTI_VOWEL = new MultiVowelState();
	private final State E = new EState();
	private final State HYPHEN = new HyphenState();
	private final State NON_WORD = new NonWordState();
	private State state;
	private int syllableCount;

	public int countSyllables(String word) {
		this.state = START;
		word = word.trim().toLowerCase() + " ";
		syllableCount = 0;
		for (int index = 0; index < word.length(); index++) {
			if (state == NON_WORD)
				return 0;
			char c = word.charAt(index);
			state.handleChar(c);
		}
		return syllableCount;
	}

	public void setState(State state) {
		this.state = state;
		state.enterState();
	}

	private boolean isVowelOrY(char c) {
		return String.valueOf(c).matches("[aeiouy]");
	}

	private boolean isVowel(char c) {
		return String.valueOf(c).matches("[aeiou]");
	}

	private boolean isConsonant(char c) {
		return String.valueOf(c).matches("[a-z&&[^aeiou]]");
	}

	private boolean isHyphen(char c) {
		return c == '-';
	}

	private boolean isNonLetter(char c) {
		return String.valueOf(c).matches("[^a-z'-]");
	}

	private boolean isEnding(char c) {
		return c == ' ';
	}

	class StartState implements State {

		@Override
		public void handleChar(char c) {
			if (isVowelOrY(c)) {
				setState(SINGLE_VOWEL);
			} else if (isConsonant(c)) {
				setState(CONSONANT);
			} else if (isNonLetter(c) || isHyphen(c)) {
				setState(NON_WORD);
			}
		}
	}

	class ConsonantState implements State {

		@Override
		public void handleChar(char c) {
			if (c == 'e') {
				setState(E);
			} else if (isVowelOrY(c)) {
				setState(SINGLE_VOWEL);
			} else if (isHyphen(c)) {
				setState(HYPHEN);
			} else if (isNonLetter(c)) {
				setState(NON_WORD);
			}
		}
	}

	/* Similar to SingleVowelState but with last E check */
	class EState implements State {

		@Override
		public void handleChar(char c) {
			if (isEnding(c) && syllableCount == 0) {
				syllableCount++;
			} else if (isVowel(c)) {
				setState(MULTI_VOWEL);
			} else if (isConsonant(c)) {
				syllableCount++;
				setState(CONSONANT);
			} else if (isHyphen(c)) {
				if (syllableCount == 0) {
					syllableCount++;
				}
				setState(HYPHEN);
			} else if (isNonLetter(c)) {
				setState(NON_WORD);
			}
		}

	}

	class SingleVowelState implements State {

		@Override
		public void handleChar(char c) {
			if (isVowel(c)) {
				setState(MULTI_VOWEL);
			} else if (isConsonant(c)) {
				syllableCount++;
				setState(CONSONANT);
			} else if (isHyphen(c)) {
				syllableCount++;
				setState(HYPHEN);
			} else if (isEnding(c)) {
				syllableCount++;
			} else if (isNonLetter(c)) {
				setState(NON_WORD);
			}
		}
	}

	class MultiVowelState implements State {

		@Override
		public void handleChar(char c) {
			if (isConsonant(c)) {
				syllableCount++;
				setState(CONSONANT);
			} else if (isHyphen(c)) {
				syllableCount++;
				setState(HYPHEN);
			} else if (isEnding(c)) {
				syllableCount++;
			} else if (isNonLetter(c)) {
				setState(NON_WORD);
			}
		}
	}

	class HyphenState implements State {

		@Override
		public void handleChar(char c) {
			if (!isEnding(c)) {
				if (isHyphen(c) || isNonLetter(c)) {
					setState(NON_WORD);
				} else if (isVowelOrY(c)) {
					setState(SINGLE_VOWEL);
				} else if (isConsonant(c)) {
					setState(CONSONANT);
				}
			}
		}
	}

	class NonWordState implements State {

		@Override
		public void handleChar(char c) {

		}

	}

}
