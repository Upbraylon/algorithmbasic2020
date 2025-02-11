package class17_动态规划3;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * arr是货币数组，其中的值都是正数，再给定一个正数aim。
 * 每个值都认为是一张货币，认为值相同的货币没有任何不同，返回组成aim的方法数。
 * 例如：arr = {1,2,1,1,2,1,2}, aim = 4
 * 方法：1+1+1+1 1+1+2 2+2
 * 一共就3种方法，所以返回3
 */
public class Code04_CoinsWaySameValueSamePapper {


	public static int coinsWayyyy(int[] arr, int aim) {
		// arr需要整理成面值和张数
		if(arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		Map<Integer, Integer> coins = new HashMap<>(arr.length);
		for (int j : arr) {
			if (!coins.containsKey(j)) {
				coins.put(j, 0);
			}
			coins.put(j, coins.get(j) + 1);
		}

		int[] coin = new int[coins.keySet().size()];
		int[] count = new int[coin.length];
		int i=0;
		for (Entry<Integer, Integer> entry : coins.entrySet()) {
			coin[i] = entry.getKey();
			count[i] = entry.getValue();
			i++;
		}
		return processs(coin, count, 0, aim);
	}
	// 当前尝试第index，剩余rest，返回方法数
	private static int processs(int[] coins, int[] nums, int index, int rest) {
		if(index == coins.length) {
			return rest == 0 ? 1 : 0;
		}
		if(rest == 0) {
			return 1;
		}
		int count = nums[index];
		int value = coins[index];
		int ans = 0;
		for (int i = 0; i <= count && i * value <= rest; i++) {
			ans += processs(coins, nums, index + 1, rest-i*value);
		}
		return ans;
	}

	public static int coinsWayyyyy(int[] arr, int aim) {
		// arr需要整理成面值和张数
		if(arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		Map<Integer, Integer> coins = new HashMap<>(arr.length);
		for (int j : arr) {
			if (!coins.containsKey(j)) {
				coins.put(j, 0);
			}
			coins.put(j, coins.get(j) + 1);
		}

		int[] coin = new int[coins.keySet().size()];
		int[] count = new int[coin.length];
		int i=0;
		for (Entry<Integer, Integer> entry : coins.entrySet()) {
			coin[i] = entry.getKey();
			count[i] = entry.getValue();
			i++;
		}
		int N = coin.length;
		int[][] dp = new int[N+1][aim+1];
		for (int j = 0; j <= N; j++) {
			dp[j][0] = 1;
		}
		for (int index = N-1; index >= 0; index--) {
			for (int rest = 1; rest <= aim; rest++) {
				dp[index][rest] = dp[index+1][rest];
				if(rest - coin[index] >= 0) {
					dp[index][rest] += dp[index][rest - coin[index]];
				}
				if(rest- (count[index]+1) * coin[index] >=0) {
					dp[index][rest] -= dp[index+1][rest- (count[index]+1) * coin[index]];
				}
			}
		}
		return dp[0][aim];
	}








	public static class Info {
		public int[] coins;
		public int[] zhangs;

		public Info(int[] c, int[] z) {
			coins = c;
			zhangs = z;
		}
	}

	public static Info getInfo(int[] arr) {
		HashMap<Integer, Integer> counts = new HashMap<>();
		for (int value : arr) {
			if (!counts.containsKey(value)) {
				counts.put(value, 1);
			} else {
				counts.put(value, counts.get(value) + 1);
			}
		}
		int N = counts.size();
		int[] coins = new int[N];
		int[] zhangs = new int[N];
		int index = 0;
		for (Entry<Integer, Integer> entry : counts.entrySet()) {
			coins[index] = entry.getKey();
			zhangs[index++] = entry.getValue();
		}
		return new Info(coins, zhangs);
	}

	public static int coinsWay(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		Info info = getInfo(arr);
		return processs(info.coins, info.zhangs, 0, aim);
	}

	// coins 面值数组，正数且去重
	// zhangs 每种面值对应的张数
	public static int process(int[] coins, int[] zhangs, int index, int rest) {
		if (index == coins.length) {
			return rest == 0 ? 1 : 0;
		}
		int ways = 0;
		for (int zhang = 0; zhang * coins[index] <= rest && zhang <= zhangs[index]; zhang++) {
			ways += process(coins, zhangs, index + 1, rest - (zhang * coins[index]));
		}
		return ways;
	}

	public static int dp1(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		Info info = getInfo(arr);
		int[] coins = info.coins;
		int[] zhangs = info.zhangs;
		int N = coins.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 1;
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				int ways = 0;
				for (int zhang = 0; zhang * coins[index] <= rest && zhang <= zhangs[index]; zhang++) {
					ways += dp[index + 1][rest - (zhang * coins[index])];
				}
				dp[index][rest] = ways;
			}
		}
		return dp[0][aim];
	}

	public static int dp2(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		Info info = getInfo(arr);
		int[] coins = info.coins;
		int[] zhangs = info.zhangs;
		int N = coins.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 1;
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				dp[index][rest] = dp[index + 1][rest];
				if (rest - coins[index] >= 0) {
					dp[index][rest] += dp[index][rest - coins[index]];
				}
				if (rest - coins[index] * (zhangs[index] + 1) >= 0) {
					dp[index][rest] -= dp[index + 1][rest - coins[index] * (zhangs[index] + 1)];
				}
			}
		}
		return dp[0][aim];
	}

	// 为了测试
	public static int[] randomArray(int maxLen, int maxValue) {
		int N = (int) (Math.random() * maxLen);
		int[] arr = new int[N];
		for (int i = 0; i < N; i++) {
			arr[i] = (int) (Math.random() * maxValue) + 1;
		}
		return arr;
	}

	// 为了测试
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// 为了测试
	public static void main(String[] args) {
		int maxLen = 10;
		int maxValue = 20;
		int testTime = 1000000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = randomArray(maxLen, maxValue);
			int aim = (int) (Math.random() * maxValue);
			int ans1 = coinsWayyyy(arr, aim);
			int ans2 = dp1(arr, aim);
			int ans3 = coinsWayyyyy(arr, aim);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println(aim);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println(ans3);
				break;
			}
		}
		System.out.println("测试结束");
	}

}
