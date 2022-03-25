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

    @Override
    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }

    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(!(o instanceof Pair)){
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>) o;
        return this.getFirst().equals(other.getFirst()) && this.getSecond().equals(other.getSecond());
    }
}
