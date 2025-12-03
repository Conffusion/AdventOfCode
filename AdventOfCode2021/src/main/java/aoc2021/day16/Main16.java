package aoc2021.day16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import common.main.AbstractMainMaster;

public class Main16 extends AbstractMainMaster<Long> {
	// input in exploded binary formaat 
	char[] binaryInput;

	public static void main(String[] args) {
		new Main16()
		.nolog()
		//.testMode()
		.start();
	}

	@Override
	public void beforeEach() {
		StringBuilder tempBinaryInput=new StringBuilder();
		// lees de Hex text in en converteer elk teken naar binary formaat (lengte=4)
		for(char c:loadInputToString().toCharArray()) {
			tempBinaryInput.append(hexToBinMap.get(c));
		}
		binaryInput=tempBinaryInput.toString().toCharArray();
	}

	@Override
	public Long star1() {
		InputParser ip=new InputParser(binaryInput);
		Packet rootPacket=parsePacket(ip);
		
		return rootPacket.star1VersionSum();
	}

	@Override
	public Long star2() {
		// TODO Auto-generated method stub
		return null;
	}

	private Packet parsePacket(InputParser input) {
		Packet packet=new Packet();
		packet.setVersion(input.readVersion());
		packet.setTypeID(input.readTypeID());
		if(packet.typeID==4)
			packet.setLiteralValue(input.readLiteral());
		else {
			// operator packet
			char lengthType=input.readBin();
			packet.binLength++;
			int readLength=0;
			if(lengthType=='0') {
				// lengte van packet uitgedrukt in aantal bits
				int subpacketLength=Integer.parseInt(new String(input.readBin(15)), 2);
				while(readLength<subpacketLength) {
					Packet subPacket=parsePacket(input);
					readLength+=subPacket.binLength;
					packet.addSubPacket(subPacket);
				}
			} else {
				// lengte van packet uitgedrukt in aantal subpackets
				int totSubPackets=Integer.parseInt(new String(input.readBin(11)),2);
				for(int pi=0;pi<totSubPackets;pi++) {
					Packet subPacket=parsePacket(input);
					readLength+=subPacket.binLength;	
					packet.addSubPacket(subPacket);
				}
			}
			// tel totale binLength van subpackets op bij binLength van huidig packet ;
			packet.binLength+=readLength;
		}
		return packet;
	}
	static class Packet {
		long version;
		int typeID;
		long literalValue;
		// total number of bin chars this packet represents (including version,typeId,...)
		int binLength;
		List<Packet> subPackets=new ArrayList<>();
		public void setVersion(int version) {
			this.version=version;
			binLength+=3;
		}
		public void setTypeID(int typeId) {
			this.typeID=typeId;
			binLength+=3;
		}
		public void setLiteralValue(LiteralValue lValue) {
			literalValue=lValue.value;
			binLength+=lValue.binLength;
		}
		public void addSubPacket(Packet packet) {
			subPackets.add(packet);
			binLength+=packet.binLength;
		}
		public long star1VersionSum() {
			return version+subPackets.stream().collect(Collectors.summingLong(p->p.star1VersionSum()));
		}
	}

	static HashMap<Character, String> hexToBinMap  = new HashMap<Character, String>();
	static {
		hexToBinMap.put('0', "0000");
		hexToBinMap.put('1', "0001");
		hexToBinMap.put('2', "0010");
		hexToBinMap.put('3', "0011");
		hexToBinMap.put('4', "0100");
		hexToBinMap.put('5', "0101");
		hexToBinMap.put('6', "0110");
		hexToBinMap.put('7', "0111");
		hexToBinMap.put('8', "1000");
		hexToBinMap.put('9', "1001");
		hexToBinMap.put('A', "1010");
		hexToBinMap.put('B', "1011");
		hexToBinMap.put('C', "1100");
		hexToBinMap.put('D', "1101");
		hexToBinMap.put('E', "1110");
		hexToBinMap.put('F', "1111");
	}
	
	static class InputParser {
		char[] binaryInput;
		int index=0;
		
		public InputParser(char[] binaryInput) {
			this.binaryInput = binaryInput;
		}
		public int readVersion() {
			return Integer.parseInt(new String(readBin(3)), 2);
		}
		public int readTypeID() {
			return Integer.parseInt(new String(readBin(3)), 2);
		}
		public LiteralValue readLiteral() {
			StringBuilder numBuilder=new StringBuilder();
			int length=0;
			while(true) {
				char firstBit=readBin();
				numBuilder.append(readBin(4));
				length+=5;
				if(firstBit=='0')
					break;
			}
			//System.out.println("Literal:"+numBuilder.toString()+" ="+Long.parseLong(numBuilder.toString(), 2));
			return new LiteralValue(Long.parseLong(numBuilder.toString(), 2),length);
		}
		
		/**
		 * leest de volgende 'length' chars van de binaryInput en zet de index vooruit.
		 * @param length aantal chars te lezen van binaryInput
		 * @return gelezen characters
		 */
		public char[] readBin(int length) {
			char[] subset=new char[length];
			System.arraycopy(binaryInput, index, subset, 0, length);
			index+=length;
			return subset;
		}
		private char readBin() {
			char c=binaryInput[index];
			index++;
			return c;
		}
	}
	static record LiteralValue (long value,int binLength) {}
}
