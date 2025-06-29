package aoc2020.day23;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Ring is een linkedlist waarbij laatste aan eerste is gekoppeld (een ring dus).
 * De waarden worden verwacht uniek te zijn waardoor {@link #elementOf(int)} een unieke waarde kan teruggeven 
 * @author walter
 *
 */
public class Ring {

	RingElement current;
	RingElement first, last;
	int maxValue=0;

	Map<Integer,RingElement>index=new HashMap<>();
	
	class RingElement {
		int value;
		RingElement next;

		public RingElement(int value) {
			super();
			this.value = value;
		}

	}

	public void add(int value) {
		RingElement newElem = new RingElement(value);
		if (first == null) {
			first = newElem;
			last = newElem;
		} else {
			last.next = newElem;
			last = last.next;
		}
		maxValue=maxValue<value?value:maxValue;
		index.put(value,newElem);
	}

	public void closeRing() {
		last.next = first;
		current = first;
	}

	/**
	 * Knipt 3 elementen na current weg en maakt cirkel terug rond
	 * 
	 * @return
	 */
	public RingElement cut(int nrOfElements) {
		RingElement cutted = current.next;
		RingElement end = current;
		for (int i = 1; i <= nrOfElements; i++) {
			end = end.next;
		}
		current.next = end.next;
		end.next = null;
		return cutted;
	}

	public void paste(RingElement newpart, RingElement after) {
		RingElement last = after.next;
		after.next = newpart;
		while (newpart.next != null)
			newpart = newpart.next;
		newpart.next = last;
	}

	public RingElement paste(RingElement newpart){
		RingElement begin=current;
		RingElement highest=current;
		int searchvalue=begin.value-1;
		while(true) {
			current=current.next;
			if(current.value>highest.value)
				highest=current;
				
			if(current==begin) {
				// we zijn rond
				if(searchvalue==0) {
					paste(newpart,highest);
					break;
				}
				searchvalue--;
			}
			if(current.value==searchvalue)
			{
				paste(newpart,current);
				break;
			}
		}
		current=begin.next;
		return current;
	}

	public static boolean contains(RingElement part,int value)
	{
		RingElement cursor=part;
		while(cursor!=null)
		{
			if (cursor.value==value)
				return true;
			cursor=cursor.next;
		}
		return false;
	}
	
	public int size() {
		RingElement cursor=current;
		int counter=0;
		do 
		{
			counter++;
			cursor=cursor.next;
		} while(cursor!=current);
		return counter;
	}
	public void newPaste(RingElement newPart) {
		int searchvalue=current.value-1;
		while(contains(newPart,searchvalue))
			searchvalue--;
		if(searchvalue==0)
		{
			searchvalue=maxValue;
			while(contains(newPart,searchvalue))
				searchvalue--;
		}
		RingElement begin=index.get(searchvalue);
		paste(newPart,begin);
		current=current.next;
	}
	
	public RingElement elementOf(int value) {
		return index.get(value);
	}
	
	public Ring next() {
		current=current.next;
		return this;
	}
	public Ring goTo(int value) {
		while(current.value!=value)
			current=current.next;
		return this;
	}
	public void walk(Consumer<RingElement> walker) {
		RingElement elem=current;
		do {
			walker.accept(elem);
			elem=elem.next;
		} while(elem!=current);
	}
	public String toString() {
		StringBuffer out=new StringBuffer();
		walk(elem->out.append(""+ elem.value+","));
		return out.toString();
	}
}
