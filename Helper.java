import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Helper {
    public static int BinaryToDecimal(String binary){
        return Integer.parseInt(binary,2);
    }
    public static String HexadecimalToBinary(String hexadecimal){
        int decimal = Integer.parseInt(hexadecimal,16);
        return Integer.toBinaryString(decimal);
    }
    public static String BinaryToHexadecimal(String binary){
        if(binary.equals("") ) return "";
        int decimal = Integer.parseInt(binary,2);
        return Integer.toHexString(decimal);
    }
    public static String DecimalToHexadecimal(int decimal){
        return Integer.toHexString(decimal);
    }
    public static int HexadecimalToDecimal(String hexadecimal){
        if(hexadecimal.equals("")) return 0;
        return Integer.parseInt(hexadecimal,16);
    }

    public static String OctalToHexadecimal(String octal){
        int decimal = Integer.parseInt(octal,8);
        return Integer.toHexString(decimal);
    }
    public static String HexadecimalToOctal(String hexaDecimal){
        int decimal = Integer.parseInt(hexaDecimal,16);
        return Integer.toOctalString(decimal);
    }
    public static String BinaryToHexadecimalWithPadding(String binary){
        return AddPadding(BinaryToHexadecimal(binary),8);
    }
    public static String DecimalToHexadecimalWithPadding(int decimal){
        return AddPadding(DecimalToHexadecimal(decimal),8);
    }
    public static String HexadecimalToBinaryWithPadding(String Hexadecimal){
        return AddPadding(HexadecimalToBinary(Hexadecimal),32);
    }
    public static String BinaryToHexadecimalWithPadding(String binary, int desiredLength){

        return AddPadding(BinaryToHexadecimal(binary),desiredLength);
    }
    public static String DecimalToHexadecimalWithPadding(int decimal, int desiredLength){
        return AddPadding(DecimalToHexadecimal(decimal),desiredLength);
    }
    public static String HexadecimalToBinaryWithPadding(String Hexadecimal, int desiredLength){
        return AddPadding(HexadecimalToBinary(Hexadecimal),desiredLength);
    }


    private static String AddPadding(String hexadecimalNumber, int desiredLength) {
        StringBuilder paddedNumber = new StringBuilder(hexadecimalNumber);
        while (paddedNumber.length() < desiredLength) {
            paddedNumber.insert(0, "0");
        }
        return paddedNumber.toString().toUpperCase();
    }


    public static byte[] FetchData(String tag, int setNo, int B, int s){
        byte[] Data = new byte[B];
        String addressWithoutBlockOffset = tag;
        int addressInDecimal = Helper.HexadecimalToDecimal(addressWithoutBlockOffset);

            addressInDecimal *= (int)Math.pow(2,s);
            addressInDecimal += setNo;


        addressInDecimal = addressInDecimal * B ;
        for(int i = 0; i < B; i++){

            // /8+i-
            Data[i] = Main.Ram[addressInDecimal+ i];
        }
        return Data;

    }
    public static byte[] StoreData(String hexaDecimalAddress, int sizeInBytes, String dataInHexaDecimal){


        int addressInDecimal = Helper.HexadecimalToDecimal(hexaDecimalAddress) ;

        byte[] dataInBytes = hexStringToByteArray(dataInHexaDecimal);



        for(int i = 0; i < sizeInBytes; i++){
            Main.Ram[addressInDecimal] = dataInBytes[i];
        }
        return dataInBytes;
    }

    private static byte[] hexStringToByteArray(String hexValue) {
        int length = hexValue.length();
        byte[] byteArray = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            String hex = hexValue.substring(i, i + 2);
            byteArray[i / 2] = (byte) Integer.parseInt(hex, 16);
        }

        return byteArray;
    }
    public static byte[] readBytesFromFile() {
        File file = new File("./RAM.dat");
        FileInputStream fileInputStream = null;
        byte[] byteArray = null;

        try {
            // Dosyayı aç
            fileInputStream = new FileInputStream(file);

            // Dosyanın boyutunu al
            int fileSize = (int) file.length();

            // Byte dizisi oluştur
            byteArray = new byte[fileSize];

            // Dosyadaki bütün byteları oku
            fileInputStream.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return byteArray;
    }
    public static void createBinaryFile(byte[] data, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
            System.out.println("Binary file created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the binary file: " + e.getMessage());
        }
    }

}


