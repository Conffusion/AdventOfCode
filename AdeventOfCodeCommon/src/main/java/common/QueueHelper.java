package common;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Om stackoverflow errors te voorkomen, elke nieuwe te onderzoeken case op de queue zetten.
 * De consumer leest het wel.
 * @param <T>
 */
public class QueueHelper<T> {

	private Queue<T> queue=new LinkedList<>();
	
	/**
	 * Add element to the queue;
	 * @param element
	 */
	public void add(T element) {
		queue.add(element);
	}
	/**
	 * reads from the queue until it is empty
	 * @param consumer
	 */
	public void consume(Consumer<T> consumer) {
		T current;
		while((current=queue.poll())!=null) {
			consumer.accept(current);
		}
	}
}
