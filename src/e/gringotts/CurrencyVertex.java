
package e.gringotts;

public class CurrencyVertex<T extends Comparable<T>, N extends Comparable<N>> {
    T vertexInfo;
    int indeg;
    int outdeg;
    CurrencyVertex<T, N> nextVertex;
    CurrencyEdge<T, N> firstEdge;

    public CurrencyVertex() {
        this(null, null);
    }

    public CurrencyVertex(T vertexInfo, CurrencyVertex<T, N> nextVertex) {
        this.vertexInfo = vertexInfo;
        this.indeg = 0;
        this.outdeg = 0;
        this.nextVertex = nextVertex;
        this.firstEdge = null;
    }
}
