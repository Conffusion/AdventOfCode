package aoc2021.day18;

public class SFNumberParser {
    private char[] line;
    private int index=0;

    public SFNumberParser(String line) {
    	this.line=line.toCharArray();
    }
    public SFNumberParser (char[] line) {
        this.line=line;
    }

    public SFNumber parse() {
        SFNumber sfnum=new SFNumber();
        if(Character.isDigit(line[++index]))
            sfnum.part1(line[index]-'0');
        else {
            sfnum.sfnum1(parse());
        }
        // skip comma
        index++;
        if(Character.isDigit(line[++index]))
            sfnum.part2(line[index]-'0');
        else
            sfnum.sfnum2(parse());
        // skip closing ]
        index++;
        return sfnum;
    }
    
    public static void main(String[] args) {
        System.out.println("expected [0,7]: "+new SFNumberParser("[0,7]").parse());
        System.out.println("expected [[[2,7],0],7],4]: "+new SFNumberParser("[[[2,7],0],7],4]").parse());
        System.out.println("magnitude of [[1,2],[[3,4],5]]: "+new SFNumberParser("[[1,2],[[3,4],5]]".toCharArray()).parse().magnitude());
        System.out.println("magnitude of [[[[0,7],4],[[7,8],[6,0]]],[8,1]]: "+new SFNumberParser("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").parse().magnitude());
        System.out.println("magnitude of [[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]: "+new SFNumberParser("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").parse().magnitude());
        System.out.println("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]: "
        		+ (new SFNumberParser("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]").parse()
        				.add(new SFNumberParser("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]").parse())));
        System.out.println(new SFNumberParser("[1,1]").parse()
        .add(new SFNumberParser("[2,2]").parse())
        .add(new SFNumberParser("[3,3]").parse())
        .add(new SFNumberParser("[4,4]").parse())
        .add(new SFNumberParser("[5,5]").parse()));
    }
}
