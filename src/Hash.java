import java.io.Serializable;

// Hash들 모여있음
class Hash implements Serializable {

    private String tag;

    public Hash(String tag){
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return tag;

    }

    @Override
    public boolean equals(Object other) {
        if(other == null || !(other instanceof Hash)) {
            return false;
        }
        Hash temp = (Hash)other;
        return tag.equals(temp.getTag());
    }
}
