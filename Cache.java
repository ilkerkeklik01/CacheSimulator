public class Cache {
    public int s;
    private int b;
    public Cache nextCache = null;
    private byte[] Memory = null;
    private boolean fetchFromMemory = false;
    public String name ="";

    public int misses = 0;
    public int hits = 0;
    public int evictions = 0;
    public int placedInSet = -1;
    public boolean isHit =false;
    public boolean isEviction = false;

    public Set[] Sets;




    public Cache(int s, int E,int b,Cache nextCache, String name){
        int S = (int)Math.pow(2,s);
        Sets = new Set[S];
        this.s = s;
        this.b = b;
        for(int i = 0; i < S; i++){
            Sets[i] = new Set(E,i,b);
        }
        this.nextCache = nextCache;
        fetchFromMemory = false;
        this.name = name;
    }
    public Cache(int s, int E,int b,byte[] memory, String name){
        int S = (int)Math.pow(2,s);
        Sets = new Set[S];
        this.s = s;
        this.b = b;
        for(int i = 0; i < S; i++){
            Sets[i] = new Set(E,i,b);
        }
        this.Memory = memory;
        fetchFromMemory = true;
        this.name = name;
    }

    //Sadece cache'teki data'yı döndürür. datanın hangi bölümü alınacağı oraya kalmış //inputu hexa olarak alır
    public byte[] ScanCacheForLoad(String hexaDecimalAddress){
        placedInSet = -1;
        isHit =false;
        isEviction = false;

        String[] inputs = SeperateAddress(hexaDecimalAddress);
        String tagAsHexaDecimal = inputs[0];
        int setNoAsDecimal = Helper.HexadecimalToDecimal(inputs[1]);
        String byteBlocks = inputs[2];
        Boolean[] isMemoryUsedAndIsAnyEviction = new Boolean[2];
        isMemoryUsedAndIsAnyEviction[0] = false;
        isMemoryUsedAndIsAnyEviction[1] = false;

        byte[] dataWanted = Sets[setNoAsDecimal].ScanSetAndGetLineData(tagAsHexaDecimal, fetchFromMemory, s, isMemoryUsedAndIsAnyEviction);
        if( dataWanted == null){
            misses++;
            isHit = false;
            placedInSet = setNoAsDecimal;

            if (nextCache != null){
                nextCache.ScanCacheForLoad(hexaDecimalAddress);
                updateSet(setNoAsDecimal, tagAsHexaDecimal, isMemoryUsedAndIsAnyEviction);
            }
        }else if(isMemoryUsedAndIsAnyEviction[0] == true){
            misses++;
            isHit = false;
            placedInSet = setNoAsDecimal;
        }else{
            isHit = true;
            placedInSet = setNoAsDecimal;
            hits++;
        }
        if(isMemoryUsedAndIsAnyEviction[1] == true){
            isEviction = true;
            evictions++;
            placedInSet = setNoAsDecimal;
        }


        return dataWanted;
    }

    public void ScanCacheForStore(String hexaDecimalAddress, int size, String hexaDecimalValue){
        placedInSet = -1;
        isHit =false;
        isEviction = false;

       byte[] givenData = Helper.StoreData(hexaDecimalAddress,size,hexaDecimalValue);

            UpdateCachesAfterStore(hexaDecimalAddress,size,givenData);


    }

    public void UpdateCachesAfterStore(String hexaDecimalAddress, int sizeOfData, byte[] updatedData){
        String[] inputs = SeperateAddress(hexaDecimalAddress);
        String tagAsHexaDecimal = inputs[0];
        int setNoAsDecimal = Helper.HexadecimalToDecimal(inputs[1]);
        String byteBlocks = inputs[2];
        int bytesBlocksInDecimal = Helper.HexadecimalToDecimal(byteBlocks);

        Line line = Sets[setNoAsDecimal].findLine(tagAsHexaDecimal);
        if(line == null){
            isHit = false;
            misses++;
        }else{
            isHit = true;
            placedInSet = setNoAsDecimal;
            hits++;
                byte[] data = Helper.FetchData(tagAsHexaDecimal,setNoAsDecimal,line.B,s);
                line.setData(tagAsHexaDecimal,data,false);


        }
        if(nextCache != null){
            nextCache.UpdateCachesAfterStore(hexaDecimalAddress,sizeOfData,updatedData);
        }
    }

    public String[] SeperateAddress(String addressBeginningInHexa){
        String addressInBit = Helper.HexadecimalToBinaryWithPadding(addressBeginningInHexa);
        String bSubstring = addressInBit.substring( addressInBit.length() - b);
        String sSubstring = addressInBit.substring(addressInBit.length() - b - s, addressInBit.length() - b);
        String remainingString = addressInBit.substring(0,addressInBit.length() - b - s);
        String bHex = Helper.BinaryToHexadecimalWithPadding(bSubstring, (int)Math.ceil(b/4.0));
        String sHex = Helper.BinaryToHexadecimalWithPadding(sSubstring, (int)Math.ceil(s/4.0));
        String remainingHex = Helper.BinaryToHexadecimalWithPadding(remainingString, 8-(int)Math.ceil(b/4.0)-(int)Math.ceil(s/4.0));

        String array[] = new String[3];
        array[0] = remainingHex;
        array[1] = sHex;
        array[2] = bHex;
        return array;
    }

    public void updateSet(int setNoAsDecimal,String tagAsHexaDecimal, Boolean[] isMemoryUsedAreThereAnyEviction){
        Sets[setNoAsDecimal].updateALine(tagAsHexaDecimal,s, isMemoryUsedAreThereAnyEviction);

    }

    public String HitOrMissString(){
        if(isHit)
            return "hit";
        return "miss";
    }


}
