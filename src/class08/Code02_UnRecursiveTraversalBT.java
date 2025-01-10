package class08;

import java.util.Stack;

public class Code02_UnRecursiveTraversalBT {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int v) {
			value = v;
		}
	}

	public static void pre(Node head) {
		System.out.print("pre-order: ");

		Stack<Node> stack = new Stack<Node>();
		while (head != null || !stack.isEmpty()) {
			while (head != null) {
				System.out.print(head.value + " "); // 访问当前节点
				stack.push(head);
				head = head.left;
			}
			if (!stack.isEmpty()) {
				head = stack.pop();
				head = head.right;
			}
		}
		System.out.println();
	}

	public static void in(Node root) {
		System.out.print("in-order: ");

		Stack<Node> stack = new Stack<Node>();
		while (root != null || !stack.isEmpty()) {
			while (root != null) {
				stack.push(root);
				root = root.left;
			}
			if (!stack.isEmpty()) {
				root = stack.pop();
				System.out.print(root.value + " "); // 访问当前节点
				root = root.right;
			}
		}
		System.out.println();
	}

	public static void post(Node root) {
		System.out.print("post-order: ");
		Stack<Node> stack = new Stack<>();
		Node prev = null;
		while (root != null || !stack.isEmpty()) {
			while (root != null) {
				stack.push(root);
				root = root.left;
			}
			root = stack.peek();
			// 第三次回到自己，弹出，且本子树也全部处理完了
			if (root.right == null || root.right == prev) {
				System.out.print(root.value + " "); // 访问当前节点
				stack.pop();
				prev = root;
				root = null;
			} else {
				// 第二次回到自己，跳去右孩子
				root = root.right;
			}
		}
	}

	public static void pos1(Node head) {
		System.out.print("pos-order: ");
		if (head != null) {
			Stack<Node> s1 = new Stack<Node>();
			Stack<Node> s2 = new Stack<Node>();
			s1.push(head);
			while (!s1.isEmpty()) {
				head = s1.pop(); // 头 右 左
				s2.push(head);
				if (head.left != null) {
					s1.push(head.left);
				}
				if (head.right != null) {
					s1.push(head.right);
				}
			}
			// 左 右 头
			while (!s2.isEmpty()) {
				System.out.print(s2.pop().value + " ");
			}
		}
		System.out.println();
	}

	public static void pos2(Node root) {
		System.out.print("pos-order: ");
		Stack<Node> stack = new Stack<>();
		Node pre = null;
		while (root != null || !stack.isEmpty()) {
			while (root != null) {
				stack.push(root);
				root = root.left;
			}
			root = stack.peek();
			if(root.right == null || root.right == pre) {
				System.out.print(root.value + " ");
				stack.pop();
				pre = root;
				root = null;
			} else {
				root = root.right;
			}
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Node head = new Node(1);
		head.left = new Node(2);
		head.right = new Node(3);
		head.left.left = new Node(4);
		head.left.right = new Node(5);
		head.right.left = new Node(6);
		head.right.right = new Node(7);

		pre(head);
		System.out.println("========");
		in(head);
		System.out.println("========");
		pos1(head);
		System.out.println("========");
		pos2(head);
		System.out.println("========");
	}

}
