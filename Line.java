public class Line {
    private String Tag ="";
    public int B;
    public int Time;
    private boolean v = false;
     byte[] Data = null;


    public Line(int b){
        Data = new byte[(int)Math.pow(2,b)];
        this.B = (int)Math.pow(2,b);
    }
    public byte[] getData(){
        return this.Data;
    }

    //Tag ve setNo'yu hesaplamayı dışarıda yapıcaz.
    public byte[] fetchData(String tag, int setNo){
        String addressWithoutBlockOffset = tag;
        if(setNo != 0){
            String setNoInHexa = Helper.DecimalToHexadecimal(setNo);

             addressWithoutBlockOffset = tag + setNoInHexa;
        }

        int addressInDecimal = Helper.HexadecimalToDecimal(addressWithoutBlockOffset) * B ;
        String address = Helper.DecimalToHexadecimal(addressInDecimal);
        String addressInOctal = Helper.HexadecimalToOctal(address);


        for(int i = 0; i < B; i++){
            System.out.println(Main.Ram[Main.Ram.length-1]);
            Data[i] = Main.Ram[addressInDecimal/8+i];
        }
        v = true;
        Time = Main.Time;
        Main.Time++;
        Tag = tag;
        return Data;

    }
    public String getTag(){
        return Tag;
    }
    public boolean getV(){
        return this.v;
    }
    public int getTime(){
        return this.Time;
    }
    public void setData(String tag,byte[] data,boolean changeTime){
        this.Data = data;
        this.Tag = tag;
        this.v = true;
        if(changeTime)
            this.Time = Main.Time;

    }
    public void setData(String tag,byte[] data,int B){
        this.Data = data;
        this.Tag = tag;
        this.v = true;
        this.Time = Main.Time;
        for(int i =0; i< B; i++){
            this.Data[i] = data[i];
        }
    }

    public String getDataAsHexString(){
        StringBuilder sb = new StringBuilder();
        for (byte b : Data) {
            String hex = String.format("%02X", b); // Byte'ı hexadecimal string olarak formatlar
            sb.append(hex);
        }
        return sb.toString();
    }
}
