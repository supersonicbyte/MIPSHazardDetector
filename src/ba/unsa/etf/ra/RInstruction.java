package ba.unsa.etf.ra;

import java.awt.*;
import java.io.Serializable;

public class RInstruction extends Instruction implements Serializable {
    private String rt;
    private String rs;
    private String rd;

    public RInstruction(String name, String rd, String rs, String rt) {
        super(name);
        this.rt = rt.toUpperCase();
        this.rs = rs.toUpperCase();
        this.rd = rd.toUpperCase();
    }

    public RInstruction(String label, String name, String rd, String rs, String rt) {
        super(label, name);
        this.rt = rt.toUpperCase();
        this.rs = rs.toUpperCase();
        this.rd = rd.toUpperCase();
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

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd.toUpperCase();
    }

    @Override
    public String toString() {
        return super.toString() + " " + getRd()  + " " + getRs() + " " + getRt();
    }
}
