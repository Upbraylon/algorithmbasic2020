package class30;

public class Code01_MorrisTraversal {

	public static class Node {
		public int value;
		Node left;
		Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	public static void process(Node root) {
		if (root == null) {
			return;
		}
		// 1
		process(root.left);
		// 2
		process(root.right);
		// 3
	}

	public static void morris(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			}
			cur = cur.right;
		}
	}

	public static void morrisPre(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					System.out.print(cur.value + " ");
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			} else {
				System.out.print(cur.value + " ");
			}
			cur = cur.right;
		}
		System.out.println();
	}

	public static void morrisIn(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			}
			System.out.print(cur.value + " ");
			cur = cur.right;
		}
		System.out.println();
	}

	public static void morrisPos(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
					printEdge(cur.left);
				}
			}
			cur = cur.right;
		}
		printEdge(head);
		System.out.println();
	}

	public static void printEdge(Node head) {
		Node tail = reverseEdge(head);
		Node cur = tail;
		while (cur != null) {
			System.out.print(cur.value + " ");
			cur = cur.right;
		}
		reverseEdge(tail);
	}

	public static Node reverseEdge(Node from) {
		Node pre = null;
		Node next = null;
		while (from != null) {
			next = from.right;
			from.right = pre;
			pre = from;
			from = next;
		}
		return pre;
	}

	// for test -- print tree
	public static void printTree(Node head) {
		System.out.println("Binary Tree:");
		printInOrder(head, 0, "H", 17);
		System.out.println();
	}

	public static void printInOrder(Node head, int height, String to, int len) {
		if (head == null) {
			return;
		}
		printInOrder(head.right, height + 1, "v", len);
		String val = to + head.value + to;
		int lenM = val.length();
		int lenL = (len - lenM) / 2;
		int lenR = len - lenM - lenL;
		val = getSpace(lenL) + val + getSpace(lenR);
		System.out.println(getSpace(height * len) + val);
		printInOrder(head.left, height + 1, "^", len);
	}

	public static String getSpace(int num) {
		String space = " ";
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < num; i++) {
			buf.append(space);
		}
		return buf.toString();
	}

	/**
	 * 判断一棵树是不是搜索二叉树，中序遍历是升序
	 * 判断时机：只到一次的节点以及能到两次第二次到的时候
	 * @param head
	 * @return
	 */
	public static boolean isBST(Node head) {
		if (head == null) {
			return true;
		}
		Node cur = head;
		Node mostRight = null;
		Integer pre = null;
		boolean ans = true;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			}
			if (pre != null && pre >= cur.value) {
				ans = false;
			}
			pre = cur.value;
			cur = cur.right;
		}
		return ans;
	}

	public static void main(String[] args) {
		Node head = new Node(4);
		head.left = new Node(2);
		head.right = new Node(6);
		head.left.left = new Node(1);
		head.left.right = new Node(3);
		head.right.left = new Node(5);
		head.right.right = new Node(7);
		printTree(head);
		morrisIn(head);
		morrisPre(head);
		morrisPos(head);
		morrisPos_test(head);
		morris_post(head);
		printTree(head);

	}

	/**
	 * morris后序遍历：第二次回到节点的时候，逆序打印左树右边界
	 * 最后单独打印整棵树的右边界
	 *
	 * 只到自己一次的节点不做任何动作
	 * @param head
	 */
	public static void morris_post(Node head){
		if(head == null){
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null){
			if(cur.left == null){
				cur = cur.right;
				continue;
			}
			mostRight = cur.left;
			while (mostRight.right != null && mostRight.right != cur){
				mostRight = mostRight.right;
			}
			// 第一次到节点
			if(mostRight.right == null){
				mostRight.right = cur;
				cur = cur.left;
			} else {
				// 第二次到节点
				mostRight.right = null;
				// 要先恢复左树最右子节点的结构再打印，否则会把整棵树的右边界也给串进来打印
				// 因为未恢复之前mostRight.right会指向cur节点
				printLeftTreeRightEdge(cur.left);
				cur = cur.right;
			}
		}
		printLeftTreeRightEdge(head);
		System.out.println();
	}

	private static void printLeftTreeRightEdge(Node head) {
		Node tail = revese(head);
		Node cur = tail;
		while (cur != null){
			System.out.print(cur.value + " ");
			cur = cur.right;
		}
		revese(tail);
	}

	private static Node revese(Node head){
		if(head == null) {
			return null;
		}
		Node pre = null;
		Node next = null;
		while (head != null){
			next = head.right;
			head.right = pre;
			pre = head;
			head = next;
		}
		return pre;
	}
	public static void morrisPos_test(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			if(cur.left == null){
				cur = cur.right;
				continue;
			}
			mostRight = cur.left;
			while (mostRight.right != null && mostRight.right != cur) {
				mostRight = mostRight.right;
			}
			if (mostRight.right == null) {
				mostRight.right = cur;
				cur = cur.left;
			} else {
				mostRight.right = null;
				printEdge(cur.left);
				cur = cur.right;
			}
		}
		printEdge(head);
		System.out.println();
	}

	/**
	 * 是否是搜索二叉树
	 * 中序遍历是否升序
	 * @param head
	 * @return
	 */
	public static boolean is_BST(Node head) {
		 if(head == null){
			 return true;
		 }
		 Node cur = head;
		 Node mostRight = null;
		 Integer pre = null;
		 boolean ans = true;
		 while (cur != null){
			 mostRight = cur.left;
			 if(mostRight != null){
				 while (mostRight.right != null && mostRight.right != cur){
					 mostRight = mostRight.right;
				 }
				 if(mostRight.right == null){
					 mostRight.right = cur;
					 cur = cur.left;
					 continue;
				 }else {
					 mostRight.right = null;
				 }
			 }

			 if(pre != null && pre >= cur.value){
				 ans = false;
			 }
			 pre = cur.value;
			 cur = cur.right;
		 }
		 return ans;
	}
}
