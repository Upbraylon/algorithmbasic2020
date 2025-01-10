package class14;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 递归--当作黑盒，输入什么，达到什么目的，basecase，如何用
 * 不要太痴迷细节
 *
 * 打印一个字符串的全部子序列
 * 打印一个字符串的全部子序列，要求不要出现重复字面值的子序列
 *
 * 子序列：可以不连续
 * 1234 : 1; 13; 14; 2; 23; 24......
 * 思想：每个位置的数要或不要 两种选择，全部展开即是答案
 */
public class Code02_PrintAllSubsquences {

	// s -> "abc" ->
	public static List<String> subs(String s) {
		char[] str = s.toCharArray();
		String path = "";
		List<String> ans = new ArrayList<>();
		process1(str, 0, ans, path);
		return ans;
	}

	// str 固定参数
	// 来到了str[index]字符，index是位置
	// str[0..index-1]已经走过了！之前的决定，都在path上
	// 之前的决定已经不能改变了，就是path（要了哪些没有要哪些）
	// str[index....]还能决定，之前已经确定，而后面还能自由选择的话，
	// 把所有生成的子序列，放入到ans里去
	public static void process1(char[] str, int index, List<String> ans, String path) {
		//index到终止位置
		if (index == str.length) {
			ans.add(path);
			return;
		}
		// 没有要index位置的字符
		process1(str, index + 1, ans, path);
		// 要了index位置的字符
		process1(str, index + 1, ans, path + String.valueOf(str[index]));
	}

	public static List<String> subsNoRepeat(String s) {
		char[] str = s.toCharArray();
		String path = "";
		// 收集的结构变成set即可
		HashSet<String> set = new HashSet<>();
		process2(str, 0, set, path);
		List<String> ans = new ArrayList<>();
		for (String cur : set) {
			ans.add(cur);
		}
		return ans;
	}

	public static void process2(char[] str, int index, HashSet<String> set, String path) {
		if (index == str.length) {
			set.add(path);
			return;
		}
		process2(str, index + 1, set, path);

		process2(str, index + 1, set, path + String.valueOf(str[index]));
	}

	public static void main(String[] args) {
		String test = "acccc";
		List<String> ans1 = subs(test);
		List<String> ans2 = subsNoRepeat(test);
		List<String> ans3 = subs1(test);

		for (String str : ans1) {
			System.out.println(str);
		}
		System.out.println("=================");
		for (String str : ans2) {
			System.out.println(str);
		}
		System.out.println("=================");
		for (String str : ans3){
			System.out.println(str);
		}
	}

	public static List<String> subs1(String str){
		char[] chars = str.toCharArray();
		String path = "";
		List<String> ans = new ArrayList<>();
		process3(chars, 0, ans, path);
		return ans;
	}

	private static void process3(char[] chars, int index, List<String> ans, String path) {
		if(index == chars.length){
			ans.add(path);
			return;
		}
		process3(chars, index+1, ans, path);
		process3(chars, index +1, ans, path+String.valueOf(chars[index]));
	}


}
