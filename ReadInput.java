import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;

class ReadInput
{

    public static final String RED = "\033[0;31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws Exception
    {
        String fi="";
        File M1 = new File("m1.txt");
        if(!M1.exists()){
            System.out.println(RED+"m1.txt is not found, please check"+ANSI_RESET);
            System.exit(0);
        }
        Scanner sc = new Scanner(M1);
        String s="";
        while (sc.hasNext()){ 
            s=s+Float.toString(sc.nextFloat());
            s=s+" ";
        }
        String s1[]=s.split(" ");
        if(Float.parseFloat(s1[0])*Float.parseFloat(s1[1])!=s1.length-2){
            System.out.println(RED+"Input files badly formatted, please check"+ANSI_RESET);
            System.exit(0);
        }
        int i=0,index=2;
        for(i=0;i<Float.parseFloat(s1[0]);i++){
            for(int j=0;j<Float.parseFloat(s1[1]);j++){
                fi=fi+(Integer.toString(i)+" "+Integer.toString(j)+" "+String.valueOf(Double.parseDouble(s1[index++])));
                fi=fi+"\n";
            }
        }
        File myObj = new File("M1.txt");
        FileWriter myWriter = new FileWriter("M1.txt");
        myWriter.write(fi);
        myWriter.close();
        fi="";

        M1 = new File("m2.txt");
        if(!M1.exists()){
            System.out.println(RED+"m2.txt is not found, please check"+ANSI_RESET);
            System.exit(0);
        }
        sc = new Scanner(M1);
        s="";
        while (sc.hasNext()){ 
            s=s+Float.toString(sc.nextFloat());
            s=s+" ";
        }
        String s2[]=s.split(" ");
        if(Float.parseFloat(s2[0])*Float.parseFloat(s2[1])!=s2.length-2){
            System.out.println(RED+"Input files badly formatted, please check"+ANSI_RESET);
            System.exit(0);
        }
        index=2;
        for(i=0;i<Float.parseFloat(s2[0]);i++){
            for(int j=0;j<Float.parseFloat(s2[1]);j++){
                fi=fi+(Integer.toString(i)+" "+Integer.toString(j)+" "+String.valueOf(Double.parseDouble(s2[index++])));
                fi=fi+"\n";
            }
        }
        if(Float.parseFloat(s2[0])!=Float.parseFloat(s1[1])){
            System.out.println(RED+"Matrices can't be multiplied, please check the dimensions"+ANSI_RESET);
            System.exit(0);
        }
        myObj = new File("M2.txt");
        myWriter = new FileWriter("M2.txt");
        myWriter.write(fi);
        myWriter.close();

        System.out.println("\"M1.txt\" and \"M2.txt\" are created.");
    }
}