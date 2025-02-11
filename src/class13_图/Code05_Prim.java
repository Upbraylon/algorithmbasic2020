package class13_图;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 一个有 n 个结点的连通图的生成树是原图的极小连通子图，且包含原图中的所有 n 个结点，
 * 并且有保持图连通的最少的边。最小生成树可以用kruskal（克鲁斯卡尔）算法或prim（普里姆）算法求出。
 *
 * 最小生成树算法之Prim算法原理：
 * 1) 可以从任意节点出发来寻找最小生成树
 * 2）某个节点加入到被选的点中后，解锁这个点出发的所有新的边
 * 3）在所有解锁的边中选最小的边，然后看看这个边会不会形成环
 * 4）如果会，不要当前边，继续考察剩下解锁的边中最小的边，重复3）
 * 5）如果不会，要当前边，将该边的指向点加入到被选中的点中，重复2）
 * 6）当所有点都被选取，最小生成树就得到了
 *
 *
 * 当边的条数较多时，P优于K算法
 */
public class Code05_Prim {

	public static class EdgeComparator implements Comparator<Edge> {

		@Override
		public int compare(Edge o1, Edge o2) {
			return o1.weight - o2.weight;
		}

	}

	public static Set<Edge> primMST(Graph graph) {
		// 解锁的边进入小根堆
		PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(new EdgeComparator());

		// 哪些点被解锁出来了
		HashSet<Node> nodeSet = new HashSet<>();

		// 依次挑选的的边在result里
		Set<Edge> result = new HashSet<>();
		// for循环是为了防止森林
		// 随便挑了一个点
		for (Node node : graph.nodes.values()) {
			// node 是开始点
			if(nodeSet.contains(node)) {
				continue;
			}

			nodeSet.add(node);
			// 由一个点，解锁所有相连的边
			priorityQueue.addAll(node.edges);
			while (!priorityQueue.isEmpty()) {
				// 弹出解锁的边中，最小的边
				Edge edge = priorityQueue.poll();
				// 可能的一个新的点
				Node toNode = edge.to;
				// 不含有的时候，就是新的点
				if (nodeSet.contains(toNode)) {
					continue;
				}

				nodeSet.add(toNode);
				result.add(edge);
				priorityQueue.addAll(toNode.edges);
			}
			// break; // 防森林就不要break，如果确定是一棵树，就break
		}
		return result;
	}

	// 请保证graph是连通图
	// graph[i][j]表示点i到点j的距离，如果是系统最大值代表无路
	// 返回值是最小连通图的路径之和
	public static int prim(int[][] graph) {
		int size = graph.length;
		int[] distances = new int[size];
		boolean[] visit = new boolean[size];
		visit[0] = true;
		for (int i = 0; i < size; i++) {
			distances[i] = graph[0][i];
		}
		int sum = 0;
		for (int i = 1; i < size; i++) {
			int minPath = Integer.MAX_VALUE;
			int minIndex = -1;
			for (int j = 0; j < size; j++) {
				if (!visit[j] && distances[j] < minPath) {
					minPath = distances[j];
					minIndex = j;
				}
			}
			if (minIndex == -1) {
				return sum;
			}
			visit[minIndex] = true;
			sum += minPath;
			for (int j = 0; j < size; j++) {
				if (!visit[j] && distances[j] > graph[minIndex][j]) {
					distances[j] = graph[minIndex][j];
				}
			}
		}
		return sum;
	}
}
