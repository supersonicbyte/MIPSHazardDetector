package ba.unsa.etf.ra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
        for(Instruction i: instructions){
            System.out.println(i);
        }
    }

    public static ArrayList<Instruction> loadInstructions(File file) {
        ArrayList<Instruction> instructions = new ArrayList<>();
        BufferedReader reader;
        Instruction ins;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                String[] parameters = line.split(" ");
                if(rType.contains(parameters[0])){
                    ins = new RInstruction(parameters[0], parameters[1], parameters[2], parameters[3]);
                    instructions.add(ins);
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
}
