package oo;

/**
 * WordCounter class for counting syllable in a word using State Design Pattern
 * (Object-Oriented).
 * 
 * @author Kongpon Charanwattanakit
 *
 */
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

    /**
     * Return total syllables counted in the word.
     * 
     * @param word
     *            is the word to count syllables
     * @return number of syllables in the word
     */
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

    /**
     * Set the state of this WordCounter.
     * 
     * @param state
     *            is the state to be set
     */
    public void setState(State state) {
	this.state = state;
	state.enterState();
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
	return String.valueOf(c).matches("[^a-z'-]");
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

    /**
     * Starting state.
     * 
     * @author Kongpon Charanwattanakit
     *
     */
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

    /**
     * Consonant letter state.
     * 
     * @author Kongpon Charanwattanakit
     *
     */
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

    /**
     * Letter E state. Similar to SingleVowelState but with last E check
     * 
     * @author Kongpon Charanwattanakit
     **/
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
		syllableCount++;
		setState(HYPHEN);
	    } else if (isNonLetter(c)) {
		setState(NON_WORD);
	    }
	}

    }

    /**
     * Single vowel state.
     * 
     * @author Kongpon Charanwattanakit
     *
     */
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

    /**
     * Multiple vowel state.
     * 
     * @author Kongpon Charanwattanakit
     *
     */
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

    /**
     * Hyphen or dash state.
     * 
     * @author Kongpon Charanwattanakit
     *
     */
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

    /**
     * Not a word state.
     * 
     * @author Kongpon Charanwattanakit
     *
     */
    class NonWordState implements State {

	@Override
	public void handleChar(char c) {

	}

    }

}
