package aoc2022.day02;

import common.main.AbstractMainMaster;

public class Main02 extends AbstractMainMaster<Long>{
    public static void main(String args[]) {
		new Main02()
        //.testMode()
        .nolog()
        .start();
    }

    @Override
    public void beforeEach() {
        super.beforeEach();
    }

    @Override
    public Long star1() {
        return (long) loadInputByLine().stream()
            .map(s->s.toCharArray())
            .mapToInt(this::scoreStar1)
            .sum();
    }

    private int scoreStar1(char[] game) {
        int score=0;
        score+=game[2]=='X'?1:0;
        score+=game[2]=='Y'?2:0;
        score+=game[2]=='Z'?3:0;
        log("---score:"+score);
        switch (game[0]) {
            case 'A': // Rock
              switch (game[2]) {
                case 'X': // Rock
                    score+=3; break;
                case 'Y': // Paper
                    score+=6;
                    break;
                case 'Z': // Scissor
              }
              break;
            case 'B': // Paper
              switch (game[2]) {
                case 'X': // Rock
                    break;
                case 'Y': // Paper
                    score+=3;
                    break;
                case 'Z': // Scissor
                    score+=6;
              }
              break;
            case 'C': // Scissor
              switch (game[2]) {
                case 'X': // Rock
                    score+=6; break;
                case 'Y': // Paper
                    break;
                case 'Z': // Scissor
                    score+=3;
              }
              
        }
        log("totscore:"+score);
        return score;
    }
    @Override
    public Long star2() {
        return (long) loadInputByLine().stream()
            .map(s->s.toCharArray())
            .mapToInt(this::scoreStar2)
            .sum();
    }

    private int scoreStar2(char[] game) {
        int score=0;
        score+=game[2]=='Y'?3:0;
        score+=game[2]=='Z'?6:0;
        log("---score:"+score);
        switch (game[0]) {
            case 'A': // Rock
              switch (game[2]) {
                case 'X': // Paper
                    score+=3; break;
                case 'Y': // Draw
                    score+=1;
                    break;
                case 'Z': // Win
                    score+=2;
              }
              break;
            case 'B': // Paper
              switch (game[2]) {
                case 'X': // Loose
                    score+=1;
                    break;
                case 'Y': // Paper
                    score+=2;
                    break;
                case 'Z': // Scissor
                    score+=3;
              }
              break;
            case 'C': // Scissor
              switch (game[2]) {
                case 'X': // Loose
                    score+=2; break;
                case 'Y': // Draw
                    score+=3;
                    break;
                case 'Z': // Win
                    score+=1;
              }
        }
        log("totscore:"+score);
        return score;
    }
    
}
