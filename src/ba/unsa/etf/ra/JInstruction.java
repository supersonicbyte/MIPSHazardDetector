package ba.unsa.etf.ra;

public class JInstruction extends Instruction{
    private String adress;

    public JInstruction(String name, String adress) {
        super(name);
        this.adress = adress;
    }
    public JInstruction(String label, String name, String adress) {
        super(label, name);
        this.adress = adress;
    }

}
