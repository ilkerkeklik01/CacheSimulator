public class Set {

    public int SetNo;
    public int E;
    public Line[] Lines;



    public Set(int E, int setNo, int b){
        Lines = new Line[E];
        SetNo = setNo;
        for(int i =0; i <E; i++ ){
            Lines[i] = new Line(b);
        }
        this.E = E;
    }

    public byte[] ScanSetAndGetLineData(String tag, boolean fetchFromMemory, int s, Boolean[] isMemoryUsedAndIsThereAnyEviction) {
        byte[] data = null;
        for(int i =0; i < Lines.length; i++){
            if(Lines[i].getTag().equals(tag)){ // -------------- If it is hit -------------
                data = Lines[i].getData();
                if((data == null || !Lines[i].getV()) && fetchFromMemory ){
                    isMemoryUsedAndIsThereAnyEviction[0] = true;
                   data  = Helper.FetchData(tag,SetNo,Lines[i].B,s);
                   Lines[i].setData(tag,data,true);
                   return data;
                }
                return data;

            }
        }
        //---------------------------- If it is miss ----------------------------
        int lowestTime = Integer.MAX_VALUE;
        Line lineToBeOut = null;
        for(int i =0; i < Lines.length; i++){
            if(Lines[i].getV() == false && fetchFromMemory){
                isMemoryUsedAndIsThereAnyEviction[0] = true;
                data  = Helper.FetchData(tag,SetNo,Lines[i].B,s);
                Lines[i].setData(tag,data,true);
                return data;
            }
            if(Lines[i].getTime() < lowestTime){
                lineToBeOut = Lines[i];
                lowestTime = Lines[i].getTime();

            }
        }
        if(fetchFromMemory){
            isMemoryUsedAndIsThereAnyEviction[0] = true;
            data  = Helper.FetchData(tag,SetNo,lineToBeOut.B,s);
            if(lineToBeOut.Data != null)
                isMemoryUsedAndIsThereAnyEviction[1] = true;
            lineToBeOut.setData(tag,data,true);
            return data;
        }
        return null;




    }

    public void updateALine(String tagAsHexaDecimal, int s, Boolean[] isMemoryUsedAreThereAnyEviction) {

        int lowestTime = Integer.MAX_VALUE;
        Line lineToBeOut = null;
        for(int i =0; i < Lines.length; i++){
            if(Lines[i].getV() == false){
                byte[] data = Helper.FetchData(tagAsHexaDecimal,SetNo,Lines[i].B,s);
                Lines[i].setData(tagAsHexaDecimal,data,true);
                return;
            }
            if(Lines[i].getTime() < lowestTime){
                lineToBeOut = Lines[i];
                lowestTime = Lines[i].getTime();
            }
        }
        isMemoryUsedAreThereAnyEviction[1] = true;
        byte[] data = Helper.FetchData(tagAsHexaDecimal,SetNo,lineToBeOut.B,s);
        lineToBeOut.setData(tagAsHexaDecimal,data,true);

    }

    public Line findLine(String tagAsHexaDecimal) {
        for(int i=0; i<Lines.length; i++){
            if(Lines[i].getTag().equals(tagAsHexaDecimal)){
                return Lines[i];
            }
        }
        return  null;
    }


}
