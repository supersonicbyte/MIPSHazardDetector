package ba.unsa.etf.ra;

public class JInstruction extends Instruction{
    private String adress;

    public JInstruction(String name, String adress) {
        super(name);
        this.adress = adress.toUpperCase();
    }
    public JInstruction(String label, String name, String adress) {
        super(label, name);
        this.adress = adress.toUpperCase();
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress.toUpperCase();
    }
}
