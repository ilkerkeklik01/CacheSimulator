package Instructions;

public abstract class Instruction {

    public char instructionType ;

    public String operationAdress;

    public int size;

    public Instruction(char instructionType,String operationAdress,int size){
        setInstructionType(instructionType);
        setoperationAdress(operationAdress);
        setSize(size);
    }

    public void setSize(int size){
        this.size = size;
    }
    public  void setInstructionType(char instructionType){
        this.instructionType = instructionType;
    }

    public void setoperationAdress(String operationAdress){
        this.operationAdress = operationAdress;
    }

    public String getOperationAdress(){
        return operationAdress;
    }
    public char getInstructionType(){
        return instructionType;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return instructionType+ " "+ operationAdress + " " + size;
    }
    public String toStringAsLine(){
        return instructionType+ " "+ operationAdress + ", " + size;
    }

}
