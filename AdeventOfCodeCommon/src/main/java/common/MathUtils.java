package common;

import java.util.List;

public class MathUtils {

	public static Long sumOf(List<Long> numbers) {
		return numbers.stream().mapToLong(n->n).sum();
	}
	
	public static Long multiplyOf(List<Long> numbers) {
		if(numbers.isEmpty())
			return 0L;
		return numbers.stream().mapToLong(n->n).reduce(1L,Math::multiplyExact);
	}
}
