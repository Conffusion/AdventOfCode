package aoc2024.day09;
import common.main.AbstractMainMaster;

/**
 * disk defragmenteren door files te verplaatsen 
 */
public class Main09 extends AbstractMainMaster<Long> {
    public static void main(String[] args) {
        new Main09()
            //.testMode()
            //.withFileOutput()
            .nolog()
           .start();
    }
    char[] input;
    int outputLength;
    int[] disk;
    
    @Override
    public void beforeEach() {
    	outputLength=0;
    	input=loadInputToString().toCharArray();
    	for(char c:input)
    		outputLength+=(c-'0');
    	System.out.println("output lengte:"+outputLength);
    	// initialize disk
    	disk=new int[outputLength];
    	int fid=-1;
    	int fo=0; // cursor in output; staat op eerste lege plaats
    	for(int fi=0;fi<input.length;fi++) {
    		if(fi%2==0) {
    			fid++;
    			// oneven positie (even id) in input-> file
    			fo=writeFile(fid,input[fi]-'0',fo,disk);
    		} else {
    			// oneven positie (oneven id) in input -> space
    			fo=writeFile(-1,input[fi]-'0',fo,disk);
    		}
    	}
        logln("disk:"+logArray(disk));
    }
    /**
     * alle lege plaatsen met delen van laatste file
     */
    // antwoord : 6519155389266
    public Long star1() {
        // spaces opvullen
        int fi=0; // cursor frontend
        int bi=disk.length-1; // lees index backend
        while(true) {
        	while(disk[bi]==-1)bi--;
        	if(fi>=bi) break;
        	if(disk[fi]==-1) {
        		disk[fi]=disk[bi];
        		disk[bi]=-1;
        		bi--;
        	}
        	fi++;
        }
        logln(logArray(disk));
        
        return checksum(disk);
    }
    /**
     * enkel volledige files verplaatsen wanneer ze passen
     */
    // antwoord : 6547228115826
    public Long star2() {
    	logln("input:"+new String(input));
    	// bevat de lege plaatsen op de disk
    	BlockRange[] spaces=new BlockRange[input.length/2];
    	BlockRange[] files=new BlockRange[input.length/2+1];
        int di=0;
        int i=0;
        while (i<input.length) {
        	int blocklen=(input[i]-'0');
        	if(i%2==0) {
        		// file
        		files[i/2]=new BlockRange(di,blocklen).id(i/2);
        	} else {
        		spaces[i/2]=new BlockRange(di,blocklen);
        	}
        	di+=blocklen; // space lengte
        	i++;
        }
        // nu bevat spaces alle lege plaatsen
        int bi=files.length-1;
        
        while(bi>=2) {
        	BlockRange file=files[bi];
        	spaceLoop:
        	for(int s=0;s<bi;s++) {
        		if(spaces[s].size>=file.size) {
        			// move file to space
        			deleteFile(file,disk);
        			file.pos=spaces[s].pos;
        			spaces[s].pos+=file.size;
        			spaces[s].size-=file.size;
        			writeFile(file,disk);
        			break spaceLoop;
        		}
        	}
        	bi--;
        	//logln(""+bi+"-disk"+logArray(disk));
        }
        
        return checksum(disk);
    }
    
    private static class BlockRange {
    	int id=-1;
    	int pos; // startpositie van de block range
    	int size; // lengte van de block range
    	
		public BlockRange(int pos, int size) {
			this.pos = pos;
			this.size = size;
		}
		
		public BlockRange id(int id) {
			this.id=id;
			return this;
		}

		@Override
		public String toString() {
			return "[id=" + id + ", pos=" + pos + ", size=" + size + "]";
		}
		
    }
    private void deleteFile(BlockRange file,int[] pdisk) {
    	for(int i=0;i<file.size;i++)
    		pdisk[file.pos+i]=-1;
    }
    private void writeFile(BlockRange file,int[] pdisk) {
    	//logln("Writing file: "+file);
    	for(int i=0;i<file.size;i++)
    		pdisk[file.pos+i]=file.id;
    }

    private int writeFile(int fileid, int length, int startpos,int[] pdisk) {
    	for(int i=0;i<length;i++)
    		pdisk[startpos+i]=fileid;
    	return startpos+length;
    }
    private Long checksum(int[] pdisk) {
    	Long result=0L;
    	for(int i=0;i<pdisk.length;i++) {
        	if(pdisk[i]>0)
        		result+=pdisk[i]*i;
        }
        return result;
    }
}