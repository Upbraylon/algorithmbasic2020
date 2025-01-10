package class15;

/**
 * 背包问题
 *
 * w 重量数组
 * V 价值数组
 * W V都是大于等于0
 * bag 表示背包能装多少货物
 * 不超重的情况下，求能够得到的最大价值
 *
 * 从左往右的尝试模型
 * 每种货物要与不要全部展开---暴力递归
 */
public class Code03_Knapsack {

	// 所有的货，重量和价值，都在w和v数组里
	// 为了方便，其中没有负数
	// bag背包容量，不能超过这个载重
	// 返回：不超重的情况下，能够得到的最大价值
	public static int maxValue(int[] w, int[] v, int bag) {
		if (w == null || v == null || w.length != v.length || w.length == 0) {
			return 0;
		}
		// 尝试函数！
		return process(w, v, 0, bag);
	}

	/**
	 * 当前考虑到了index货物，index.....所有的货物可以自由选择
	 * 做的选择不能超过背包容量
	 * 返回最大价值
	 *
	 * @param w
	 * @param v
	 * @param index 0~N
	 * @param rest 负~bag
	 * @return
	 */
	public static int process(int[] w, int[] v, int index, int rest) {
		// 如果baseCase是这样，那么当背包容量为负，上面不知道这个选择无效
		/*if (rest < 0) {
			return 0;
		}*/
		// 背包没有容量，背包容量为0不能停，有可能后面有重量为0的货物
		// 设置无效解技巧
		if (rest < 0) {
			return -1;
		}
		// 没有货物
		if (index == w.length) {
			return 0;
		}
		// 没有要当前货物
		int p1 = process(w, v, index + 1, rest);
		int p2 = 0;
		// 要当前货
		int next = process(w, v, index + 1, rest - w[index]);
		// 后面不为-1.说明有效
		if (next != -1) {
			// 加本货物的价值
			p2 = v[index] + next;
		}
		return Math.max(p1, p2);
	}

	public static int dp(int[] w, int[] v, int bag) {
		if (w == null || v == null || w.length != v.length || w.length == 0) {
			return 0;
		}
		int N = w.length;
		// index 0--N
		// bag 负--bag
		int[][] dp = new int[N + 1][bag + 1];
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= bag; rest++) {
				int p1 = dp[index + 1][rest];
				int p2 = 0;
				// 小于0无效解，否则表中拿
				int next = rest - w[index] < 0 ? -1 : dp[index + 1][rest - w[index]];
				if (next != -1) {
					p2 = v[index] + next;
				}
				dp[index][rest] = Math.max(p1, p2);
			}
		}
		return dp[0][bag];
	}

	public static int cache(int[] w, int[] v, int bag){
		if(w==null || v==null || w.length!=v.length || w.length==0){
			return 0;
		}
		int N = w.length;
		int[][] help = new int[N+1][bag+1];
		for (int i=0; i<N+1;i++){
			for (int j=0; j<bag+1;j++){
				help[i][j] = -1;
			}
		}
		return process1(w, v, 0, bag, help);
	}

	private static int process1(int[] w, int[] v, int index, int bag, int[][] help) {
		if(bag<0){
			return -1;
		}
		if(help[index][bag] != -1){
			return help[index][bag];
		}

		if(index == w.length){
			help[index][bag] = 0;
			return help[index][bag];
		}

		int p1 = process1(w,v,index+1, bag, help);

		int p2=0;
		p2 = process1(w, v, index +1, bag-w[index], help);
		if(p2!= -1){
			p2+=v[index];
		}

		help[index][bag] = Math.max(p1, p2);
		return help[index][bag];
	}

	public static void main(String[] args) {
		int[] weights = { 3, 2, 4, 7, 3, 1, 7 };
		int[] values = { 5, 6, 3, 19, 12, 4, 2 };
		int bag = 14;
		System.out.println(maxValue(weights, values, bag));
		System.out.println(dp(weights, values, bag));
		System.out.println(cache(weights, values, bag));
	}

}
