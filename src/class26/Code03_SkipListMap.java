package class26;

import java.util.ArrayList;

public class Code03_SkipListMap {

	// 跳表的节点定义
	public static class SkipListNode<K extends Comparable<K>, V> {
		public K key;
		public V val;
		// 多层链表，每个节点有多层链表
		public ArrayList<SkipListNode<K, V>> nextNodes;

		public SkipListNode(K k, V v) {
			key = k;
			val = v;
			nextNodes = new ArrayList<SkipListNode<K, V>>();
		}

		// 遍历的时候，如果是往右遍历到的null(next == null), 遍历结束
		// 头(null), 头节点的null，认为最小
		// node  -> 头，node(null, "")  node.isKeyLess(!null)  true
		// node里面的key是否比otherKey小，true，不是false
		public boolean isKeyLess(K otherKey) {
			//  otherKey == null -> false 
			return otherKey != null && (key == null || key.compareTo(otherKey) < 0);
		}

		public boolean isKeyEqual(K otherKey) {
			return (key == null && otherKey == null)
					|| (key != null && otherKey != null && key.compareTo(otherKey) == 0);
		}

	}

	public static class SkipListMap<K extends Comparable<K>, V> {
		private static final double PROBABILITY = 0.5; // < 0.5 继续做，>=0.5 停  概率
		private SkipListNode<K, V> head; // 头节点
		private int size; // 挂了多少个节点
		private int maxLevel; // 最高有多少层

		public SkipListMap() {
			// 整个跳表最小的点
			head = new SkipListNode<K, V>(null, null);
			head.nextNodes.add(null); // 0 第0层
			size = 0;
			maxLevel = 0;
		}

		// 从最高层开始，一路找下去，
		// 最终，找到第0层的<key的最右的节点
		// 下台阶算法
		private SkipListNode<K, V> mostRightLessNodeInTree(K key) {
			if (key == null) {
				return null;
			}
			int level = maxLevel; // 从最高层开始找
			SkipListNode<K, V> cur = head;
			while (level >= 0) { // 从上层跳下层
				//  cur  level  -> level-1
				// 当前层小于key的最右节点在哪里
				cur = mostRightLessNodeInLevel(key, cur, level--);
			}
			return cur;
		}

		// 在level层里，如何往右移动
		// 现在来到的节点是cur，来到了cur的level层，在level层上，找到<key最后一个节点并返回
		private SkipListNode<K, V> mostRightLessNodeInLevel(K key, 
				SkipListNode<K, V> cur, 
				int level) {
			SkipListNode<K, V> next = cur.nextNodes.get(level);
			while (next != null && next.isKeyLess(key)) {
				cur = next;
				next = cur.nextNodes.get(level);
			}
			return cur;
		}

		/**
		 * 跳表中是否包含key
		 * @param key
		 * @return
		 */
		public boolean containsKey(K key) {
			if (key == null) {
				return false;
			}
			SkipListNode<K, V> less = mostRightLessNodeInTree(key);
			SkipListNode<K, V> next = less.nextNodes.get(0);
			return next != null && next.isKeyEqual(key);
		}

		// 新增、改value
		public void put(K key, V value) {
			if (key == null) {
				return;
			}
			// 0层上，最右一个，< key 的Node -> >key
			// 第0层所有的记录都有
			// 假设key=70，在第零层找小于70的最右节点。假设为69，那么69再往右 看一个节点，看是否是70
			SkipListNode<K, V> less = mostRightLessNodeInTree(key);
			SkipListNode<K, V> find = less.nextNodes.get(0);
			// 说明原来的70节点在表里，更新值即可
			if (find != null && find.isKeyEqual(key)) {
				find.val = value;
			} else { // find == null   8   7   9
				// find==null || find != key,70之前没加过，进入新增环节
				size++;
				int newNodeLevel = 0;
				// 随机决定有几层链表
				while (Math.random() < PROBABILITY) {
					newNodeLevel++;
				}
				// newNodeLevel
				// 新节点的链表层数大于原来最大的
				// head的链表要和新节点的链表层数一样高
				// 升链表只会发生在head上
				while (newNodeLevel > maxLevel) {
					head.nextNodes.add(null);
					maxLevel++;
				}
				// 初始化新节点
				SkipListNode<K, V> newNode = new SkipListNode<K, V>(key, value);
				for (int i = 0; i <= newNodeLevel; i++) {
					newNode.nextNodes.add(null);
				}

				int level = maxLevel;
				SkipListNode<K, V> pre = head;
				while (level >= 0) {
					// level 层中，找到最右的 < key 的节点
					pre = mostRightLessNodeInLevel(key, pre, level);
					// 如果原来的链表层数要大于新节点的层数，就level--，一直到和新节点的层数相等再开始挂
					if (level <= newNodeLevel) {
						// 从新节点的最高层开始，新节点后接pre的下一个节点，pre后接新节点
						newNode.nextNodes.set(level, pre.nextNodes.get(level));
						pre.nextNodes.set(level, newNode);
					}
					level--;
				}
			}
		}

