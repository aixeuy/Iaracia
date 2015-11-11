package main;

import Asset.Citta;
import Asset.Guerra;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Dio {
    public static Citta[] citta;
    public static int numCitta=6;
    public static int orototale=14*(numCitta)-1;
    public static int oromercato;
    public static int oromancanza;
    public static int[][] nesso;
    public static void init(){
        citta=new Citta[numCitta];
        citta[0]=new Citta("milano",0);
        citta[1]=new Citta("turino",1);
        citta[2]=new Citta("genova",2);
        citta[3]=new Citta("venezia",3);
        citta[4]=new Citta("firenze",4);
        citta[5]=new Citta("bologna",5);
        oromercato=orototale;
        oromancanza=0;
        for(Citta c:citta){
            c.oro=orototale/numCitta;
            oromercato-=c.oro;
        }
        nesso=new int[numCitta][numCitta];
        for(int i=0;i<numCitta;i++){
            for(int j=0;j<numCitta;j++){
                nesso[i][j]=13;
            }
        }
    }
    public static void riformareGoverno(Citta nuovo){
        int[] inv=new int[numCitta];
        for(int i=0;i<numCitta;i++){
            inv[i]=citta[i].investire();
            System.out.print(citta[i].nome+" investa "+inv[i]+"IR;");
        }
        System.out.print("\n");
        int ind=0;int max=0;
        for(int i=0;i<numCitta;i++){
            if(inv[i]>max){
                max=inv[i];ind=i;
            }
        }
        int somma=0;
        for(int i=0;i<numCitta;i++){
            somma+=inv[i];
        }
        if(nuovo.superiore!=null&&inv[nuovo.superiore.index]==max){
            System.out.println(nuovo.nome+" e controllato(a) ancora di "+nuovo.superiore.nome);
        }
        else if(max>somma/2&&citta[ind].influenza*Math.random()>nuovo.influenza*Math.random()){
            nuovo.superiore=citta[ind];
            System.out.println(nuovo.nome+" e controllato(a) di "+citta[ind].nome);
            nuovo.influenza-=max;
            citta[ind].influenza+=max;
        }
        else{
            nuovo.influenza-=somma;
            for(int i=0;i<numCitta;i++){
                citta[i].influenza+=inv[i];
            }
            nuovo.superiore=null;
            System.out.println("nuovo(a) " + nuovo.nome + " rimane indipendente");
        }
        for(Guerra g:nuovo.guerra){
            //g.io.terminare(g);
            g.nemica.terminare(g.opposto);
        }
        nuovo.guerra=new ArrayList<Guerra>();
        nuovo.oro+=somma;
        System.out.print(nuovo.nome+" con "+nuovo.oro+"IR");
    }
    public static boolean prestito(Citta vuole){
        boolean ottiene=false;
        for(Citta c :citta){
            if(c==vuole){
                continue;
            }
            if(Math.random()*nesso[c.index][vuole.index]>0.8*medio(c.index)){
                int denaro=(int)(3*Math.random()+1);
                if(denaro>c.oro/3){continue;}
                ottiene=true;
                c.oro-=denaro;
                c.influenza+=denaro;
                vuole.oro+=denaro;
                vuole.influenza-=denaro;
                System.out.println(c.nome+" presta "+denaro+"IR a "+vuole.nome);
                nesso[c.index][vuole.index]++;
                nesso[vuole.index][c.index]+=denaro;
            }
        }
        return ottiene;
    }
    public static Citta laPiuforte(){
        int max=0;Citta tr=citta[0];
        for(Citta c:citta){
            if(c.influenza>max){
                max=c.influenza;
                tr=c;
            }
        }
        for(Citta c:citta){
            if(c==tr){
                continue;
            }
            if(c.influenza==tr.influenza){
                return null;
            }
        }
        return tr;
    }
    public static int medio(int i){
        int somma =0;
        for(int j=0;j<numCitta;j++){
            somma+=nesso[i][j];
        }
        somma-=nesso[i][i];
        nesso[i][i]=somma/(numCitta-1);
        return somma/(numCitta-1);
    }
    public static void main(String[] args) {
	    init();
        int ind=0;
        boolean fermare=false;
        java.util.Scanner sc;
        String s="";
        boolean veloce=false;
        while(true) {
            Citta questa=citta[ind];
            if(fermare||!veloce) {
                sc = new java.util.Scanner(System.in);
                s = sc.nextLine();
            }
            while(!s.equals("")){
                if(s.equals("l")){
                    System.out.println("nome      ec po mi superiore");
                    for(Citta c:citta){
                        System.out.println(c.toString());
                    }
                }
                else if(s.equals("n")){
                    System.out.println("          mi tu ge ve fi bo");
                    for(int i=0;i<numCitta;i++){
                        System.out.print(citta[i].nome);
                        for(int k=citta[i].nome.length();k<10;k++){
                            System.out.print(" ");
                        }
                        for(int j=0;j<numCitta;j++){
                            System.out.print(nesso[i][j]+" ");
                        }
                        System.out.print("\n");
                    }
                }
                else if(s.equals("g")){
                    for(int i=0;i<numCitta;i++){
                        System.out.print(citta[i].nome);
                        for(int k=citta[i].nome.length();k<10;k++){
                            System.out.print(" ");
                        }
                        for(Guerra g:citta[i].guerra){
                            System.out.print(g.nemica.nome+" ");
                        }
                        System.out.print("\n");
                    }
                }
                else if(s.equals("v")){
                    veloce=!veloce;
                }
                sc = new java.util.Scanner(System.in);
                s = sc.nextLine();
            }
            fermare=questa.fare();
            ind++;
            if(ind>=numCitta){
                if(oromancanza>=1){
                    oromancanza--;
                    oromercato++;
                }
                ind=0;
            }
        }
    }
}
