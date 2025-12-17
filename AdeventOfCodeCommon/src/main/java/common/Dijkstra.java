package common;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Is een soort hashmap die per key de beste oplossing bijhoudt.
 */
public class Dijkstra<K,V> {
	private Comparator<V> isBetter;
	private ConcurrentHashMap<K,V> register=new ConcurrentHashMap<>();
	
	/**
	 * @param isBetter om te bepalen welke de beste value is voor eenzelfde key
	 * Comparator.compare wordt aangeroepen met (new, old) en moet >0 teruggeven 
	 * wanneer new beter is dan old.
	 */
	public Dijkstra(Comparator<V> isBetter) {
		this.isBetter=isBetter;
	}
	/**
	 * Vergelijkt de value met de bestaande value voor gegeven key.
	 * Wanneer de key al bestaat in het register en isBetter geeft aan dat de 
	 * nieuwe value beter is, wordt het register upgedate met de nieuwe value
	 * en geeft de functie true terug. Ander false;
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean evaluate(K key, V value) {
		V old=register.get(key);
		if (old==null || isBetter.compare(value, old)>0) {
			register.put(key, value);
			return true;
		}
		return false;
	}
	public V getValue(K key) {
		return register.get(key);
	}
}
