package class16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * 最大深度解法
 *
 * 思想：X出发走出的最大深度大于Y，X拓扑在Y之前
 * 如果从X出发，后面的节点个数为M
 * 从Y出发，后面的节点个数为N
 * 若M>=N, 则X<=Y，即X的拓扑序在Y之前
 */
// OJ链接：https://www.lintcode.com/problem/topological-sorting
public class Code03_TopologicalOrderDFS1 {

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
	public static class Record {
		public DirectedGraphNode node;
		public int deep;

		public Record(DirectedGraphNode n, int o) {
			node = n;
			deep = o;
		}
	}

	/**
	 * deep深度大的，拓扑序在前
	 */
	public static class MyComparator implements Comparator<Record> {

		@Override
		public int compare(Record o1, Record o2) {
			return o2.deep - o1.deep;
		}
	}

	public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
		HashMap<DirectedGraphNode, Record> order = new HashMap<>();
		for (DirectedGraphNode cur : graph) {
			f(cur, order);
		}
		ArrayList<Record> recordArr = new ArrayList<>();
		for (Record r : order.values()) {
			recordArr.add(r);
		}
		recordArr.sort(new MyComparator());
		ArrayList<DirectedGraphNode> ans = new ArrayList<DirectedGraphNode>();
		for (Record r : recordArr) {
			ans.add(r.node);
		}
		return ans;
	}

	public static Record f(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> order) {
		// 记忆化搜索，当前节点后面有多少个节点，存在直接返回
		if (order.containsKey(cur)) {
			return order.get(cur);
		}
		int follow = 0;// 所有邻居最大的深度值
		for (DirectedGraphNode next : cur.neighbors) {
			follow = Math.max(follow, f(next, order).deep);
		}
		// 构建当前节点的record，deep为后面连接节点的最大deep+1
		Record ans = new Record(cur, follow + 1);
		order.put(cur, ans);
		return ans;
	}

}
