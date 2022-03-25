public class Pair<A, B> {

    private final A first;
    private final B second;

    public Pair(A first, B second){
        this.first = first;
        this.second = second;
    }
    public Pair(Pair<A, B> pair){
        this.first = pair.getFirst();
        this.second = pair.getSecond();
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
