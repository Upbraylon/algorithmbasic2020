package class21_蓄水池_斐波那契;

/**
 * 蓄水池算法
 * 10个中奖名单
 * i号球：10/i的概率中奖，奖池中某个球1/10的概率淘汰
 */
public class Code01_ReservoirSampling {

	public static class RandomBox {
		// 奖池
		private int[] bag;
		// 总的中奖个数
		private int N;
		// 第几个球
		private int count;

		public RandomBox(int capacity) {
			bag = new int[capacity];
			N = capacity;
			count = 0;
		}

		// 请等概率返回1~i中的一个数字
		private int rand(int i) {
			return (int) (Math.random() * i) + 1;
		}

		// 加入奖池：N/count的概率中奖，奖池中1/N的概率淘汰
		public void add(int num) {
			count++;
			if (count <= N) {
				bag[count - 1] = num;
			} else if (rand(count) <= N) {
				bag[rand(N) - 1] = num;
			}
		}

		// 返回中奖的答案
		public int[] choices() {
			int[] ans = new int[N];
			for (int i = 0; i < N; i++) {
				ans[i] = bag[i];
			}
			return ans;
		}

	}

	// 请等概率返回1~i中的一个数字
	public static int random(int i) {
		return (int) (Math.random() * i) + 1;
	}

	public static void main(String[] args) {
		System.out.println("hello");
		int test = 10000;
		int ballNum = 17;
		int[] count = new int[ballNum + 1];
		for (int i = 0; i < test; i++) {
			int[] bag = new int[10];
			int bagi = 0;
			for (int num = 1; num <= ballNum; num++) {
				// 小于中奖总数的直接进去，100%中奖
				if (num <= 10) {
					bag[bagi++] = num;
				} else { // num > 10
					// 10/num的概率中奖
					if (random(num) <= 10) {
						// bag中有个球1/10的概率淘汰
						bagi = (int) (Math.random() * 10);
						bag[bagi] = num;
					}
				}

			}
			// 统计多次抽奖中 每个球的中奖次数
			for (int num : bag) {
				count[num]++;
			}
		}
		for (int i = 1; i <= ballNum; i++) {
			System.out.println(i + " : " + count[i]);
		}

		System.out.println("hello");
		int all = 100;
		int choose = 10;
		int testTimes = 50000;
		int[] counts = new int[all + 1];
		for (int i = 0; i < testTimes; i++) {
			RandomBox box = new RandomBox(choose);
			for (int num = 1; num <= all; num++) {
				box.add(num);
			}
			int[] ans = box.choices();
			for (int j = 0; j < ans.length; j++) {
				counts[ans[j]]++;
			}
		}

		for (int i = 0; i < counts.length; i++) {
			System.out.println(i + " times : " + counts[i]);
		}

	}
}
