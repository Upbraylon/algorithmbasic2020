package class12;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class Code01_UnionFind {

	public static class Node<V> {
		V value;

		public Node(V v) {
			value = v;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			Node<?> node = (Node<?>) o;
			return Objects.equals(value, node.value);
		}

		@Override
		public int hashCode() {
			return Objects.hash(value);
		}
	}

	public static class UnionFind<V> {
		// 样本对应自己包装后的节点
		public HashMap<V, Node<V>> nodes;
		// 不想在节点上面增加指针字段，用一张表来表示这种关系：父亲是谁
		public HashMap<Node<V>, Node<V>> parents;
		// 谁挂谁：集合大小决定，sizeMap中记录的集合的代表节点和集合大小的对应关系
		public HashMap<Node<V>, Integer> sizeMap;

		// 初始化
		public UnionFind(List<V> values) {
			nodes = new HashMap<>();
			parents = new HashMap<>();
			// 只留代表节点记录，不留非代表节点，可以方便查出当前并查集有几个集合
			sizeMap = new HashMap<>();
			for (V cur : values) {
				Node<V> node = new Node<>(cur);
				nodes.put(cur, node);
				// 初始自己是自己的父节点
				parents.put(node, node);
				// 初始自己是自己集合的代表节点
				sizeMap.put(node, 1);
			}
		}

		// 给你一个节点，请你往上到不能再往上，把代表返回
		public Node<V> findFather(Node<V> cur) {
			Stack<Node<V>> path = new Stack<>();
			// 一直往上到不能再往上，代表节点
			while (cur != parents.get(cur)) {
				path.push(cur);
				cur = parents.get(cur);
			}
			// 链扁平化
			while (!path.isEmpty()) {
				parents.put(path.pop(), cur);
			}
			return cur;
		}

		// 判断代表节点
		public boolean isSameSet(V a, V b) {
			return findFather(nodes.get(a)) == findFather(nodes.get(b));
		}

		public void union(V a, V b) {
			Node<V> aHead = findFather(nodes.get(a));
			Node<V> bHead = findFather(nodes.get(b));
			// aHead == bHead 已经是一个集合了，不用做任何事
			if (aHead == bHead){
				return;
			}
			int aSetSize = sizeMap.get(aHead);
			int bSetSize = sizeMap.get(bHead);
			// 大小集合头部重定向
			Node<V> big = aSetSize >= bSetSize ? aHead : bHead;
			Node<V> small = big == aHead ? bHead : aHead;
			parents.put(small, big);
			sizeMap.put(big, aSetSize + bSetSize);
			sizeMap.remove(small);
		}

		public int sets() {
			return sizeMap.size();
		}

	}
}
