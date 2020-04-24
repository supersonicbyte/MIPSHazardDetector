package ba.unsa.etf.ra;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
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
        }
    };

    public static void main(String[] args) {
        System.out.println("Unesite apslolutnu putanju do .txt datoteke: ");
        Scanner input = new Scanner(System.in);
        String filePath = input.nextLine();
        File file = new File(filePath);
        while (!file.exists()) {
            System.out.println("Datoteka ne postoji! Molimo vas unesite ponovo putanju: ");
            filePath = input.nextLine();
            file = new File(filePath);
        }
        ArrayList<Instruction> instructions = loadInstructions(file);
        for (Instruction i : instructions) {
            System.out.println(i);
        }
        writeInscructions(instructions, filePath);
    }

    public static ArrayList<Instruction> loadInstructions(File file) throws IllegalArgumentException {
        ArrayList<Instruction> instructions = new ArrayList<>();
        BufferedReader reader;
        Instruction ins;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                String[] parameters = line.split(" ");
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
            return null;
        }
        return instructions;
    }

    public static void writeInscructions(ArrayList<Instruction> instructionList, String filePath) {
        if (instructionList != null && !instructionList.isEmpty()) {
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileOutputStream("output.txt"));
                for (Instruction i : instructionList)
                    pw.println(i);
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
