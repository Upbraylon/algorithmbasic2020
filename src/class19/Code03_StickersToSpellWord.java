package class19;

import java.util.HashMap;

// 本题测试链接：https://leetcode.com/problems/stickers-to-spell-word

/**
 * 地位非常重要
 * 没必要做严格表依赖
 * 可变参数类型是字符串，不同于整型，无法确定表的大小，可能性巨多
 */
public class Code03_StickersToSpellWord {

	public static int minStickers1(String[] stickers, String target) {
		int ans = process1(stickers, target);
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	/**
	 * @param stickers 所有贴纸stickers，每一种贴纸都有无穷张
	 * @param target
	 * @return 最少张数
	 */
	public static int process1(String[] stickers, String target) {
		if (target.length() == 0) {
			// 之前的已经解决了，还需要零张
			return 0;
		}
		// 搞不定的时候返回系统最大值
		int min = Integer.MAX_VALUE;
		// 每一张贴纸都作为第一张去试
		for (String first : stickers) {
			// first能消掉target中的哪些字符，剩下的是rest，还需要继续去试
			String rest = minus(target, first);
			// first作为第一张有效，再继续试下面的，否则换第一张重试
			if (rest.length() != target.length()) {
				min = Math.min(min, process1(stickers, rest));
			}
		}
		// 后续没有有效解，我也该返回系统最大值
		// 后续是系统最大，返回系统最大加0
		// 否则返回 min+1,1是first
		return min + (min == Integer.MAX_VALUE ? 0 : 1);
	}

	/**
	 * S1中消掉S2中出现的字符
	 *
	 * S1 aabc  S2 aek
	 * 结果 abc
	 *
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String minus(String s1, String s2) {
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		int[] count = new int[26];
		for (char cha : str1) {
			count[cha - 'a']++;
		}
		//[1 1 1 0 -1 0 0 0 -1]
		for (char cha : str2) {
			count[cha - 'a']--;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 26; i++) {
			if (count[i] > 0) {
				for (int j = 0; j < count[i]; j++) {
					builder.append((char) (i + 'a'));
				}
			}
		}
		return builder.toString();
	}

	public static int minStickers2(String[] stickers, String target) {
		int N = stickers.length;
		// 关键优化(用词频表替代贴纸数组)
		// 词频统计表
		int[][] counts = new int[N][26];
		for (int i = 0; i < N; i++) {
			char[] str = stickers[i].toCharArray();
			for (char cha : str) {
				counts[i][cha - 'a']++;
			}
		}
		int ans = process2(counts, target);
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	/**
	 * stickers[i] 数组，当初i号贴纸的字符统计 int[][] stickers -> 所有的贴纸
	 * 每一种贴纸都有无穷张
	 * 返回搞定target的最少张数
	 *
	 * @param stickers 二维数组代替所有的贴纸，值是每个字符的词频统计
	 * @param t
	 * @return 最少张数
	 */
	public static int process2(int[][] stickers, String t) {
		if (t.length() == 0) {
			return 0;
		}
		// target做出词频统计
		// target  aabbc  2 2 1..
		//                0 1 2..
		char[] target = t.toCharArray();
		int[] tcounts = new int[26];
		for (char cha : target) {
			tcounts[cha - 'a']++;
		}
		int N = stickers.length;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < N; i++) {
			// 尝试第一张贴纸是谁
			int[] sticker = stickers[i];
			// 最关键的优化(重要的剪枝!这一步也是贪心!)
			// 所有贴纸中含有target第一个字符的才去跑分支
			// 后续总有消掉 第一个字母的时候，让这个时候先到来
			// 不剪枝的时候，最优答案呈现好几次，剪枝之后一次就能抓到，其他的就不用走了
			if (sticker[target[0] - 'a'] > 0) {
				StringBuilder builder = new StringBuilder();
				for (int j = 0; j < 26; j++) {
					if (tcounts[j] > 0) {
						int nums = tcounts[j] - sticker[j];
						for (int k = 0; k < nums; k++) {
							builder.append((char) (j + 'a'));
						}
					}
				}
				String rest = builder.toString();
				min = Math.min(min, process2(stickers, rest));
			}
		}
		return min + (min == Integer.MAX_VALUE ? 0 : 1);
	}

	public static int minStickers3(String[] stickers, String target) {
		int N = stickers.length;
		int[][] counts = new int[N][26];
		for (int i = 0; i < N; i++) {
			char[] str = stickers[i].toCharArray();
			for (char cha : str) {
				counts[i][cha - 'a']++;
			}
		}
		HashMap<String, Integer> dp = new HashMap<>();
		dp.put("", 0);
		int ans = process3(counts, target, dp);
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	public static int process3(int[][] stickers, String t, HashMap<String, Integer> dp) {
		if (dp.containsKey(t)) {
			return dp.get(t);
		}
		if(t.length()==0){
			return 0;
		}
		char[] target = t.toCharArray();
		int[] tcounts = new int[26];
		for (char cha : target) {
			tcounts[cha - 'a']++;
		}
		int N = stickers.length;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < N; i++) {
			int[] sticker = stickers[i];
			if (sticker[target[0] - 'a'] > 0) {
				StringBuilder builder = new StringBuilder();
				for (int j = 0; j < 26; j++) {
					if (tcounts[j] > 0) {
						int nums = tcounts[j] - sticker[j];
						for (int k = 0; k < nums; k++) {
							builder.append((char) (j + 'a'));
						}
					}
				}
				String rest = builder.toString();
				min = Math.min(min, process3(stickers, rest, dp));
			}
		}
		int ans = min + (min == Integer.MAX_VALUE ? 0 : 1);
		dp.put(t, ans);
		return ans;
	}

}
