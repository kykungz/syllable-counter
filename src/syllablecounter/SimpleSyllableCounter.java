package syllablecounter;

/**
 * SimpleSyllableCounter class for counting syllable in a word using State
 * Design Pattern (non-OO).
 * 
 * @author Kongpon Charanwattanakit
 *
 */
public class SimpleSyllableCounter {
	private State state;

	/**
	 * Return total syllables counted in the word.
	 * 
	 * @param word
	 *            is the word to count syllables
	 * @return number of syllables in the word
	 */
	public int countSyllables(String word) {
		word = word.toLowerCase() + " ";
		int syllableCount = 0;
		this.state = State.START;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			switch (state) {
			case START:
				if (isVowelOrY(c)) {
					setState(State.SINGLE_VOWEL);
				} else if (isConsonant(c)) {
					setState(State.CONSONANT);
				} else if (isNonLetter(c) || isHyphen(c)) {
					return 0;
				}
				break;
			case CONSONANT:
				if (c == 'e') {
					setState(State.E);
				} else if (isVowelOrY(c)) {
					setState(State.SINGLE_VOWEL);
				} else if (isHyphen(c)) {
					setState(State.HYPHEN);
				} else if (isNonLetter(c)) {
					return 0;
				}
				break;
			case SINGLE_VOWEL:
				if (isVowel(c)) {
					setState(State.MULTI_VOWEL);
				} else if (isConsonant(c)) {
					syllableCount++;
					setState(State.CONSONANT);
				} else if (isHyphen(c)) {
					syllableCount++;
					setState(State.HYPHEN);
				} else if (isEnding(c)) {
					syllableCount++;
				} else if (isNonLetter(c)) {
					return 0;
				}
				break;
			case MULTI_VOWEL:
				if (isConsonant(c)) {
					syllableCount++;
					setState(State.CONSONANT);
				} else if (isHyphen(c)) {
					syllableCount++;
					setState(State.HYPHEN);
				} else if (isEnding(c)) {
					syllableCount++;
				} else if (isNonLetter(c)) {
					return 0;
				}
				break;
			case E:
				if (isEnding(c) && syllableCount == 0) {
					syllableCount++;
				} else if (isVowel(c)) {
					setState(State.MULTI_VOWEL);
				} else if (isConsonant(c)) {
					syllableCount++;
					setState(State.CONSONANT);
				} else if (isHyphen(c)) {
					if (syllableCount == 0) {
						syllableCount++;
					}
					setState(State.HYPHEN);
				} else if (isNonLetter(c)) {
					return 0;
				}
				break;
			case HYPHEN:
				if (!isEnding(c)) {
					if (isHyphen(c) || isNonLetter(c)) {
						return 0;
					} else if (isVowelOrY(c)) {
						setState(State.SINGLE_VOWEL);
					} else if (isConsonant(c)) {
						setState(State.CONSONANT);
					}
				}
				break;
			default:
				break;
			}
		}
		return syllableCount;
	}

	/**
	 * Set the state of this WordCounter.
	 * 
	 * @param state
	 *            is the state to be set
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Check if the character is a vowel (a, e, i, o, u) of letter Y.
	 * 
	 * @param c
	 *            is the character to be checked
	 * @return true if it is a vowel or y, false otherwise
	 */
	private boolean isVowelOrY(char c) {
		return String.valueOf(c).matches("[aeiouy]");
	}

	/**
	 * Check if the character is a vowel (a, e, i, o, u).
	 * 
	 * @param c
	 *            is the character to be checked
	 * @return true if it is a vowel, false otherwise
	 */
	private boolean isVowel(char c) {
		return String.valueOf(c).matches("[aeiou]");
	}

	/**
	 * Check if the character is a consonant (non-vowel).
	 * 
	 * @param c
	 *            is the character to be checked
	 * @return true if it is a consonant, false otherwise
	 */
	private boolean isConsonant(char c) {
		return String.valueOf(c).matches("[a-z&&[^aeiou]]");
	}

	/**
	 * Check if the character is a hyphen or dash.
	 * 
	 * @param c
	 *            is the character to be checked
	 * @return true if it is a hyphen, false otherwise
	 */
	private boolean isHyphen(char c) {
		return c == '-';
	}

	/**
	 * Check if the character is a non letter (special character). Excluding
	 * hyphen and apostrophe.
	 * 
	 * @param c
	 *            is the character to be checked
	 * @return true if it is not a letter, false otherwise
	 */
	private boolean isNonLetter(char c) {
		return String.valueOf(c).matches("[^a-z' -]");
	}

	/**
	 * Check if the character is an ending charanter (whitespace).
	 * 
	 * @param c
	 *            is the character to be checked
	 * @return true if it is a whitespace, false otherwise
	 */
	private boolean isEnding(char c) {
		return c == ' ';
	}

}
