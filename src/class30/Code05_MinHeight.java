package class30;

public class Code05_MinHeight {

	public static class Node {
		public int val;
		public Node left;
		public Node right;

		public Node(int x) {
			val = x;
		}
	}

	public static int minHeight1(Node head) {
		if (head == null) {
			return 0;
		}
		return p(head);
	}

	// 返回x为头的树，最小深度是多少
	public static int p(Node x) {
		if (x.left == null && x.right == null) {
			return 1;
		}
		// 左右子树起码有一个不为空
		int leftH = Integer.MAX_VALUE;
		if (x.left != null) {
			leftH = p(x.left);
		}
		int rightH = Integer.MAX_VALUE;
		if (x.right != null) {
			rightH = p(x.right);
		}
		return 1 + Math.min(leftH, rightH);
	}

	// 根据morris遍历改写

	/**
	 * 变量curLevel时时刻刻随着节点更新当前的level
	 *  只到一次的节点，level++
	 *  到两次的节点，第一次到的时候计算逻辑level++，第二次到的时候，计算逻辑为level减去左树右边界上的节点个数，如果左树最右节点是叶节点，更新最小值
	 *
	 * 变量minHight记录最小的高度
	 * 最后单独算一下整棵树右边界的高度，如果最右节点是叶节点的化更新最小值
	 *
	 * @param head
	 * @return
	 */
	public static int minHeight2(Node head) {
		if (head == null) {
			return 0;
		}
		Node cur = head;
		Node mostRight = null;
		int curLevel = 0;
		int minHeight = Integer.MAX_VALUE;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				int rightBoardSize = 1;
				while (mostRight.right != null && mostRight.right != cur) {
					rightBoardSize++;
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) { // 第一次到达
					// 要去往左孩子，level直接加1
					curLevel++;
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else { // 第二次到达
					// 第二次回来的时候，要检查最右孩子是不是叶节点
					if (mostRight.left == null) {
						// 叶子节点，计算最小level
						minHeight = Math.min(minHeight, curLevel);
					}
					// 因为是从最右孩子回来的，要减去最右孩子路上的节点数，算出当前level
					curLevel -= rightBoardSize;
					mostRight.right = null;
				}
			} else { // 只有一次到达
				// 左孩子为空，往右跳，level++
				curLevel++;
			}
			cur = cur.right;
		}
		// 最后单独计算一次整棵树的最右边，要看最右子节点是不是叶节点
		int finalRight = 1;
		cur = head;
		while (cur.right != null) {
			finalRight++;
			cur = cur.right;
		}
		if (cur.left == null) {
			minHeight = Math.min(minHeight, finalRight);
		}
		return minHeight;
	}

	// for test
	public static Node generateRandomBST(int maxLevel, int maxValue) {
		return generate(1, maxLevel, maxValue);
	}

	// for test
	public static Node generate(int level, int maxLevel, int maxValue) {
		if (level > maxLevel || Math.random() < 0.5) {
			return null;
		}
		Node head = new Node((int) (Math.random() * maxValue));
		head.left = generate(level + 1, maxLevel, maxValue);
		head.right = generate(level + 1, maxLevel, maxValue);
		return head;
	}

	public static void main(String[] args) {
		int treeLevel = 7;
		int nodeMaxValue = 5;
		int testTimes = 100000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			Node head = generateRandomBST(treeLevel, nodeMaxValue);
			int ans1 = minHeight1(head);
			int ans2 = minHeight2(head);
			if (ans1 != ans2) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish!");

	}

}
