package syllablecounter;

public interface State {
	/** Do nothing. */
	public default void enterState() {
	};

	public void handleChar(char c);
}
