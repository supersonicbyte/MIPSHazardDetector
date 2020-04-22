package ba.unsa.etf.ra;

public class IInstruction extends Instruction {
    private String rt;
    private String rs;
    private String immidiate;

    public IInstruction() {
    }

    public IInstruction(String name, String rt, String rs, String immidiate) {
        super(name);
        this.rt = rt;
        this.rs = rs;
        this.immidiate = immidiate;
    }

    public IInstruction(String label, String name, String rt, String rs, String immidiate) {
        super(label, name);
        this.rt = rt;
        this.rs = rs;
        this.immidiate = immidiate;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getImmidiate() {
        return immidiate;
    }

    public void setImmidiate(String immidiate) {
        this.immidiate = immidiate;
    }
}
