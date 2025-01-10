package class13;

import java.util.Objects;

public class Edge {
	public int weight;
	public Node from;
	public Node to;

	public Edge(int weight, Node from, Node to) {
		this.weight = weight;
		this.from = from;
		this.to = to;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Edge edge = (Edge) o;
		return weight == edge.weight && Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
	}

	@Override
	public int hashCode() {
		return Objects.hash(weight, from, to);
	}
}
