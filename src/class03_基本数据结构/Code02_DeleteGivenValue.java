package class03_基本数据结构;

/**
 * 链表删除给定的值，不一定只有一个，需要遍历到底
 */
public class Code02_DeleteGivenValue {

	public static class Node {
		public int value;
		public Node next;

		public Node(int data) {
			this.value = data;
		}
	}

	// head = removeValue(head, 2);
	public static Node removeValue(Node head, int num) {
		// head来到第一个不需要删的位置
		while (head != null) {
			if (head.value != num) {
				break;
			}
			head = head.next;
		}
		// 1 ) head == null
		// 2 ) head != null
		Node pre = head;
		Node cur = head;
		while (cur != null) {
			if (cur.value == num) {
				pre.next = cur.next;
			} else {
				pre = cur;
			}
			cur = cur.next;
		}
		return head;
	}

	public static Node remove(Node head, int num) {
		// head可能就是要删的数，那就删掉，直到第一个不需要删的位置
		 while (head != null) {
			 if(head.value != num) {
				 break;
			 }
			 head = head.next;
		 }
		 Node pre = head;
		 Node cur = head;
		 while (cur != null) {
			 if(cur.value == num) {
				 pre.next = cur.next;
			 }else {
				 pre = cur;
			 }
			 cur = cur.next;
		 }
		 return head;
	}
}
