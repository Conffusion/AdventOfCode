package common;

import java.util.Iterator;

import org.junit.jupiter.api.Assertions;

/**
 * iterator die loopt over de elementen van een array.
 * Na de laatste beginnen we gewoon terug bij de eerste
 * @param <T>
 */
public class LoopIterator<T> implements Iterator<T> {

	private T[] data;
	int index=0;
	public LoopIterator(T[] data) {
		this.data=data;
	}

	/**
	 * We loopen dus er is altijd een next.
	 * @returns true
	 */
	@Override
	public boolean hasNext() {
		return true;
	}

	/**
	 * volgende element, als we aan het einde van de lijst zijn beginnen we van vooraf aan.
	 */
	@Override
	public T next() {
		T elem=data[index];
		index=(index+1)%data.length;
		return elem;
	}

	// test this class
	public static void main(String[] args) {
		Character[] input=new Character[] {'1','2','5'};
		LoopIterator<Character> itit=new LoopIterator<>(input);
		Assertions.assertEquals('1', itit.next());
		Assertions.assertEquals('2', itit.next());
		Assertions.assertEquals('5', itit.next());
		Assertions.assertEquals('1', itit.next());
		Assertions.assertEquals('2', itit.next());
		Assertions.assertEquals('5', itit.next());
		Assertions.assertEquals('1', itit.next());
		Assertions.assertEquals('2', itit.next());
		Assertions.assertEquals('5', itit.next());
		Assertions.assertEquals('1', itit.next());
		Assertions.assertEquals('2', itit.next());
		Assertions.assertEquals('5', itit.next());
	}

}
