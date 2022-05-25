package control;

public class TwoTuple<A, B> {
    public final A first;
    public B second;
    public TwoTuple(A a, B b) { //twotuple constructor
        first = a;
        second = b;
    }
    public String toString() { //overrides toString to print twotuples
        return first + "=" + second;
    }
    public String getFirst() { //returns first using "" to convert to String
        return first + "";
    }
    public B getSecond() { //returns second
        return second;
    }
    public void setSecond(B second) {
        this.second = second;
    }
}