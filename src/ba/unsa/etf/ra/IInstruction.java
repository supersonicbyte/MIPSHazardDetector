package ba.unsa.etf.ra;

import java.io.Serializable;

public class IInstruction extends Instruction implements Serializable {
    private String rt;
    private String rs;
    private String immidiate;

    public IInstruction() {
    }

    public IInstruction(String name, String rt, String rs, String immidiate) {
        super(name);
        this.rt = rt.toUpperCase();
        this.rs = rs.toUpperCase();
        this.immidiate = immidiate;
    }

    public IInstruction(String label, String name, String rt, String rs, String immidiate) {
        super(label, name);
        this.rt = rt.toUpperCase();
        this.rs = rs.toUpperCase();
        this.immidiate = immidiate;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt.toUpperCase();
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs.toUpperCase();
    }

    public String getImmidiate() {
        return immidiate;
    }

    public void setImmidiate(String immidiate) {
        this.immidiate = immidiate.toUpperCase();
    }

    @Override
    public String dajOdredisni() {
        return rt;
    }

    @Override
    public String toString() {
        return super.toString() + " " + getRt()  + " " + getRs() + " " + getImmidiate();
    }
}
