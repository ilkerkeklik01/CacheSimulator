package Instructions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class InstructionList {

    private  ArrayList<Instruction> instructions;

    public InstructionList(String fileName){
        instructions = new ArrayList<>();
        if(!getInstructions(fileName)){
            System.out.println("Error while processing "+fileName);
        }

    }

    private boolean getInstructions(String fileName)  {
        try(Scanner scanner = new Scanner(new File(fileName))){
            while (scanner.hasNext()){
                String currentAdress;
                char currentInstructionType =scanner.next().charAt(0);
                int currentSize;
                String currentData;

                if(scanner.hasNext()){
                    String strArr[] = scanner.nextLine().split(",");

                    if(currentInstructionType == 'I'){
                        currentAdress = strArr[0].trim();
                        currentSize = Integer.parseInt(strArr[1].trim());
                        instructions.add(new I_Instruction(currentAdress,currentSize));
                    }else if(currentInstructionType == 'L'){
                        currentAdress = strArr[0].trim();
                        currentSize = Integer.parseInt(strArr[1].trim());
                        instructions.add(new L_Instruction(currentAdress,currentSize));
                    }else if(currentInstructionType == 'M'){
                        currentAdress = strArr[0].trim();
                        currentSize = Integer.parseInt(strArr[1].trim());
                        currentData= strArr[2].trim();
                        instructions.add(new M_Instruction(currentAdress,currentSize,currentData));
                    }else if(currentInstructionType == 'S'){
                        currentAdress = strArr[0].trim();
                        currentSize = Integer.parseInt(strArr[1].trim());
                        currentData= strArr[2].trim();
                        instructions.add(new S_Instruction(currentAdress,currentSize,currentData));
                    }



                }//security


            }
            return true;
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }





    }

    public ArrayList<Instruction> getInstructions() {
        return new ArrayList<Instruction>(instructions);
    }


}
