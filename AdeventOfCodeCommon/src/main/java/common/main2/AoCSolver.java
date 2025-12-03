package common.main2;

/**
 *  Te implementeren voor elke dag en bevat de eigenlijke puzzel logica
 * @param <T> data type van antwoord
 */
public class AoCSolver<T> {
	protected AoCContext context;
	
	public void setContext(AoCContext context) {
		this.context=context;
	}
	public void beforeAllTest() {}
	public void beforeAll() {}
	public void beforeEach() {}
	public T star1() {return null;}
	public T star2() {return null;}
}
