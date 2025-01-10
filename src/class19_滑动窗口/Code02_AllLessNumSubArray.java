package class19_滑动窗口;

import java.util.LinkedList;

/**
 * 给定一个整型数组arr和一个整数num，某个arr中的子数组sub，如果想达标，必须满足：
 * sub中的最大值-sub中的最小值<=num, 返回arr中达标子数组的数量
 */
public class Code02_AllLessNumSubArray {

	// 暴力的对数器方法
	// O(N^3)
	public static int right(int[] arr, int sum) {
		if (arr == null || arr.length == 0 || sum < 0) {
			return 0;
		}
		int N = arr.length;
		int count = 0;
		for (int L = 0; L < N; L++) {
			for (int R = L; R < N; R++) {
				int max = arr[L];
				int min = arr[L];
				for (int i = L + 1; i <= R; i++) {
					max = Math.max(max, arr[i]);
					min = Math.min(min, arr[i]);
				}
				if (max - min <= sum) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * O(N)
	 *
	 * 结论1
	 * 假设某个范围为(L,R)的子数组达标：max - min <= sum，那么其内部所有的子数组都达标
	 * 因为最大值只能变小，最小值只能变大
	 *
	 * 结论2
	 * 如果某个范围为(L,R)的子数组不达标，那么L往左扩的子数组不达标，R往右扩的子数组也不达标
	 * 因为子数组扩大，最小值只能变小，最大值只能变大
	 *
	 * 从0开始，R一直往右扩，直到不达标，此时结算以0开头的子数组；
	 * L右扩，L来到1，R再往右扩直到不达标，此时结算以1开头的子数组；
	 * 如此这般，这般如此，得出所有的子数组。
	 *
	 * R不达标的时候结算L是利用结论1。
	 * 结算L到R中的所有以L开头的子数组是根据结论2。
	 *
	 * @param arr
	 * @param sum
	 * @return
	 */
	public static int num(int[] arr, int sum) {
		if (arr == null || arr.length == 0 || sum < 0) {
			return 0;
		}
		int N = arr.length;
		int count = 0;
		// 窗口内最大值的更新结构
		LinkedList<Integer> maxWindow = new LinkedList<>();
		// 窗口内最小值的更新结构
		LinkedList<Integer> minWindow = new LinkedList<>();
		int R = 0;
		for (int L = 0; L < N; L++) {
			// [L, R)（R初次不达标）
			while (R < N) {
				while (!maxWindow.isEmpty() && arr[maxWindow.peekLast()] <= arr[R]) {
					maxWindow.pollLast();
				}
				maxWindow.addLast(R);
				while (!minWindow.isEmpty() && arr[minWindow.peekLast()] >= arr[R]) {
					minWindow.pollLast();
				}
				minWindow.addLast(R);
				if (arr[maxWindow.peekFirst()] - arr[minWindow.peekFirst()] > sum) {
					break;
				} else {
					R++;
				}
			}
			// R不达标的时候，以L开头的子数组有R-L个达标
			count += R - L;
			// 窗口内最大值是否即将过期
			if (maxWindow.peekFirst() == L) {
				maxWindow.pollFirst();
			}
			// 窗口内最小值是否即将过期
			if (minWindow.peekFirst() == L) {
				minWindow.pollFirst();
			}
		}
		return count;
	}

	// for test
	public static int[] generateRandomArray(int maxLen, int maxValue) {
		int len = (int) (Math.random() * (maxLen + 1));
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	// for test
	public static void printArray(int[] arr) {
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				System.out.print(arr[i] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int maxLen = 100;
		int maxValue = 200;
		int testTime = 100000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxLen, maxValue);
			int sum = (int) (Math.random() * (maxValue + 1));
			int ans1 = right(arr, sum);
			int ans2 = num(arr, sum);
			if (ans1 != ans2) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println(sum);
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("测试结束");

	}

}
