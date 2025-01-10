package class05;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Code04_FindMinKth {

	public static class MaxHeapComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {
			return o2 - o1;
		}

	}

	// 利用大根堆，时间复杂度O(N*logK)
	public static int minKth1(int[] arr, int k) {
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>(new MaxHeapComparator());
		for (int i = 0; i < k; i++) {
			maxHeap.add(arr[i]);
		}
		for (int i = k; i < arr.length; i++) {
			if (!maxHeap.isEmpty() && arr[i] < maxHeap.peek()) {
				maxHeap.poll();
				maxHeap.add(arr[i]);
			}
		}
		return maxHeap.isEmpty() ? Integer.MAX_VALUE : maxHeap.peek();
	}

	// 改写快排，时间复杂度O(N)
	// k >= 1

	/**
	 * 时间复杂度O(n)
	 * master公式：T(n) = a*T(n/b) + O(n^d)--T(n) 代表递归方法处理规模为n的数据量的时间复杂度，T(n/b) 代表调用子递归方法的总体时间复杂度，O(n^d) 代表调用子递归方法这行代码外其他代码（下面简称递归外代码）的时间复杂度。
	 * a: 每次递归调用子问题的数量。即在一个递归方法，需要调用几次子递归方法
	 * b: 子问题的规模缩小的比例。例如二分法递归搜索时，每次需要查找的数据都缩小了一半，那么 b=2
	 * d: 每次递归调用之外的代码时间复杂度的参数。例如二分法递归搜索时，每次递归时除了调用递归的方法，没有什么for循环代码，时间复杂度是 O(1)，即 nd=1，因此 d=0; 这里每个递归方法O(n),d=1
	 *
	 * 根据Master公式推导时间复杂度
	 * 只要一个递归方法满足Master公式，那么它的时间复杂度就可以确定：
	 *
	 * 当 d<logba 时，递归时间复杂度为：O(nlogba)
	 * 当 d=logba 时，递归时间复杂度为：O(nd * logn)
	 * 当 d>logba 时，递归时间复杂度为：O(nd)
	 * log21=0 < d, 时间复杂度O(n^d) = O(n)
	 * @param array
	 * @param k
	 * @return
	 */
	public static int minKth2(int[] array, int k) {
		int[] arr = copyArray(array);
		return process2(arr, 0, arr.length - 1, k - 1);
	}

	public static int[] copyArray(int[] arr) {
		int[] ans = new int[arr.length];
		for (int i = 0; i != ans.length; i++) {
			ans[i] = arr[i];
		}
		return ans;
	}

	// arr 第k小的数
	// process2(arr, 0, N-1, k-1)
	// arr[L..R]  范围上，如果排序的话(不是真的去排序)，找位于index的数
	// index [L..R]
	public static int process2(int[] arr, int L, int R, int index) {
		if (L == R) { // L = =R ==INDEX
			return arr[L];
		}
		// 不止一个数  L +  [0, R -L]
		int pivot = arr[L + (int) (Math.random() * (R - L + 1))];
		int[] range = partision1(arr, L, R, pivot);
		if (index >= range[0] && index <= range[1]) {
			return arr[index];
		} else if (index < range[0]) {
			return process2(arr, L, range[0] - 1, index);
		} else {
			return process2(arr, range[1] + 1, R, index);
		}
	}

	public static int[] partition(int[] arr, int L, int R, int pivot) {
		int less = L - 1;
		int more = R + 1;
		int cur = L;
		while (cur < more) {
			if (arr[cur] < pivot) {
				swap(arr, ++less, cur++);
			} else if (arr[cur] > pivot) {
				swap(arr, cur, --more);
			} else {
				cur++;
			}
		}
		return new int[] { less + 1, more - 1 };
	}

	public static void swap(int[] arr, int i1, int i2) {
		int tmp = arr[i1];
		arr[i1] = arr[i2];
		arr[i2] = tmp;
	}

	// 利用bfprt算法，时间复杂度O(N)
	public static int minKth3(int[] array, int k) {
		int[] arr = copyArray(array);
		return bfprt(arr, 0, arr.length - 1, k - 1);
	}

	// arr[L..R]  如果排序的话，位于index位置的数，是什么，返回
	public static int bfprt(int[] arr, int L, int R, int index) {
		if (L == R) {
			return arr[L];
		}
		// L...R  每五个数一组
		// 每一个小组内部排好序
		// 小组的中位数组成新数组
		// 这个新数组的中位数返回
		// 这个pivot至少排除了3/10规模的数不用考虑
		int pivot = medianOfMedians(arr, L, R);
		int[] range = partision1(arr, L, R, pivot);
		// range范围内的数都相等，直接返回index即可
		if (index >= range[0] && index <= range[1]) {
			return arr[index];
		} else if (index < range[0]) {
			return bfprt(arr, L, range[0] - 1, index);
		} else {
			return bfprt(arr, range[1] + 1, R, index);
		}
	}

	// arr[L...R]  五个数一组
	// 每个小组内部排序
	// 每个小组中位数领出来，组成marr
	// marr中的中位数，返回
	public static int medianOfMedians(int[] arr, int L, int R) {
		int size = R - L + 1;
		int offset = size % 5 == 0 ? 0 : 1;
		int[] mArr = new int[size / 5 + offset];
		for (int team = 0; team < mArr.length; team++) {
			int teamFirst = L + team * 5;
			// L ... L + 4
			// L +5 ... L +9
			// L +10....L+14
			// 小于等于五个一组取中位数
			mArr[team] = getMedian(arr, teamFirst, Math.min(R, teamFirst + 4));
		}
		// marr中，找到中位数
		// marr(0, marr.len - 1,  mArr.length / 2 )
		return bfprt(mArr, 0, mArr.length - 1, mArr.length / 2);
	}
	// 获取数组中L,R范围内的中位数: 先排序，返回上中位数
	public static int getMedian(int[] arr, int L, int R) {
		insertionSort(arr, L, R);
		return arr[(L + R) / 2];
	}

	public static void insertionSort(int[] arr, int L, int R) {
		for (int i = L + 1; i <= R; i++) {
			for (int j = i - 1; j >= L && arr[j] > arr[j + 1]; j--) {
				swap(arr, j, j + 1);
			}
		}
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) (Math.random() * maxSize) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	public static void main(String[] args) {
		int testTime = 1000000;
		int maxSize = 100;
		int maxValue = 100;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			int k = (int) (Math.random() * arr.length) + 1;
			int ans1 = minKth1(arr, k);
			int ans2 = minKth2(arr, k);
			int ans3 = minKth3(arr, k);
			if (ans1 != ans2 || ans2 != ans3) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");

		/*int[] arr = new int[]{1,2,3,4,5};
		int i = medianOfMedians(arr, 0, 4);
		System.out.println(i);*/
	}

	private static int[] partision1(int[] arr, int L, int R, int porite){
		if(L==R){
			return new int[]{L,L};
		}
		int cur = L;
		int less = L -1;
		int more = R+1;
		while (cur<more){
			if(arr[cur] < porite){
				swap(arr, cur++, ++less);
			} else if(arr[cur] > porite){
				swap(arr, cur, --more);
			} else {
				cur ++;
			}
		}

		return new int[] {less+1, more -1};
	}
}
