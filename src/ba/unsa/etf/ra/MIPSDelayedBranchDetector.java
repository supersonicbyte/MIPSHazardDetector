package ba.unsa.etf.ra;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MIPSDelayedBranchDetector {
    public static ArrayList<String> rType = new ArrayList<>() {
        {
            add("add");
            add("addu");
            add("and");
            add("nor");
            add("or");
            add("sll");
            add("sllv");
            add("slt");
            add("sltu");
            add("srav");
            add("srlv");
            add("sub");
            add("subu");
            add("xor");
            add("mul");
        }
    };

    public static ArrayList<String> iTypeNoMemory = new ArrayList<>() {
        {
            add("beq");
            add("bne");
            add("addi");
            add("addiu");
            add("slti");
            add("andi");
            add("ori");
            add("xori");
            add("lui");
        }
    };

    public static ArrayList<String> iTypeMemory = new ArrayList<>() {
        {
            add("lb");
            add("lh");
            add("lw");
            add("sb");
            add("sh");
            add("sw");
        }
    };

    public static ArrayList<String> jType = new ArrayList<>() {
        {
            add("jal");
            add("j");
            add("jr");
        }
    };

    public static HashMap<String, Integer> adressMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Unesite apsolutnu putanju do .txt datoteke (npr. C:\\Users\\USERNAME\\Desktop\\xyz.txt\\): ");
        Scanner input = new Scanner(System.in);
        String filePath = input.nextLine();
        File file = new File(filePath);
        while (!file.exists()) {
            System.out.println("Datoteka ne postoji! Molimo vas unesite ponovo putanju: ");
            filePath = input.nextLine();
            file = new File(filePath);
        }
        ArrayList<Instruction> instructions;
        try {
            instructions = loadInstructions(file);
        }
        catch (Exception e){
            e.printStackTrace();
            //System.out.println(e.getMessage());
            return;
        }
        // mapiramo label - redni broj instrukcije u datoteci
        for (int i = 0; i < instructions.size(); ++i) {
            if (instructions.get(i).getLabel() != null && instructions.get(i).getLabel() != "") {
                adressMap.put(instructions.get(i).getLabel().trim(), i);
            }
        }
        ArrayList<Instruction> outputList = new ArrayList<>();
        for (int i = 0; i < instructions.size(); ++i) {
            if (instructions.get(i).getName().equals("BEQ") || instructions.get(i).getName().equals("BNE")) {
                if (i >= 1 && validateIndependent(instructions.get(i - 1), instructions.get(i))) {
                    outputList.add(instructions.get(i));
                    outputList.add(instructions.get(i - 1));
                } else if (i < instructions.size() - 1 && validateNext((IInstruction) instructions.get(i), instructions.get(i + 1), instructions)) {
                    outputList.add(instructions.get(i));
                    outputList.add(instructions.get(i + 1));
                } else {
                    IInstruction ins = (IInstruction) instructions.get(i);
                    Integer index = adressMap.get(ins.getImmidiate().trim());
                    if (validateTarget((IInstruction) instructions.get(i), instructions.get(index), instructions)) {
                        outputList.add(instructions.get(i));
                        outputList.add(instructions.get(index));
                    }
                }
            }

        }
        for (Instruction i : instructions) {
            System.out.println(i);
        }
        writeInscructions(outputList, filePath);
    }

    private static boolean validateTarget(IInstruction branch, Instruction target, ArrayList<Instruction> instructions) {
        if (target.getName().equals("BEQ") || target.getName().equals("BNE")) {
            return false;
        }
        if (target instanceof JInstruction) return false;
        Integer index = adressMap.get(branch.getImmidiate().trim());
        Integer nextIndex = instructions.indexOf(branch) + 1;
        for (int i = nextIndex; i < index; i++) {
            Instruction instr = instructions.get(i);
            if (instr instanceof RInstruction) {
                if (target.dajOdredisni().equals(((RInstruction) instr).getRs()) || target.dajOdredisni().equals(((RInstruction) instr).getRt())) {
                    return false;
                }
            } else if (instr instanceof IInstruction) {
                if (target.dajOdredisni().equals(((IInstruction) instr).getRs())) {
                    return false;
                }
            } else continue; //j tip ne utice
        }
        return true;
    }

    private static boolean validateNext(IInstruction branch, Instruction next, ArrayList<Instruction> instructions) {
        if (next instanceof IInstruction && (next.getName().equals("BEQ") || !next.getName().equals("BNE"))) {
            return false;
        }
        if (next instanceof JInstruction) return false;
        Integer index = adressMap.get(branch.getImmidiate().trim());
        for (int i = index; i < instructions.size(); i++) {
            Instruction afterTarget = instructions.get(i);
            if (afterTarget instanceof RInstruction) {
                if (next.dajOdredisni().equals(((RInstruction) afterTarget).getRs()) || next.dajOdredisni().equals(((RInstruction) afterTarget).getRt())) {
                    return false;
                }
            } else if (afterTarget instanceof IInstruction) {
                if (next.dajOdredisni().equals(((IInstruction) afterTarget).getRs())) {
                    return false;
                }
            } else continue; //j tip ne utice
        }
        return true;
    }

    private static boolean validateIndependent(Instruction ins1, Instruction ins2) {
        if (ins1 instanceof RInstruction) {
            RInstruction rins = (RInstruction) ins1;
            IInstruction iins = (IInstruction) ins2;
            if (!rins.getRd().equals(iins.getRs()) && !rins.getRd().equals(iins.getRt())) return true;
        }
        if (ins1 instanceof IInstruction && !ins1.getName().equals("BEQ") && !ins1.getName().equals("BNE")) {
            IInstruction i1 = (IInstruction) ins1;
            IInstruction i2 = (IInstruction) ins2;
            if (!i1.getRt().equals(i2.getRs()) && !i1.getRt().equals(i2.getRt())) return true;
        }
        return false;
    }

    public static ArrayList<Instruction> loadInstructions(File file) throws Exception {
        ArrayList<Instruction> instructions = new ArrayList<>();
        BufferedReader reader;
        Instruction ins;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine().replaceAll("\\s+", " "); // regex da izbriše viška whitespace

            while (line != null) {
                String[] parameters = line.split(" ");
                for(String s: parameters) if(s.equals("") || s.equals(" ")) throw new Exception("Neispravan format ulazne datoteke");
                if (!parameters[0].contains(":")) {        //ukoliko prva rijec nema : znaci da nema labele na toj instrukciji
                    if (rType.contains(parameters[0].toLowerCase())) {
                        ins = new RInstruction(parameters[0], parameters[1], parameters[2], parameters[3]);
                        instructions.add(ins);
                    } else if (iTypeNoMemory.contains(parameters[0].toLowerCase())) {
                        if (parameters[0].toLowerCase() != "lui") {
                            ins = new IInstruction(parameters[0], parameters[1], parameters[2], parameters[3]);
                        } else {
                            ins = new IInstruction(parameters[0], parameters[1], "", parameters[2]); //instrukcija lui nema izvorisni registar
                        }
                        instructions.add(ins);
                    } else if (jType.contains(parameters[0].toLowerCase())) {
                        ins = new JInstruction(parameters[0], parameters[1]);
                        instructions.add(ins);
                    } else if (iTypeMemory.contains(parameters[0].toLowerCase())) {
                        ins = new IMemInstruction(parameters[0], parameters[1], parameters[2]);
                        instructions.add(ins);
                    } else {
                        System.out.println(parameters[0]);
                        throw new IllegalArgumentException("Neispravan format ulazne datoteke!");
                    }
                } else {
                    if (rType.contains(parameters[1].toLowerCase())) {
                        String label = parameters[0].replace(":", "");
                        ins = new RInstruction(label, parameters[1], parameters[2], parameters[3], parameters[4]);
                        instructions.add(ins);
                    } else if (iTypeNoMemory.contains(parameters[1].toLowerCase())) {
                        String label = parameters[0].replace(":", "");
                        if (parameters[1].toLowerCase() != "lui") {
                            ins = new IInstruction(label, parameters[1], parameters[2], parameters[3], parameters[4]);
                        } else {
                            ins = new IInstruction(label, parameters[1], parameters[2], "", parameters[3]); //instrukcija lui nema izvorisni registar
                        }
                        instructions.add(ins);
                    } else if (jType.contains(parameters[1].toLowerCase())) {
                        ins = new JInstruction(parameters[0], parameters[1], parameters[2]);
                        instructions.add(ins);
                    } else if (iTypeMemory.contains(parameters[1].toLowerCase())) {
                        String label = parameters[0].replace(":", "");
                        ins = new IMemInstruction(label, parameters[1], parameters[2], parameters[3]);
                        instructions.add(ins);
                    } else {
                        for (int i = 0; i < parameters.length; ++i) System.out.println(parameters[i]);
                        throw new IllegalArgumentException("Neispravan format ulazne datoteke!");
                    }
                }
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return instructions;
    }

    public static void writeInscructions(ArrayList<Instruction> instructionList, String filePath) {
        if (instructionList != null && !instructionList.isEmpty()) {
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileOutputStream("output.txt"));
                pw.println("Instrukcije zadrške su ispisane u sljedećem formatu \n branch instrukcija - njena instrukcija zadrške: \n ");
                for (int i = 0; i < instructionList.size(); i += 2) {
                    pw.print(instructionList.get(i));
                    pw.print("- ");
                    pw.println(instructionList.get(i + 1));
                }
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileOutputStream("output.txt"));
                pw.println("Za datu sekvencu instrukcija nije moguce pronaci instrukciju zadrske ili ona nije potrebna");
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
