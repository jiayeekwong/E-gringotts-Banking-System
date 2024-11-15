
package e.gringotts;

import java.util.ArrayList;


class WeightedCurrencyGraph<T extends Comparable<T>, N extends Comparable<N>> {
    CurrencyVertex<T, N> head;
    int size;

    public WeightedCurrencyGraph() {
        this.head = null;
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean hasVertex(T vertexInfo) {
        if (head == null) {
            return false;
        }
        CurrencyVertex<T, N> current = head;
        while (current != null) {
            if (current.vertexInfo.compareTo(vertexInfo) == 0) {
                return true;
            }
            current = current.nextVertex;
        }
        return false;
    }

    public int getIndeg(T vertexInfo) {
        if (hasVertex(vertexInfo)) {
            CurrencyVertex<T, N> current = head;
            while (current != null) {
                if (current.vertexInfo.compareTo(vertexInfo) == 0) {
                    return current.indeg;
                }
                current = current.nextVertex;
            }
        }
        return -1;
    }

    public int getOutdeg(T vertexInfo) {
        if (hasVertex(vertexInfo)) {
            CurrencyVertex<T, N> current = head;
            while (current != null) {
                if (current.vertexInfo.compareTo(vertexInfo) == 0) {
                    return current.outdeg;
                }
                current = current.nextVertex;
            }
        }
        return -1;
    }

    public boolean addVertex(T vertexInfo) {
        if (hasVertex(vertexInfo)) {
            return false;
        }
        CurrencyVertex<T, N> current = head;
        CurrencyVertex<T, N> newVertex = new CurrencyVertex<>(vertexInfo, null);
        if (current == null) {
            head = newVertex;
        }
        else {
            while (current.nextVertex != null) {
                current = current.nextVertex;
            }
            current.nextVertex = newVertex;
        }
        size++;
        return true;
    }

    public int getIndex(T vertexInfo) {
        CurrencyVertex<T, N> current = head;
        for (int i = 0; i < size; i++) {
            if (current.vertexInfo.compareTo(vertexInfo) == 0) {
                return i;
            }
            current = current.nextVertex;
        }
        return -1;
    }

    public ArrayList<T> getAllVertexObjects() {
        ArrayList<T> list = new ArrayList<>();
        CurrencyVertex<T, N> current = head;
        while (current != null) {
            list.add(current.vertexInfo);
            current = current.nextVertex;
        }
        return list;
    }

    public T getVertex(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        CurrencyVertex<T, N> current = head;
        for (int i = 0; i < index; i++) {
            current = current.nextVertex;
        }
        return current.vertexInfo;
    }

    public boolean hasEdge(T source, T destination) {
        if (head == null) {
            return false;
        }
        if (!hasVertex(source) || !hasVertex(destination)) {
            return false;
        }

        CurrencyVertex<T, N> sourceVertex = head;
        while (sourceVertex != null) {
            if (sourceVertex.vertexInfo.compareTo(source) == 0) {

                CurrencyEdge<T, N> currentEdge = sourceVertex.firstEdge;
                while (currentEdge != null) {
                    if (currentEdge.toVertex.vertexInfo.compareTo(destination) == 0) {
                        return true;
                    }
                    currentEdge = currentEdge.nextEdge;
                }
            }
            sourceVertex = sourceVertex.nextVertex;
        }
        return false;
    }

    public boolean addEdge(T source, T destination, N weight) {
        if (head == null) {
            return false;
        }
        if (!hasVertex(source) || !hasVertex(destination)) {
            return false;
        }

        CurrencyVertex<T, N> sourceVertex = head;
        while (sourceVertex != null) {
            if (sourceVertex.vertexInfo.compareTo(source) == 0) {

                CurrencyVertex<T, N> destinationVertex = head;
                while (destinationVertex != null) {
                    if (destinationVertex.vertexInfo.compareTo(destination) == 0) {
                        CurrencyEdge<T, N> currentEdge = sourceVertex.firstEdge;
                        CurrencyEdge<T, N> newEdge = new CurrencyEdge<>(destinationVertex, weight, currentEdge);
                        sourceVertex.firstEdge = newEdge;

                        sourceVertex.outdeg++;
                        destinationVertex.indeg++;
                        return true;
                    }
                    destinationVertex = destinationVertex.nextVertex;
                }
            }
            sourceVertex = sourceVertex.nextVertex;
        }
        return false;
    }

    public N getEdgeWeight(T source, T destination) {
        if (head == null) {
            return null;
        }
        if (!hasVertex(source) || !hasVertex(destination)) {
            return null;
        }

        CurrencyVertex<T, N> sourceVertex = head;
        while (sourceVertex != null) {
            if (sourceVertex.vertexInfo.compareTo(source) == 0) {
                CurrencyEdge<T, N> currentEdge = sourceVertex.firstEdge;
                while (currentEdge != null) {
                    if (currentEdge.toVertex.vertexInfo.compareTo(destination) == 0) {
                        return currentEdge.weight;
                    }
                    currentEdge = currentEdge.nextEdge;
                }
            }
            sourceVertex = sourceVertex.nextVertex;
        }
        return null;
    }

    public ArrayList<T> getNeighbours(T vertex) {
        if (!hasVertex(vertex)) {
            return null;
        }
        ArrayList<T> list = new ArrayList<>();
        CurrencyVertex<T, N> current = head;
        while (current != null) {
            if (current.vertexInfo.compareTo(vertex) == 0) {
                CurrencyEdge<T, N> currentEdge = current.firstEdge;
                while (currentEdge != null) {
                    list.add(currentEdge.toVertex.vertexInfo);
                    currentEdge = currentEdge.nextEdge;
                }
            }
            current = current.nextVertex;
        }
        return list;
    }

    public void printEdges() {
        CurrencyVertex<T, N> current = head;
        while (current != null) {
            System.out.printf("# %s : ", current.vertexInfo);
            CurrencyEdge<T, N> currentEdge = current.firstEdge;
            while (currentEdge != null) {
                System.out.printf("[%s, %s] ", current.vertexInfo, currentEdge.toVertex.vertexInfo);
                currentEdge = currentEdge.nextEdge;
            }
            System.out.println();
            current = current.nextVertex;
        }
    }

    // Q1
    public boolean addUndirectedEdge(T source, T destination, N weight) {
        return (addEdge(source, destination, weight) && addEdge(destination, source, weight));
    }

    // Q2
    public CurrencyEdge<T, N> removeEdge(T source, T destination) {
        if (head == null) {
            return null;
        }
        if (!hasVertex(source) || !hasVertex(destination)) {
            return null;
        }

        CurrencyVertex<T, N> currentSource = head;
        while (currentSource != null) {
            if (currentSource.vertexInfo.compareTo(source) == 0) {
                CurrencyEdge<T, N> previousEdge = currentSource.firstEdge;
                CurrencyEdge<T, N> currentEdge = currentSource.firstEdge;
                while (currentEdge != null) {
                    if (currentEdge.toVertex.vertexInfo.compareTo(destination) == 0) {
                        previousEdge.nextEdge = currentEdge.nextEdge;
                        currentEdge.nextEdge = null;
                        currentSource.outdeg--;
                        currentEdge.toVertex.indeg--;
                        currentEdge.toVertex = null;
                        size--;
                        return currentEdge;
                    }
                    previousEdge = currentEdge;
                    currentEdge = currentEdge.nextEdge;
                }
            }
            currentSource = currentSource.nextVertex;
        }
        return null;
    }

}