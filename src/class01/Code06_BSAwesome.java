package class01;

/**
 * 找局部最小
 *
 * 无序数组：正负值都有，任意两个相邻的数不相等
 * 局部最小：0位置的数小于1位置的数，0位置的数布局最小
 * 		n-1位置的数小于n-2位置的数，n-1位置的数局部最小
 * 	 	arr[i-1]>arr[i] && arr[i]<arr[i+1], arr[i]局部最小
 */
public class Code06_BSAwesome {

	public static int getLessIndex(int[] arr) {
		if (arr == null || arr.length == 0) {
			return -1; // no exist
		}
		if (arr.length == 1 || arr[0] < arr[1]) {
			return 0;
		}
		if (arr[arr.length - 1] < arr[arr.length - 2]) {
			return arr.length - 1;
		}
		int left = 1;
		int right = arr.length - 2;
		int mid = 0;
		while (left < right) {
			mid = (left + right) / 2;
			if (arr[mid] > arr[mid - 1]) {
				right = mid - 1;
			} else if (arr[mid] > arr[mid + 1]) {
				left = mid + 1;
			} else {
				return mid;
			}
		}
		return left;
	}

}
