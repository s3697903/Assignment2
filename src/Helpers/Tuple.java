package Helpers;

public class Tuple<T1, T2> {

    private T1 t1;
    private T2 t2;

    public Tuple(T1 first, T2 second){
        this.t1 = first;
        this.t2 = second;
    }

    public T1 first() {
        return this. t1;
    }

    public T2 second() {
        return this.t2;
    }
}
