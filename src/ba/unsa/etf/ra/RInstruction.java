package ba.unsa.etf.ra;

public class RInstruction extends Instruction {
    private String rt;
    private String rs;
    private String rd;

    public RInstruction(String name, String rd, String rs, String rt) {
        super(name);
        this.rt = rt;
        this.rs = rs;
        this.rd = rd;
    }

    public RInstruction(String label, String name, String rd, String rs, String rt) {
        super(label, name);
        this.rt = rt;
        this.rs = rs;
        this.rd = rd;
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

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd;
    }

    @Override
    public String toString() {
        return super.toString() + " " + getRd()  + " " + getRs() + " " + getRt();
    }
}
