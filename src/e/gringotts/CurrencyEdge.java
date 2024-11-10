
package e.gringotts;

public class CurrencyEdge<T extends Comparable<T>, N extends Comparable<N>> {
    CurrencyVertex<T, N> toVertex;
    N weight;
    CurrencyEdge<T, N> nextEdge;

    public CurrencyEdge() {
        this.toVertex = null;
        this.weight = null;
        this.nextEdge = null;
    }

    public CurrencyEdge(CurrencyVertex<T, N> toVertex, N weight, CurrencyEdge<T, N> nextEdge) {
        this.toVertex = toVertex;
        this.weight = weight;
        this.nextEdge = nextEdge;
    }
}
