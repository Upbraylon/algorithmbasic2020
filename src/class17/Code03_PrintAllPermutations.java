package class17;

import java.util.ArrayList;
import java.util.List;

/**
 * 递归的技巧：清除现场 恢复现场
 *
 * 打印一个字符串的全部排列
 * 打印一个字符串的全部排列，要求不要出现重复的排列
 *
 * 全排列：所有的字符必须都参与，只是顺序不同
 */
public class Code03_PrintAllPermutations {

	public static List<String> permutation1(String s) {
		List<String> ans = new ArrayList<>();
		if (s == null || s.length() == 0) {
			return ans;
		}
		char[] str = s.toCharArray();
		// 剩下的字符
		ArrayList<Character> rest = new ArrayList<Character>();
		for (char cha : str) {
			rest.add(cha);
		}
		String path = "";
		f(rest, path, ans);
		return ans;
	}

	/**
	 * 递归函数
	 * @param rest 剩下的字符
	 * @param path 之前做过的决定
	 * @param ans 结果
	 */
	public static void f(ArrayList<Character> rest, String path, List<String> ans) {
		if (rest.isEmpty()) {
			ans.add(path);
			return;
		}

		for (int i = 0; i < rest.size(); i++) {
			char cur = rest.get(i);
			rest.remove(i);
			f(rest, path + cur, ans);
			// 恢复现场：供下次选择
			rest.add(i, cur);
		}
	}

	public static List<String> permutation2(String s) {
		List<String> ans = new ArrayList<>();
		if (s == null || s.length() == 0) {
			return ans;
		}
		char[] str = s.toCharArray();
		g1(str, 0, ans);
		return ans;
	}

	/**
	 *
	 * @param str
	 * @param index
	 * @param ans
	 */
	public static void g1(char[] str, int index, List<String> ans) {
		if (index == str.length) {
			ans.add(String.valueOf(str));
			return;
		}
		for (int i = index; i < str.length; i++) {
			// index位置的值和i换
			swap(str, index, i);
			g1(str, index + 1, ans);
			// 返回之前换回来
			swap(str, index, i);

		}
	}

	/**
	 * 全排列去重
	 *
	 * 使用set是过滤思想，所有的情况都跑完再过滤
	 * 下面的方法使用的是剪枝，发现情况没必要做，就不再计算
	 * 剪枝优于过滤
	 * @param s
	 * @return
	 */
	public static List<String> permutation3(String s) {
		List<String> ans = new ArrayList<>();
		if (s == null || s.length() == 0) {
			return ans;
		}
		char[] str = s.toCharArray();
		g2(str, 0, ans);
		return ans;
	}

	public static void g2(char[] str, int index, List<String> ans) {
		if (index == str.length) {
			ans.add(String.valueOf(str));
		} else {
			// ascii码
			// visited局部范围有效，只对当前index有效，下一个index会清空重新标记
			boolean[] visited = new boolean[256];
			for (int i = index; i < str.length; i++) {
				// index和i位置的字符交换，只有i位置的字符不曾试过，才交换过来考虑
				if (!visited[str[i]]) {
					visited[str[i]] = true;
					swap(str, index, i);
					g2(str, index + 1, ans);
					swap(str, index, i);
				}
			}
		}
	}

	public static void swap(char[] chs, int i, int j) {
		char tmp = chs[i];
		chs[i] = chs[j];
		chs[j] = tmp;
	}

	public static void main(String[] args) {
		String s = "acc";
		List<String> ans1 = permutation1(s);
		for (String str : ans1) {
			System.out.println(str);
		}
		System.out.println("=======");
		List<String> ans2 = permutation2(s);
		for (String str : ans2) {
			System.out.println(str);
		}
		System.out.println("=======");
		List<String> ans3 = permutation3(s);
		for (String str : ans3) {
			System.out.println(str);
		}

	}

}