		public V get(K key) {
			if (key == null) {
				return null;
			}
			SkipListNode<K, V> less = mostRightLessNodeInTree(key);
			SkipListNode<K, V> next = less.nextNodes.get(0);
			return next != null && next.isKeyEqual(key) ? next.val : null;
		}

		/**
		 * 删完一个节点之后，发现最高层没节点了，最好将最高层降下来
		 * 往右到不能再往右，往下，循环
		 *
		 * @param key
		 */
		public void remove(K key) {
			if (containsKey(key)) {
				size--;
				int level = maxLevel;
				SkipListNode<K, V> pre = head;
				while (level >= 0) {
					pre = mostRightLessNodeInLevel(key, pre, level);
					SkipListNode<K, V> next = pre.nextNodes.get(level);
					// 1）在这一层中，pre下一个就是key
					// 2）在这一层中，pre的下一个key是>要删除key
					if (next != null && next.isKeyEqual(key)) {
						// free delete node memory -> C++
						// level : pre -> next(key) -> ...
						pre.nextNodes.set(level, next.nextNodes.get(level));
					}
					// 在level层只有一个节点了，就是默认节点head，降低链表层数
					if (level != 0 && pre == head && pre.nextNodes.get(level) == null) {
						head.nextNodes.remove(level);
						maxLevel--;
					}
					level--;
				}
			}
		}

		/**
		 * 第0层的第一个
		 * o(1)
		 * @return
		 */
		public K firstKey() {
			return head.nextNodes.get(0) != null ? head.nextNodes.get(0).key : null;
		}

		/**
		 * 第0层的最后一个
		 * 不要从第0层遍历,复杂度是O(n)
		 *
		 * @return
		 */
		public K lastKey() {
			int level = maxLevel;
			SkipListNode<K, V> cur = head;
			while (level >= 0) {
				SkipListNode<K, V> next = cur.nextNodes.get(level);
				while (next != null) {
					cur = next;
					next = cur.nextNodes.get(level);
				}
				level--;
			}
			return cur.key;
		}

		/**
		 * 大于等于key
		 *
		 * @param key
		 * @return
		 */
		public K ceilingKey(K key) {
			if (key == null) {
				return null;
			}
			SkipListNode<K, V> less = mostRightLessNodeInTree(key);
			SkipListNode<K, V> next = less.nextNodes.get(0);
			return next != null ? next.key : null;
		}

		/**
		 * 小于等于key
		 *
		 * @param key
		 * @return
		 */
		public K floorKey(K key) {
			if (key == null) {
				return null;
			}
			SkipListNode<K, V> less = mostRightLessNodeInTree(key);
			SkipListNode<K, V> next = less.nextNodes.get(0);
			return next != null && next.isKeyEqual(key) ? next.key : less.key;
		}

		public int size() {
			return size;
		}

	}

	// for test
	public static void printAll(SkipListMap<String, String> obj) {
		for (int i = obj.maxLevel; i >= 0; i--) {
			System.out.print("Level " + i + " : ");
			SkipListNode<String, String> cur = obj.head;
			while (cur.nextNodes.get(i) != null) {
				SkipListNode<String, String> next = cur.nextNodes.get(i);
				System.out.print("(" + next.key + " , " + next.val + ") ");
				cur = next;
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		SkipListMap<String, String> test = new SkipListMap<>();
		printAll(test);
		System.out.println("======================");
		test.put("A", "10");
		printAll(test);
		System.out.println("======================");
		test.remove("A");
		printAll(test);
		System.out.println("======================");
		test.put("E", "E");
		test.put("B", "B");
		test.put("A", "A");
		test.put("F", "F");
		test.put("C", "C");
		test.put("D", "D");
		printAll(test);
		System.out.println("======================");
		System.out.println(test.containsKey("B"));
		System.out.println(test.containsKey("Z"));
		System.out.println(test.firstKey());
		System.out.println(test.lastKey());
		System.out.println(test.floorKey("D"));
		System.out.println(test.ceilingKey("D"));
		System.out.println("======================");
		test.remove("D");
		printAll(test);
		System.out.println("======================");
		System.out.println(test.floorKey("D"));
		System.out.println(test.ceilingKey("D"));
		

	}

}
