package ba.unsa.etf.ra;

public class IInstruction extends Instruction {
    private String rt;
    private String rs;
    private String immidiate;

    public IInstruction() {
    }

    public IInstruction(String name, String rt, String rs, String immidiate) {
        super(name);
        this.rt = rt.toUpperCase();
        this.rs = rs.toUpperCase();
        this.immidiate = immidiate.toUpperCase();
    }

    public IInstruction(String label, String name, String rt, String rs, String immidiate) {
        super(label, name);
        this.rt = rt.toUpperCase();
        this.rs = rs.toUpperCase();
        this.immidiate = immidiate.toUpperCase();
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
}
