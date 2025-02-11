package class20_单调栈;

import java.util.Stack;

/**
 * 给定一个只包含正数的数组arr，arr中任何一个子数组sub，一定可以算出(sub累加和)*(sub中的最小值)是什么
 * 那么所有子数组中，这个值最大是多少
 *
 * 正数数组，不存在范围大累加和小的情况
 *
 * -- 以x为最小值的子数组，求sum*x最大是多少
 */
public class Code02_AllTimesMinToMax {

	/**
	 * 枚举每个子数组
	 * @param arr
	 * @return
	 */
	public static int max1(int[] arr) {
		int max = Integer.MIN_VALUE;
		// i开头，j结尾的子数组
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++) {
				int minNum = Integer.MAX_VALUE;
				int sum = 0;
				//[i,j]中求累加和以及最小值
				for (int k = i; k <= j; k++) {
					sum += arr[k];
					minNum = Math.min(minNum, arr[k]);
				}
				// 抓取最大结果
				max = Math.max(max, minNum * sum);
			}
		}
		return max;
	}

	/**
	 * 可能有重复值，但当作无重复值来处理，不会影响计算结果
	 *
	 * @param arr
	 * @return
	 */
	public static int max2(int[] arr) {
		int size = arr.length;
		int[] sums = new int[size];
		sums[0] = arr[0];
		for (int i = 1; i < size; i++) {
			sums[i] = sums[i - 1] + arr[i];
		}
		int max = Integer.MIN_VALUE;
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < size; i++) {
			while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
				int j = stack.pop();
				max = Math.max(max, (stack.isEmpty() ? sums[i - 1] : (sums[i - 1] - sums[stack.peek()])) * arr[j]);
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int j = stack.pop();
			max = Math.max(max, (stack.isEmpty() ? sums[size - 1] : (sums[size - 1] - sums[stack.peek()])) * arr[j]);
		}
		return max;
	}

	public static int[] gerenareRondomArray() {
		int[] arr = new int[(int) (Math.random() * 20) + 10];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * 101);
		}
		return arr;
	}

	public static void main(String[] args) {
		int testTimes = 2000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			int[] arr = gerenareRondomArray();
			if (max1(arr) != max2(arr)) {
				System.out.println("FUCK!");
				break;
			}
		}
		System.out.println("test finish");
	}

}
