package ba.unsa.etf.ra;

import java.io.Serializable;

public class IMemInstruction extends Instruction implements Serializable {
    private String rt;
    private String immAndRs;

    public IMemInstruction(){}

    public IMemInstruction(String name, String rt, String immAndRs) {
        super(name);
        this.rt = rt.toUpperCase();
        this.immAndRs = immAndRs;
    }

    public IMemInstruction(String label, String name, String rt, String immAndRs) {
        super(label, name);
        this.rt = rt.toUpperCase();
        this.immAndRs = immAndRs;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getImmAndRs() {
        return immAndRs;
    }

    public void setImmAndRs(String immAndRs) {
        this.immAndRs = immAndRs;
    }
    // funkcija za ekstrakciju Rs iz oblika 24(R2)
    public String getRs(){
        String s = "";
        for(int i = 0; i < immAndRs.length(); ++i){
            if(immAndRs.charAt(i) == '('){
                int j = i + 1;
                while(immAndRs.charAt(j) != ')' && j < immAndRs.length()){
                    s += immAndRs.charAt(j);
                    j++;
                }
                break;
            }
        }
        return s;
    }

    public String getImm(){
        String s = "";
        for(int i = 0; i < immAndRs.length(); ++i){
         if(!Character.isDigit(immAndRs.charAt(i))) break;
         else s += immAndRs.charAt(i);
        }
        return s;
    }

    @Override
    public String dajOdredisni() {
        if(getName().contains("L")) return rt;
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + " " + rt + " " + immAndRs;
    }
}
