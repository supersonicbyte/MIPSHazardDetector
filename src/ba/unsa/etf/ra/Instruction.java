package ba.unsa.etf.ra;

import java.io.Serializable;

public abstract class Instruction implements Serializable {
    private String label;
    private String name;

    public Instruction() {
    }

    public Instruction(String name) {
        this.name = name.toUpperCase();
        this.label=null;
    }

    public Instruction(String label, String name) {
        this.label = label;
        this.name = name.toUpperCase();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String dajOdredisni();

    @Override
    public String toString() {
        if(label != null){
            return label + ":  " + name;
        }
        return name;
    }
}
