package aoc2020.day05;
interface S{static void main(String[]t)throws Exception{final var a=new int[884];org.apache.commons.io.FileUtils.readLines(new java.io.File("5"),"UTF8").stream().mapToInt(s->Integer.parseInt(s.replaceAll("[B,R]","1").replaceAll("[F,L]","0"),2)).forEach(i->a[i]=1);for(int i=7;i<883;i++)if(a[i-1]+a[i+1]>1&a[i]==0)System.out.print(i);}}
