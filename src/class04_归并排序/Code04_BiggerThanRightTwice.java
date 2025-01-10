package class04_归并排序;

/**
 * 给定无序数组arr，针对arr中的每个数，存在右边的数乘以2之后依然严格小于num，求arr中每个数的满足这样条件的右侧数的个数之和。
 * res += arr[p1] > arr[p2]*2 ? M+1-p1 : 0;
 */
public class Code04_BiggerThanRightTwice {

	public static int biggerTwice(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		return process(arr, 0, arr.length - 1);
	}

	public static int process(int[] arr, int l, int r) {
		if (l == r) {
			return 0;
		}
		// l < r
		int mid = l + ((r - l) >> 1);
		return process(arr, l, mid) + process(arr, mid + 1, r) + merge1(arr, l, mid, r);
	}

	public static int merge(int[] arr, int L, int m, int r) {
		// [L....M]   [M+1....R]
		
		int ans = 0;
		// 目前囊括进来的数，是从[M+1, windowR)
		int windowR = m + 1;
		// 因为有序，才能指针不回退的进行计算，所有还需要merge
		// arr[i] > (arr[windowR] * 2)， 因为有序，arr[i]也必定大于windowR之前到m+1的所有数arr[windowR] * 2

		// 针对左组的每个数，考察右组有多少符合
		for (int i = L; i <= m; i++) {
			// 右组的数符合条件，右组前进
			while (windowR <= r && arr[i] > (arr[windowR] * 2)) {
				windowR++;
			}
			// 结算当前左组的数
			// windowR满足当前的i，那么windowR肯定满足i之后的数，这样可以做到指针不回退
			ans += windowR - m - 1;
		}
		
		
		int[] help = new int[r - L + 1];
		int i = 0;
		int p1 = L;
		int p2 = m + 1;
		while (p1 <= m && p2 <= r) {
			help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
		}
		while (p1 <= m) {
			help[i++] = arr[p1++];
		}
		while (p2 <= r) {
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[L + i] = help[i];
		}
		return ans;
	}

	// [1 3 4 6] [1 2 2 4 5]
	// 左组不符合，左组前进；
	// 符合，结算当前右组的数，右组前进
	// 整体指针不回退
	public static int merge1(int[] arr, int L, int m, int r) {
		// [L....M]   [M+1....R]

		int ans = 0;
		int windowL = L;
		// 针对每个右组的数，看左组有多少个符合：
		/*for (int i=m+1; i<=r;i++){
			// 若左组windowL位置的不符合，那么针对当前windowL，右组当前数i之后的肯定也不符合，这样左组的windowL可以前进
			while (windowL<=m && arr[windowL]<=arr[i]*2){
					windowL++;
			}
			// 左组windowL位置的符合，那么windowL之后m+1之前的肯定都符合，直接计算
			ans += m+1-windowL;
		}*/
		int p1 = L;
		int p2 = m + 1;
		while (p1 <= m && p2 <= r) {

			if(arr[p1] > arr[p2]*2) {
				ans += m+1-p1;
				p2++;
			} else {
				p1++;
			}

		}

		int[] help = new int[r - L + 1];
		int i = 0;
		 p1 = L;
		  p2 = m + 1;
		while (p1 <= m && p2 <= r) {
			help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
		}
		while (p1 <= m) {
			help[i++] = arr[p1++];
		}
		while (p2 <= r) {
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[L + i] = help[i];
		}
		return ans;
	}

	// [1 3 4 6] [1 2 2]
	// 这里不能同时merge同时累加，因为累加的条件和排序条件不一样，排序和累加的条件要一致才可以
	public static int merge2(int[] arr, int L, int m, int r) {
		// [L....M]   [M+1....R]

		int ans = 0;
		int[] help = new int[r - L + 1];
		int i = 0;
		int p1 = L;
		int p2 = m + 1;

		// 下面这种方式排序正确，但是拷贝的index顺序错误
	/*	while (p1 <= m && p2 <= r) {
			ans += arr[p1] > arr[p2]*2 ? (m-p1+1) : 0;
			help[i++] = arr[p1] > arr[p2] ? arr[p2++] : arr[p1++];
		}*/

		// 下面这种拷贝顺序一致，但是排序错误
		while (p1 <= m && p2 <= r) {
			ans += arr[p1] > arr[p2]*2 ? (m-p1+1) : 0;
			// [1 3 4 6] [1 2 2]
			// 下面这句相当于给右组的二倍排序了，即：[1 3 4 6] [2 4 4]
			help[i++] = arr[p1] > arr[p2]*2 ? arr[p2++] : arr[p1++];
		}

		while (p1 <= m) {
			help[i++] = arr[p1++];
		}
		while (p2 <= r) {
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[L + i] = help[i];
		}
		return ans;
	}

	// for test
	public static int comparator(int[] arr) {
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i] > (arr[j] << 1)) {
					ans++;
				}
			}
		}
		return ans;
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) ((maxValue + 1) * Math.random());
		}
		return arr;
	}

	// for test
	public static int[] copyArray(int[] arr) {
		if (arr == null) {
			return null;
		}
		int[] res = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			res[i] = arr[i];
		}
		return res;
	}

	// for test
	public static boolean isEqual(int[] arr1, int[] arr2) {
		if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
			return false;
		}
		if (arr1 == null && arr2 == null) {
			return true;
		}
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}

	// for test
	public static void printArray(int[] arr) {
		if (arr == null) {
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// for test
	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 10;
		int maxValue = 10;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			if (biggerTwice(arr1) != comparator(arr2)) {
				System.out.println("Oops!");
				printArray(arr1);
				printArray(arr2);
				break;
			}
		}
		System.out.println("测试结束");
	}

}
