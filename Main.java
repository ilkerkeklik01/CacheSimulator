import Instructions.Instruction;
import Instructions.InstructionList;
import Instructions.M_Instruction;
import Instructions.S_Instruction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {


    public static int Time = 1;
    public static int hits = 0;
    public static int misses = 0;
    public static int L1s = -1;
    public static int L1E = -1;
    public static int L1b = -1;
    public static int L2s = -1;
    public static int L2E = -1;
    public static int L2b = -1;
    public static int L2B = -1;
    public static int L1B = -1;
    static String traceFile = null;
    public static int addressabilityBit = 32;
    public static StringBuilder sbGeneral = new StringBuilder();

    public static byte[] Ram;
    public static Cache L2,L1D,L1I;
    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-L")) {
                String option = args[i];
                if (option.equals("-L1s") && i < args.length - 1) {
                    L1s = parsePositiveIntOption(args[i + 1]);
                    i++;
                } else if (option.equals("-L1E") && i < args.length - 1) {
                    L1E = parsePositiveIntOption(args[i + 1]);
                    i++;
                } else if (option.equals("-L1b") && i < args.length - 1) {
                    L1b = parsePositiveIntOption(args[i + 1]);
                    L1B = (int)Math.pow(L1B,2);
                    i++;
                } else if (option.equals("-L2s") && i < args.length - 1) {
                    L2s = parsePositiveIntOption(args[i + 1]);
                    i++;
                } else if (option.equals("-L2E") && i < args.length - 1) {
                    L2E = parsePositiveIntOption(args[i + 1]);
                    i++;
                } else if (option.equals("-L2b") && i < args.length - 1) {
                    L2b = parsePositiveIntOption(args[i + 1]);
                    L2B = (int)Math.pow(L2B,2);
                    i++;
                } else {
                    System.out.println("Incorrect option " + option);
                    System.exit(1);
                }
            } else if (args[i].equals("-t") && i < args.length - 1) {
                traceFile = args[i + 1];
                i++;
            }
        }
        if (traceFile == null) {
            System.out.println("Incorrect option: options -t and name of the trace file must be specified.");
            System.exit(1);
        }


        InstructionList instructionList = new InstructionList("./" + traceFile);
        ArrayList<Instruction> list = instructionList.getInstructions();

        for(Instruction each : list){
            System.out.println(each);
        }
        Ram = Helper.readBytesFromFile();

        // ------------------------------ Input reading end ----------------------------------------

        //Cache cache = new Cache(L2s,L2E,L2B);
        //Cache cache1 = new Cache(s,E,B);
        L2 = new Cache(L2s,L2E,L2b,Ram,"L2");
        L1I = new Cache(L1s,L1E,L1b,L2, "L1I");
        L1D = new Cache(L1s,L1E,L1b,L2,"L1D") ;

        startTrace(list);

        StringBuilder sb = new StringBuilder();
        sb.append(WriteTraceOutput(L1I));
        sb.append("\n");
        sb.append(WriteTraceOutput(L1D));
        sb.append("\n");
        sb.append(WriteTraceOutput(L2));
        sb.append("\n");
        sb.append(WriteGeneralOutput(L1I));
        sb.append(WriteGeneralOutput(L1D));
        sb.append(WriteGeneralOutput(L2));
        sb.append(WriteTotalOutput(L1I,L1D,L2));
        sb.append("\n");

        writeStringToFile(sbGeneral.toString(), "./output.txt");;

        Helper.createBinaryFile(Ram,"./modifiedRam.dat");

        writeStringToFile(sb.toString(), "./caches.txt");






       /* String address = "002dba30"; //istenen address şimdi bunu int'e çevirip. byteblock küsüratını çıkarıcaz mesela address 95 byteblock 16 ise 80. addresten itibaren 96ya kadar alıcaz
        int addressInDecimal = Helper.HexadecimalToDecimal(address);
        int addressSetBegining = addressInDecimal - addressInDecimal%B;   //addressInDecimal%B
        String addressBeginningInHexa = Helper.DecimalToHexadecimalWithPadding(addressSetBegining);
        String[] inputs = SeperateAddress(addressBeginningInHexa);
        cache.ScanCache(inputs[0],inputs[1], inputs[2],null);*/
        System.out.println("xd");


    }


    private static int parsePositiveIntOption(String option) {
        try {
            int value = Integer.parseInt(option);
            if (value < 0) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Hatalı seçenek değeri: " + option);
            System.exit(1);
            return -1; // Kod asla buraya ulaşmayacak, sadece derleyici uyarısı kaldırmak için ekledim
        }
    }

    private static int startTrace(ArrayList<Instruction> instructions){

        for (Instruction instruction : instructions) {
            if (instruction.getInstructionType() == 'S') {
                sbGeneral.append(((S_Instruction) instruction).toStringAsLine() + "\n");
                L1D.ScanCacheForStore(instruction.getOperationAdress(),instruction.getSize(),((S_Instruction) instruction).getData());
                sbGeneral.append(SToString(L1D));
                Time++;
            } else if (instruction.getInstructionType() == 'M' ) {
                sbGeneral.append(((M_Instruction) instruction).toStringAsLine() + "\n");
                L1D.ScanCacheForLoad(instruction.getOperationAdress());
                sbGeneral.append(LToString(L1D));
                L1D.ScanCacheForStore(instruction.getOperationAdress(),instruction.getSize(),((M_Instruction) instruction).getData());
                sbGeneral.append(SToString(L1D));

                Time++;
            } else if (instruction.getInstructionType() == 'L') {
                sbGeneral.append(( instruction).toStringAsLine() + "\n");
                L1D.ScanCacheForLoad(instruction.getOperationAdress());
                sbGeneral.append(LToString(L1D));
                Time++;
            } else if (instruction.getInstructionType() == 'I') {
                sbGeneral.append(( instruction).toStringAsLine() + "\n");
                L1I.ScanCacheForLoad(instruction.getOperationAdress());
                sbGeneral.append(IToString(L1I));
                Time++;
            } else {
                System.out.println("Invalid instruction");
            }
        }
        return 0;

    }


    private static String WriteTraceOutput(Cache cache){
        StringBuilder sb = new StringBuilder();
        sb.append("   ~~~~~~~~~~~~"+cache.name + "~~~~~~~~~~~\n");
        for(int i =0; i< cache.Sets.length; i++){
            sb.append("--------------Set" + cache.Sets[i].SetNo + "------------- \n");
            sb.append("Tag     Time    v        Data\n");
            for(int j =0; j<cache.Sets[i].E; j++){

                if(cache.Sets[i].Lines[j].getTag().equals("")){
                    sb.append("      ");
                }
                sb.append(cache.Sets[i].Lines[j].getTag() +"  " + cache.Sets[i].Lines[j].getTime() + "     "+ cache.Sets[i].Lines[j].getV() + "\t    " + cache.Sets[i].Lines[j].getDataAsHexString()+ "\n");
            }
        }
        return sb.toString();
    }

    private static String WriteGeneralOutput(Cache cache){
        StringBuilder sb = new StringBuilder();
        sb.append(cache.name +"-hits:"+ cache.hits + " " + cache.name +"-misses:" + cache.misses  +" "+ cache.name +"-evictions:" + cache.evictions + "\n");
        return sb.toString();
    }
    private static String WriteTotalOutput(Cache cache, Cache cache1, Cache cache2){
        StringBuilder sb = new StringBuilder();
        sb.append("-Total hits:" + (cache.hits + cache1.hits + cache2.hits)+ " \n-Total misses:" + (cache.misses + cache1.misses + cache2.misses) + " \n-Total evictions:" + (cache.evictions + cache1.evictions + cache2.evictions));
        return sb.toString();
    }

    private static void writeStringToFile(String content, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("String başarıyla dosyaya yazıldı.");
        } catch (IOException e) {
            System.err.println("Dosyaya yazarken bir hata oluştu: " + e.getMessage());
        }
    }

    private static String LToString(Cache cache){

        String xd = "  " + cache.name + " " + cache.HitOrMissString() ;
        String zort = "  Place in " + cache.name + " set " + cache.placedInSet;
        if(!cache.isHit){
            zort +=  ", " + cache.nextCache.name + " set " + cache.nextCache.placedInSet;
            xd +=  ", " + cache.nextCache.name + " " + cache.nextCache.HitOrMissString();
        }
        return xd +"\n" + zort + "\n";
    }
    private static String SToString(Cache cache){
        String xd = "  " + cache.name + " " + cache.HitOrMissString() + ", " + cache.nextCache.name + " " + cache.nextCache.HitOrMissString() + "\n";
        String zort = "  Stored in ";
        if(cache.isHit)
            zort += cache.name + ", ";
        if(cache.nextCache.isHit)
            zort += cache.nextCache.name +", ";
        zort +=  "RAM\n";
        return xd + zort;
    }
    private static String IToString(Cache cache){
        String xd = "  " + cache.name + " " + cache.HitOrMissString() ;
        String zort = "  Place in " + cache.name + " set " + cache.placedInSet;
        if(!cache.isHit){
            zort +=  ", " + cache.nextCache.name + " set " + cache.nextCache.placedInSet;
            xd +=  ", " + cache.nextCache.name + " " + cache.nextCache.HitOrMissString();
        }
        return xd +"\n" + zort + "\n";
    }







}
