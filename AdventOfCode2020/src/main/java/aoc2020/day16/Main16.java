package aoc2020.day16;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aoc2020.common.MainMaster;

public class Main16 extends MainMaster {
	static Pattern fieldDef = Pattern.compile("(.*): (\\d+)-(\\d+) or (\\d+)-(\\d+)");
	List<Field> fields = new ArrayList<>();
	Long[] myticket;
	List<Long[]> nearby = new ArrayList<>();

	class Field {
		String name;
		long from1;
		long from2;
		long to1;
		long to2;
		long position;

		public Field(String name, long from1, long to1, long from2, long to2) {
			super();
			this.name = name;
			this.from1 = from1;
			this.from2 = from2;
			this.to1 = to1;
			this.to2 = to2;
		}

		public boolean isValid(long num) {
			return (from1 <= num && num <= to1) || (from2 <= num && num <= to2);
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public long validate(long num) {
		for (Field f : fields)
			if (f.isValid(num))
				return 0;
		return num;
	}

	/**
	 * geeft false van zodra 1 waarde met geen enkel field klopt
	 * 
	 * @param ticket
	 * @return
	 */
	public boolean isValid(Long[] ticket) {
		for (Long value : ticket) {
			boolean valid = false;
			for (Field f : fields)
				valid = valid || f.isValid(value);
			if (!valid)
				return false;
		}

		return true;
	}

	public Long[] convert(String ticket) {
		String[] values = ticket.split(",");
		Long[] result = new Long[values.length];
		for (int i = 0; i < values.length; i++)
			result[i] = Long.parseLong(values[i]);
		return result;
	}

	public void loadData(String filename) {
		List<String> data = super.loadInput(16, filename);
		int zone = 1; // 1=fields, 2=your ticket, 3=nearby tickets
		for (String line : data) {
			Matcher m = fieldDef.matcher(line);
			if (line.trim().length() == 0)
				continue;
			if (m.matches()) {
				// field definition
				fields.add(new Field(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)),
						Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5))));
			} else if (line.equals("your ticket:"))
				zone = 2;
			else if (line.equals("nearby tickets:"))
				zone = 3;
			else if (zone == 2)

				myticket = convert(line);
			else if (zone == 3) {
				nearby.add(convert(line));
			}
		}
	}

	public Map.Entry<Field, boolean[]> findSingle(Map<Field, boolean[]> matrix, int column) {
		Map.Entry<Field, boolean[]> result = null;
		for (Map.Entry<Field, boolean[]> e : matrix.entrySet()) {
			if (e.getValue()[column]) {
				if (result != null)
					return null; // meerdere true's gevonden
				result = e;
			}
		}
		return result;
	}

	public List<Field> clean(Map<Field, boolean[]> options) {
		Set<Field> allFields = new HashSet<>();
		List<Field> departureFields = new ArrayList<>();
		int prevSize = -1;
		while (prevSize != allFields.size()) {
			prevSize = allFields.size();
			for (int i = 0; i < myticket.length; i++) {
				Map.Entry<Field, boolean[]> result = findSingle(options, i);
				if (result != null && !allFields.contains(result.getKey())) {
					System.out.println("Gevonden " + result.getKey() + ",pos:" + i);
					// zet ander kolommen voor dit veld op false
					Arrays.fill(result.getValue(), false);
					result.getValue()[i] = true;
					result.getKey().position = i;
					allFields.add(result.getKey());
					if (result.getKey().name.startsWith("departure") && !departureFields.contains(result.getKey())) {
						departureFields.add(result.getKey());
						if (departureFields.size() == 6)
							return departureFields;
					}
				}
			}
		}
		System.err.println("Geen verbeteringen meer :(. Gevonden:" + departureFields.size());
		return null;
	}

	public void star2(String filename) {
		loadData(filename);
		Map<Field, boolean[]> options = new HashMap<>();
		boolean[] init = new boolean[myticket.length];
		Arrays.fill(init, true);
		for (Field f : fields)
			options.put(f, Arrays.copyOf(init, myticket.length));
		for (Long[] ticket : nearby) {
			if (isValid(ticket)) {
				// geldig ticket
				for (int i = 0; i < ticket.length; i++) {
					for (Map.Entry<Field, boolean[]> e : options.entrySet()) {
						if (e.getValue()[i]) {
							boolean valid = e.getKey().isValid(ticket[i]);
							if (!valid & i == 0) {
								if (log)
									logln("Positie " + i + ", waarde:" + ticket[i] + " is niet valid for "
											+ e.getKey());
							}
							e.getValue()[i] = e.getValue()[i] && valid;
						}
					}
				}
			}
		}
		if (log) {
			System.out.println("before clean:");
			for (Map.Entry<Field, boolean[]> e : options.entrySet()) {
				System.out.println("Field " + e.getKey().name + ": " + Arrays.toString(e.getValue()));
			}
		}
		clean(options);
		if (log) {
			System.out.println("after clean:");
			for (Map.Entry<Field, boolean[]> e : options.entrySet()) {
				System.out.println("Field " + e.getKey().name + ": " + Arrays.toString(e.getValue()));
			}
		}
		long result = 1;
		for (Map.Entry<Field, boolean[]> e : options.entrySet()) {
			if (e.getKey().name.startsWith("departure")) {
				if(log)logln("Field " + e.getKey().name + ": " + Arrays.toString(e.getValue()));
				boolean found = false;
				for (int i = 0; i < e.getValue().length; i++)
					if (e.getValue()[i]) {
						if (found)
							System.err.println("Voor " + e.getKey() + " meerdere plaatsen gevonden");
						result *= myticket[i];
						found = true;
					}
			}
		}
		System.out.println("***STAR2:" + result);
	}

	// Answer: 28884
	public void star1(String filename) {
		List<String> data = super.loadInput(16, filename);
		int zone = 1; // 1=fields, 2=your ticket, 3=nearby tickets
		long errorRate = 0;
		for (String line : data) {
			Matcher m = fieldDef.matcher(line);
			if (line.trim().length() == 0)
				continue;
			if (m.matches()) {
				// field definition
				fields.add(new Field(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)),
						Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5))));
			} else if (line.equals("your ticket:"))
				zone = 2;
			else if (line.equals("nearby tickets:"))
				zone = 3;
			else if (zone == 3) {
				for (String value : line.split(",")) {
					errorRate += validate(Long.parseLong(value));
				}
			}
		}
		System.out.println("***STAR1:" + errorRate);
	}

	public static void main(String[] args) {
		Main16 m = new Main16();
		m.log=false;
		m.timer(()->m.star2("input.txt"));
	}

}
