package oo;

/**
 * State interface of the word counter.
 * 
 * @author Kongpon Charanwattanakit
 *
 */
public interface State {
	/** Do nothing. */
	public default void enterState() {
	};

	/**
	 * Handle a character to configure the state.
	 * 
	 * @param c
	 *            is a character to determine
	 */
	public void handleChar(char c);
}
