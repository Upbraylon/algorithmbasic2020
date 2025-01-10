package class22;

public class Code01_Manacher {

	public static int manacher(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		// "12132" -> "#1#2#1#3#2#"
		char[] str = manacherString(s);
		// 回文半径的大小
		// 回文半径数组的最大值除2就是最终的答案
		int[] pArr = new int[str.length];
		// 当前最长半径的中心
		int C = -1;
		// 讲述中：R代表最右的扩成功的位置
		// coding：最右的扩成功位置的，再下一个位置
		// 最长回文半径
		int R = -1;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < str.length; i++) { // 0 1 2
			// R第一个违规的位置，i>= R
			// i位置扩出来的答案，i位置扩的区域，至少是多大。
			// 2 * C - i 就是i'
			// i在R内，三种情况
			// pArr[2 * C - i] < R-i ---> pArr[2 * C - i]
			// pArr[2 * C - i] > R-i ---> R-i
			// pArr[2 * C - i] == R-i ---> 至少 R-i

			// i在R外，i的回文半径至少是1（i本身）
			// 这是不用比较的，可以直接得结论的
			// R是第一个违规的位置，R>i,i在R内部；R==i则是在外部
			// Math.min(pArr[2 * C - i], R - i) 不用验证的区域
			pArr[i] = R > i ? Math.min(pArr[2 * C - i], R - i) : 1;

			// 以i为中心向两边扩散，不能越界
			// i + pArr[i]和i - pArr[i]是下一个需要比较的位置
			// 比如5为C，最长回文半径为2，那么现在就是（4，5，6）这三个位置构成回文，下一个待比较的位置是3和7，即5+2和5-2
			// 如果是不需要验证的情况，也就是进一次while，判断一次就break，省代码
			while (i + pArr[i] < str.length && i - pArr[i] > -1) {
				// 数组随机读取，不用回退
				if (str[i + pArr[i]] == str[i - pArr[i]]) {
					pArr[i]++;
				} else {
					break;
				}
			}
			// 是否更新R和C
			if (i + pArr[i] > R) {
				R = i + pArr[i];
				C = i;
			}
			max = Math.max(max, pArr[i]);
		}
		// 原始串 121
		// 处理串 #1#2#1#
		// pArr中存储的是半径，最大值max为4，所求答案为121的长度3， max-1即可
		// 偶回文也一样，2222 --> #2#2#2#2#， 半径max=5，最大回文长度4，5-1=4
		return max - 1;
	}

	// 奇数 1 2 1 ---  #1#2#1# 7
	// 偶数 1 2 2 1 -- #1#2#2#1# 9
	public static char[] manacherString(String str) {
		char[] charArr = str.toCharArray();
		char[] res = new char[str.length() * 2 + 1];
		int index = 0;
		for (int i = 0; i != res.length; i++) {
			// 偶数# 奇数原值
			res[i] = (i & 1) == 0 ? '#' : charArr[index++];
		}
		return res;
	}

	// for test
	public static int right(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = manacherString(s);
		int max = 0;
		for (int i = 0; i < str.length; i++) {
			int L = i - 1;
			int R = i + 1;
			while (L >= 0 && R < str.length && str[L] == str[R]) {
				L--;
				R++;
			}
			max = Math.max(max, R - L - 1);
		}
		return max / 2;
	}

	// for test
	// Math.random() 0.0 < Math.random() < 1.0
	// Math.random()*10  0.0 < Math.random()*10 < 10
	// (int)Math.random()*10 0 1 2 3 4 5 6 7 8 9
	// 若要1--10，需要(int) (Math.random() * 10) + 1

	public static String getRandomString(int possibilities, int size) {
		char[] ans = new char[(int) (Math.random() * size) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (char) ((int) (Math.random() * possibilities) + 'a');
		}
		return String.valueOf(ans);
	}

	public static void main(String[] args) {
		int possibilities = 5;
		int strSize = 20;
		int testTimes = 5000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			String str = getRandomString(possibilities, strSize);
			if (manacher(str) != right(str)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
