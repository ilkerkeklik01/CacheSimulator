package Instructions;

public class S_Instruction extends Instruction{

    private String data;
    public S_Instruction(String operationAdress, int size,String data) {
        super('S',operationAdress, size);
        setData(data);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return super.toString()+ " "+data;
    }
    public String toStringAsLine(){
        return this.instructionType+ " "+ operationAdress + ", " + size +", " + data;

    }
}
