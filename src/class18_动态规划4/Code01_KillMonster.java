package class18_动态规划4;

/**
 * 给定3个参数，N，M，K
 * 怪兽有N滴血，等着英雄来砍自己
 * 英雄每一次打击，都会让怪兽流失[0~M]的血量
 * 到底流失多少？每一次在[0~M]上等概率得获得一个值，求K次打击之后，英雄把怪兽砍死的概率
 */
public class Code01_KillMonster {

	public static double right(int N, int M, int K) {
		if (N < 1 || M < 1 || K < 1) {
			return 0;
		}
		// 第1次击打，流失的血量可能是0-M中任一种（M+1的可能性）；第二次也是，第三次...第K次，总共排列组合：(M+1)的K次
		long all = (long) Math.pow(M + 1, K);
		// 能杀死的可能数量
		long kill = process(K, M, N);
		return (double) kill / (double) all;
	}

	// 怪兽还剩hp点血
	// 每次的伤害在[0~M]范围上
	// 还有times次可以砍
	// 返回砍死的情况数！
	public static long process(int times, int M, int hp) {
		if (times == 0) {
			return hp <= 0 ? 1 : 0;
		}
		// 血量已经小于0，但还有次数可以砍，直接计算剩余次数的量级，因为必死
		if (hp <= 0) {
			return (long) Math.pow(M + 1, times);
		}
		long ways = 0;
		// 当前一刀，掉的血量从0--M都考虑一下，逐个尝试，结果累加
		for (int i = 0; i <= M; i++) {
			ways += process(times - 1, M, hp - i);
		}
		return ways;
	}

	public static double dp1(int N, int M, int K) {
		if (N < 1 || M < 1 || K < 1) {
			return 0;
		}
		long all = (long) Math.pow(M + 1, K);
		long[][] dp = new long[K + 1][N + 1];
		dp[0][0] = 1;
		for (int times = 1; times <= K; times++) {
			dp[times][0] = (long) Math.pow(M + 1, times);
			for (int hp = 1; hp <= N; hp++) {
				long ways = 0;
				for (int i = 0; i <= M; i++) {
					if (hp - i >= 0) {
						ways += dp[times - 1][hp - i];
					} else {
						// times-1 : hp - i 是抛除本次之后的，所以次数要减1
						ways += (long) Math.pow(M + 1, times - 1);
					}
				}
				dp[times][hp] = ways;
			}
		}
		long kill = dp[K][N];
		return (double) kill / (double) all;
	}
	public static double dp3(int N, int M, int K) {
		if (N < 1 || M < 1 || K < 1) {
			return 0;
		}
		long all = (long) Math.pow(M + 1, K);
		long[][] dp = new long[K + 1][N + 1];
		dp[0][0] = 1;
		for (int times = 1; times <= K; times++) {
			dp[times][0] = (long) Math.pow(M + 1, times);
			for (int hp = 1; hp <= N; hp++) {
				long ways = 0;
				if(hp-M < 1) {
					ways = (long) Math.pow(M+1, times-1);
				} else {
					ways = dp[times-1][hp-M-1];
				}
				dp[times][hp] = dp[times][hp-1] + dp[times-1][hp] - ways;
			}
		}
		long kill = dp[K][N];
		return (double) kill / (double) all;
	}

	public static double dp2(int N, int M, int K) {
		if (N < 1 || M < 1 || K < 1) {
			return 0;
		}
		long all = (long) Math.pow(M + 1, K);
		long[][] dp = new long[K + 1][N + 1];
		dp[0][0] = 1;
		for (int times = 1; times <= K; times++) {
			dp[times][0] = (long) Math.pow(M + 1, times);
			for (int hp = 1; hp <= N; hp++) {
				// 位置依赖，dp[times][hp - 1] + dp[times - 1][hp] 多加了1个元素（hp - 1 - M >= 0)，或多加了小于0的部分（hp - 1 - M < 0）
				dp[times][hp] = dp[times][hp - 1] + dp[times - 1][hp];
				// hp-1-M >= 0,说明多加的数在格子里：dp[times - 1][hp - 1 - M]
				if (hp - 1 - M >= 0) {
					dp[times][hp] -= dp[times - 1][hp - 1 - M];
				} else {
					//  hp-1-M< 0, 多加的数已经出去了，公式计算结果, 小于0的部分只和times有关，和hp已经无关了
					dp[times][hp] -= Math.pow(M + 1, times - 1);
				}
			}
		}
		long kill = dp[K][N];
		return (double) kill / (double) all;
	}

	public static void main(String[] args) {
		int NMax = 10;
		int MMax = 10;
		int KMax = 10;
		int testTime = 200;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int N = (int) (Math.random() * NMax);
			int M = (int) (Math.random() * MMax);
			int K = (int) (Math.random() * KMax);
			double ans1 = right(N, M, K);
			double ans2 = dp3(N, M, K);
			double ans3 = dp2(N, M, K);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("Oops!");
				break;
			}
		}
		System.out.println("测试结束");
	}

}
