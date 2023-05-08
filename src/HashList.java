import java.io.Serializable;
import java.util.ArrayList;

class HashList implements Serializable {
    private ArrayList<Hash> hashList;

    public HashList() {
        hashList = new ArrayList<Hash>();
    }

    public ArrayList<Hash> getHashList() {
        return hashList;
    }

    public void setHashList(ArrayList<Hash> hashList) {
        this.hashList = hashList;
    }
}