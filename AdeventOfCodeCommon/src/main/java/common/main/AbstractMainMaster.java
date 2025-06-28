package common.main;

/**
 * Basis voor de Main
 * T : data type van antwoord
 */
public class AbstractMainMaster<T> extends MainMaster<T> {

	@Override
	public void beforeAllTest() {
		// TODO Auto-generated method stub
	}
		
	@Override
	public void beforeAll() {
		loadInput();		
	}

	@Override
	public void beforeEach() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public T star1() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T star2() {
		// TODO Auto-generated method stub
		return null;
	}
}
