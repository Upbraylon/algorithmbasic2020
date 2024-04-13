package class16;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * 图的深度优先遍历
 * 1）利用栈实现
 * 2）从源节点开始把节点按照深度放入栈，然后弹出
 * 3）每弹出一个点，把给节点下一个没有进过栈的邻接点放入栈
 * 4）直到栈变空
 */
public class Code02_DFS {

	public static void dfs(Node node) {
		if (node == null) {
			return;
		}
		Stack<Node> stack = new Stack<>();
		HashSet<Node> set = new HashSet<>();
		stack.add(node);
		set.add(node);
		System.out.println(node.value);
		while (!stack.isEmpty()) {
			Node cur = stack.pop();
			for (Node next : cur.nexts) {
				// 邻居节点还有没入栈的，本节点还得重新入栈
				if (!set.contains(next)) {
					// 把当初弹出的节点重新压回去
					stack.push(cur);
					stack.push(next);
					set.add(next);
					System.out.println(next.value);
					break;
				}
			}
		}
	}

	public static void dfss(Node node) {
		if(node == null) {
			return;
		}

		Stack<Node> stack = new Stack<>();
		stack.push(node);
		Set<Node> set = new HashSet<>();
		set.add(node);
		System.out.println(node.value);
		while (!stack.isEmpty()) {
			Node cur = stack.pop();
			for (Node next : cur.nexts) {
				if(!set.contains(next)) {
					stack.push(cur);
					stack.push(next);
					set.add(next);
					System.out.println(next.value);
					// 这里要break，否则只遍历了同一节点的邻居，就不是深度了
					break;
				}
			}
		}
	}
	

}
