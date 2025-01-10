package class13_图;

import java.util.*;

/**
 * 图的拓扑排序算法
 *
 * 1）在图中找到所有入度为0的点输出
 * 2）把所有入度为0的点在图中删掉，继续找入度为0的点输出，周而复始
 * 3）图的所有点都被删除后，一次输出的顺序就是拓扑排序
 *
 * 要求：有向图且其中没有环
 * 应用：事件安排、编译顺序
 */
// OJ链接：https://www.lintcode.com/problem/topological-sorting
public class Code03_TopologicalOrderBFS {

	// 不要提交这个类
	public static class DirectedGraphNode {
		public int label;
		public ArrayList<DirectedGraphNode> neighbors;

		public DirectedGraphNode(int x) {
			label = x;
			neighbors = new ArrayList<DirectedGraphNode>();
		}
	}

	// 提交下面的
	public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
		HashMap<DirectedGraphNode, Integer> indegreeMap = new HashMap<>();
		// 初始化节点-入度map结构
		for (DirectedGraphNode cur : graph) {
			indegreeMap.put(cur, 0);
		}
		// 遍历每个节点，其相邻节点入度+1
		for (DirectedGraphNode cur : graph) {
			for (DirectedGraphNode next : cur.neighbors) {
				indegreeMap.put(next, indegreeMap.get(next) + 1);
			}
		}
		// 入度为0的节点，入零队列
		Queue<DirectedGraphNode> zeroQueue = new LinkedList<>();
		for (DirectedGraphNode cur : indegreeMap.keySet()) {
			if (indegreeMap.get(cur) == 0) {
				zeroQueue.add(cur);
			}
		}
		ArrayList<DirectedGraphNode> ans = new ArrayList<>();
		// 出队列，收集答案，同时其相邻节点入度-1
		while (!zeroQueue.isEmpty()) {
			DirectedGraphNode cur = zeroQueue.poll();
			ans.add(cur);
			for (DirectedGraphNode next : cur.neighbors) {
				indegreeMap.put(next, indegreeMap.get(next) - 1);
				if (indegreeMap.get(next) == 0) {
					zeroQueue.offer(next);
				}
			}
		}
		return ans;
	}

	public static void main(String[] args) {
		// graph = {0,1,2,3#1,4#2,4,5#3,4,5#4#5}
		DirectedGraphNode node0 = new DirectedGraphNode(0);
		DirectedGraphNode node1 = new DirectedGraphNode(1);
		DirectedGraphNode node2 = new DirectedGraphNode(2);
		DirectedGraphNode node3 = new DirectedGraphNode(3);
		DirectedGraphNode node4 = new DirectedGraphNode(4);
		DirectedGraphNode node5 = new DirectedGraphNode(5);
		node0.neighbors.add(node1);
		node0.neighbors.add(node2);
		node0.neighbors.add(node3);

		node1.neighbors.add(node4);
		node2.neighbors.add(node4);
		node2.neighbors.add(node5);

		node3.neighbors.add(node4);
		node3.neighbors.add(node5);

		ArrayList<DirectedGraphNode> graph = new ArrayList<>();
		graph.add(node0);
		graph.add(node1);
		graph.add(node2);
		graph.add(node3);
		graph.add(node4);
		graph.add(node5);
		ArrayList<DirectedGraphNode> directedGraphNodes = topSort(graph);
		for (DirectedGraphNode node : directedGraphNodes) {
			System.out.print(node.label + " ");
		}
		System.out.println();
		List<DirectedGraphNode> res = topSort1(graph);
		for (DirectedGraphNode node : res) {
			System.out.print(node.label + " ");
		}
	}

	public static List<DirectedGraphNode> topSort1(ArrayList<DirectedGraphNode> graph) {
		Map<DirectedGraphNode, Integer> inMap = new HashMap<>();
		for (DirectedGraphNode node : graph) {
			inMap.put(node, 0);
		}

		for (DirectedGraphNode node : graph) {
			for (DirectedGraphNode next : node.neighbors) {
				inMap.put(next, inMap.get(next)+1);
			}
		}

		Queue<DirectedGraphNode> zeroQueue = new LinkedList<>();
		for (DirectedGraphNode node : inMap.keySet()) {
			if(inMap.get(node) == 0) {
				zeroQueue.add(node);
			}
		}
		List<DirectedGraphNode> res = new ArrayList<>();
		while (!zeroQueue.isEmpty()) {
			DirectedGraphNode cur = zeroQueue.poll();
			res.add(cur);
			for (DirectedGraphNode node : cur.neighbors) {
				inMap.put(node, inMap.get(node) -1);
				if(inMap.get(node) == 0) {
					zeroQueue.offer(node);
				}
			}
		}
		return res;
	}
}
