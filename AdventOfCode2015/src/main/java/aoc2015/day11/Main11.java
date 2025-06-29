package aoc2015.day11;

import aoc2015.common.MainMaster;

/**
 * Base 26 tellen
 */
public class Main11 extends MainMaster<String> {

	public static void main(String[] args) {
		new Main11()
		//.withTestMode()
		.start();
	}
	// paswoord omgezet in integers (a-> 0, b->0,...)
	int[] currPassword;
	
	@Override
	public String star1() {
		String input="hxbxwxba";
		if(testMode)
			input="abcdefgh";
		currPassword=toNumbers(input);
		while(!isValid()) {
			increase();
			System.out.println(toText(currPassword));
		}
		return toText(currPassword);
	}

	int[] toNumbers(String word) {
		int[] response=new int[word.length()];
		for(int i=0;i<word.length();i++)
			response[i]=word.charAt(i)-'a';
		return response;
	}
	String toText(int[] password) {
		char[]out=new char[password.length];
		for(int i=0;i<password.length;i++) {
			out[i]=(char)(password[i]+'a');
		}
		return new String(out);
	}
	void increase() {
		int index=currPassword.length-1;
		while(increase(index)>0) {
			index--;
			if(index==-1) {
				int[]newpw=new int[currPassword.length+1];
				System.arraycopy(currPassword, 0, newpw, 1, currPassword.length);
				newpw[0]=0;
			}
		}
	}
	/**
	 * verhoogt het character op positie index. 
	 *  
	 * @param value huidige waarde. deze wordt direct aangepast
	 * @param index positie die moet verhoogd worden
	 * @return 1 als er een overflow is (z->a), anders 0
	 */
	int increase(int index) {
		if(currPassword[index]==25) {
			for(int i=index;i<currPassword.length;i++)
				currPassword[i]=0;
			return 1;
		}
		int waarde=currPassword[index];
		int inc=1;
		// i(=8), o(=14), l(=11) -> direct naar volgende switchen
		if(currPassword[index]==8 || currPassword[index]==11||currPassword[index]==14)
			inc=2;
		currPassword[index]= waarde+inc;
		return 0;
	}
	
	boolean isValid() {
		boolean straat=false;
		int paren=0;
		int paar=-1;
		for(int i=0;i<currPassword.length;i++) {
			if(i<currPassword.length-3) {
				// zoeken naar abc, bcd,cde, def,... 
				if(currPassword[i]+1==(currPassword[i+1]) && currPassword[i+1]+1==(currPassword[i+2])) {
					straat=true;
				}
			}
			if(i<=currPassword.length-2 && paren<2) {
				// zoeken naar paar
				if (currPassword[i]==(currPassword[i+1]) && paar!=currPassword[i]) {
					if(paren==0) {
						paar=currPassword[i];
					}
					paren++;
				}
			}
		}
		return straat && paren==2;
	}
	
	@Override
	public String star2() {
		String input="hxbxxzaa";
		currPassword=toNumbers(input);
		while(!isValid()) {
			increase();
			System.out.println(toText(currPassword));
		}
		return toText(currPassword);
	}

}
