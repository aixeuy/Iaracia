package Asset;

import main.Dio;

import java.util.ArrayList;

/**
 * Created by Win7uX32 on 2015/7/10.
 */
public class Citta {
    public int index;
    public String nome;
    public int oro;
    public Citta superiore;
    public int influenza;
    public ArrayList<Guerra> guerra;
    public int militare;
    public Citta(String nome,int index){
        this.index=index;
        this.nome=nome;
        oro=0;
        influenza=13;
        guerra=new ArrayList<Guerra>();
        militare=13;
    }
    public boolean fare(){
        boolean fermare=true;
        if(this.oro<=0){
            if(!Dio.prestito(this)) {
                Dio.riformareGoverno(this);
                return true;
            }
        }
        System.out.print(nome);
        Citta lpf=Dio.laPiuforte();
        boolean ce=false;
        for(Guerra g:guerra){
            if(g.nemica==lpf){
                ce=true;
            }
        }
        if( superiore==null&&!ce&&lpf!= null && this!=lpf&&this.oro > lpf.oro - 3&&this.militare>lpf.militare&&
                Math.random()>0.8&&oro>10){
            Guerra g=new Guerra(this,lpf,Math.min(Math.abs(lpf.oro-this.oro),lpf.influenza-this.influenza));
            this.guerra.add(g);
            Guerra h=new Guerra(lpf,this,Math.min(Math.abs(lpf.oro-this.oro),lpf.influenza-this.influenza));
            lpf.guerra.add(h);
            g.opposto=h;
            h.opposto=g;
            System.out.print(" dichiara guerra a "+lpf.nome);
            Dio.nesso[index][lpf.index]-=2;
            Dio.nesso[lpf.index][index]-=2;
            for(int i=0;i<Dio.numCitta;i++) {
                Dio.nesso[i][index]-=3;
            }
        }
        else{
            Citta scopo=Dio.citta[(int)(Math.random()*Dio.numCitta)];
            ce=false;
            for(Guerra g:guerra){
                if(g.nemica==scopo){
                    ce=true;
                }
            }
            if(superiore==null&&scopo.superiore!=this&&!ce&&scopo!=this&&this.militare>scopo.militare&&this.oro>scopo.oro*2&&
                    Math.random()*Dio.medio(index)>0.8*Dio.nesso[index][scopo.index]&&Math.random()>0.5&&oro>10){
                Guerra g=new Guerra(this,scopo,scopo.oro);
                this.guerra.add(g);
                Guerra h=new Guerra(scopo,this,0);
                scopo.guerra.add(h);
                g.opposto=h;
                h.opposto=g;
                System.out.print(" dichiara guerra a "+scopo.nome);
                Dio.nesso[index][scopo.index]-=2;
                Dio.nesso[scopo.index][index]-=3;
                for(int i=0;i<Dio.numCitta;i++) {
                    Dio.nesso[i][index]-=3;
                    if(Dio.citta[i].superiore!=this&&i!=index&&i!=scopo.index&&Dio.nesso[i][index]<Dio.medio(i)&&Dio.nesso[i][scopo.index]>Dio.medio(i)&&Math.random()>0.3){
                        g=new Guerra(Dio.citta[i],this,5);
                        Dio.citta[i].guerra.add(g);
                        h=new Guerra(this,Dio.citta[i],0);
                        guerra.add(h);
                        g.opposto=h;
                        h.opposto=g;
                        System.out.print(","+Dio.citta[i].nome+" dichiara guerra a "+nome);
                        Dio.nesso[index][i]-=3;
                        Dio.nesso[scopo.index][i]+=3;
                    }
                }
            }
        }
        if(this.guerra.size()!=0&&Math.random()>=0.3){
            Guerra g=guerra.get((int)(guerra.size()*Math.random()));
            System.out.print(" batte con "+g.nemica.nome);
            Dio.nesso[index][g.nemica.index]-=1;
            Dio.nesso[g.nemica.index][index]-=1;
            g.battere();
        }
        else if(this==Dio.laPiuforte()&&this.oro>10){
            ArrayList<Citta> minaccia=new ArrayList<Citta>();
            for(Citta c:Dio.citta){
                if(c==this){
                    continue;
                }
                if(c.oro>=oro-3){
                    minaccia.add(c);
                }
            }
            if(minaccia.size()==1){
                Citta scopo=(Citta) minaccia.get(0);
                if(oro>=scopo.oro) {
                    attack(scopo);
                }
                else{
                    fermare=economia();;
                }
            }
            else{
                fermare=economia();
            }
        }
        else {
            fermare=economia();
        }
        System.out.println(" con "+oro+"IR in somma");
        if(this.superiore!=null&&oro>=this.superiore.oro){
            System.out.println(nome+" ottiene indipendenza");
            this.superiore=null;
        }
        return fermare;
    }
    public boolean economia(){
        this.oro--;
        this.influenza--;
        Dio.oromercato+=1;
        if(Math.random()<=1.0*Dio.oromercato/Dio.orototale){
            for(int i=0;i<Dio.numCitta;i++){
                Dio.nesso[i][index]++;
            }
            int proffito=(int)(Math.random()*Dio.oromercato);
            this.militare+=proffito/5;
            this.influenza+=proffito;
            Dio.oromercato-=proffito;
            System.out.println(" ottiene "+proffito+"IR");
            if(proffito>=5&&this.superiore!=null){
                int dona=proffito-5;
                this.superiore.oro+=dona;
                proffito=5;
                System.out.print(dona+"IR sono donati a "+superiore.nome);
                Dio.nesso[index][index]--;
            }
            this.oro+=proffito;
            return true;
        }
        return false;
    }
    public void attack(Citta c){
        oro--;
        Dio.oromercato++;
        influenza--;
        System.out.print(" attacca "+c.nome);
        Dio.nesso[index][c.index]--;
        Dio.nesso[c.index][index]--;
        if(Math.random()>0.5){
            System.out.print(",successo");
            Dio.nesso[c.index][index]--;
            c.oro-=3;
            Dio.oromancanza+=3;
        }
        else{
            System.out.print(",insuccesso");
        }
    }
    public int investire(){
        int investiento=(int)(Math.random()*oro/Dio.numCitta);
        this.oro-=investiento;
        return investiento;
    }
    public void terminare(Guerra g){
        this.guerra.remove(g);
    }
    public String toString(){
        String str="";
        str+=nome;
        for(int i=nome.length();i<10;i++){
            str+=" ";
        }
        str+=oro+" "+influenza+" "+militare+" ";
        if(superiore!=null){
            str+=superiore.nome;
        }
        return str;
    }
}
