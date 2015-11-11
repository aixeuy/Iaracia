package Asset;

import main.Dio;

/**
 * Created by Win7uX32 on 2015/7/10.
 */
public class Guerra {
    public Citta io;
    public Citta nemica;
    int volere;
    int tempo;
    int vittoria;
    int sconfitta;
    public Guerra opposto;
    public Guerra(Citta io,Citta nemica,int volere){
        this.io=io;
        this.nemica=nemica;
        this.volere=volere;
        tempo=0;
        vittoria=0;
        sconfitta=0;
    }
    public void battere(){
        io.oro--;
        if(io.militare*Math.random()>nemica.militare*Math.random()){
            nemica.oro-=3;
            Dio.oromancanza+=3;
            vittoria++;
            io.influenza+=1;
            nemica.influenza-=1;
            System.out.println(",vittoria");
            io.militare++;
            if(nemica.militare<0){
                nemica.militare=1;
            }
            if(nemica.oro<=0){
                int denaro=0-nemica.oro;
                io.oro+=nemica.oro;
                nemica.oro=0;
                denaro=(int)(io.oro/3*Math.random())+1;
                io.oro-=denaro;
                nemica.oro+=denaro;
                nemica.superiore=io;
                io.influenza+=volere;
                for(int i=0;i<Dio.numCitta;i++) {
                    Dio.nesso[i][io.index]-=3;
                }
                Dio.nesso[io.index][io.index]+=4;
                //nemica.oro-=volere;
                io.terminare(this);
                nemica.terminare(this.opposto);
                System.out.println(" "+nemica.nome+" e controllato di "+io.nome);
            }
            else{
                mandare();
            }
        }
        else{
            io.influenza-=1;
            nemica.influenza+=1;
            sconfitta++;
            System.out.println(",sconfitta");
        }
    }
    public boolean accettare(int vo){
        /*double li=(nemica.oro/(3.0*vittoria/tempo+0.1));
        double qui=(io.oro/(3.0*sconfitta/tempo+0.1));*/
        if(io.oro/(sconfitta+0.1)>=nemica.oro/(vittoria+0.1)){
            return false;
        }
        else{
            if(vo>=io.oro){
                return false;
            }
            if(io.oro-3.0*sconfitta/tempo>vo){
                return false;
            }
        }
        return true;
    }
    public void mandare(){
        if(this.opposto.accettare(this.volere)){
            io.oro+=volere;
            nemica.oro-=volere;
            io.influenza+=volere;
            if(volere==0){
                io.influenza+=3;
            }
            io.terminare(this);
            nemica.terminare(this.opposto);
            Dio.nesso[nemica.index][io.index]-=volere;
            System.out.println("esige "+volere+"IR,"+"accettatto");
        }
        else{
            System.out.println("esige "+volere+"IR,"+"rifiutato");
            volere-=(int)(volere/2*Math.random())+1;
            if(volere<=0){
                volere=1;
            }
        }
    }
}
