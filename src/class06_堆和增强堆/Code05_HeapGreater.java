package class06_堆和增强堆;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/*
 * T一定要是非基础类型，有基础类型需求包一层
 */
public class Code05_HeapGreater<T> {

	private final ArrayList<T> heap;
	private final HashMap<T, Integer> indexMap;
	private int heapSize;
	private final Comparator<? super T> comp;

	public Code05_HeapGreater(Comparator<T> c) {
		heap = new ArrayList<>();
		indexMap = new HashMap<>();
		heapSize = 0;
		comp = c;
	}

	public boolean isEmpty() {
		return heapSize == 0;
	}

	public int size() {
		return heapSize;
	}

	public boolean contains(T obj) {
		return indexMap.containsKey(obj);
	}

	public T peek() {
		return heap.get(0);
	}

	public void push(T obj) {
		heap.add(obj);
		indexMap.put(obj, heapSize);
		heapInsert(heapSize++);
	}

	public T pop() {
		T ans = heap.get(0);
		swap(0, heapSize - 1);
		indexMap.remove(ans);
		heap.remove(--heapSize);
		heapify(0);
		return ans;
	}

	public void remove(T obj) {
		T replace = heap.get(heapSize - 1);
		int index = indexMap.get(obj);
		indexMap.remove(obj);
		heap.remove(--heapSize);
		if (obj != replace) {
			heap.set(index, replace);
			indexMap.put(replace, index);
			resign(replace);
		}
	}

	public void resign(T obj) {
		heapInsert(indexMap.get(obj));
		heapify(indexMap.get(obj));
	}

	// 请返回堆上的所有元素
	public List<T> getAllElements() {
		return new ArrayList<>(heap);
	}

	private void heapInsert(int index) {
		// 这地方有点绕
		// 小于0，第一个元素在前，这里就是大的元素在前，所以要小的-大的
		// 不论是数字的比较大小还是比较器的比较，都是为了一个优先级
		// compare的参数位置和优先级有关，结果小于0，0位置优先，结果大于0，1位置优先
		// 因此，如果把优先级高的放在参数0位置，结果就要判断是否小于0
		// 否则放在1位置，结果就要判断是否大于0
		// 子类的实现也不用关注我里取的是大于0还是小于0，子类若想让第一个参数优先级最高，就构造小于0的结果；
		// 若想让第二个参数优先级最高就构造大于0的结果
		while (comp.compare(heap.get(index), heap.get((index - 1) / 2)) < 0) {
			swap(index, (index - 1) / 2);
			index = (index - 1) / 2;
		}
	}

	private void heapify(int index) {
		int left = index * 2 + 1;
		while (left < heapSize) {
			int best = left + 1 < heapSize && comp.compare(heap.get(left + 1), heap.get(left)) < 0 ? (left + 1) : left;
			best = comp.compare(heap.get(best), heap.get(index)) < 0 ? best : index;
			if (best == index) {
				break;
			}
			swap(best, index);
			index = best;
			left = index * 2 + 1;
		}
	}

	private void swap(int i, int j) {
		T o1 = heap.get(i);
		T o2 = heap.get(j);
		heap.set(i, o2);
		heap.set(j, o1);
		indexMap.put(o2, i);
		indexMap.put(o1, j);
	}

	static class Inner<T> {
		public T value;

		public Inner(T v) {
			value = v;
		}
	}
}
