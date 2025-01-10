package class16;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 给定一个数组arr，arr[i]代表第i号咖啡机泡一杯咖啡的时间
 * 给定一个正数N，表示N个人等着咖啡机泡咖啡，每台咖啡机只能轮流泡咖啡
 * 阶段一：求出所有人最快喝完咖啡的时间：排队
 *
 * 只有一台洗咖啡杯的机器，一次只能洗一个杯子，时间耗费a，洗完才能洗下一个杯子
 * 每个咖啡杯也可以自己挥发干净，时间耗费b，咖啡杯可以并行挥发
 * 假设所有人拿到咖啡之后立刻喝干净，返回从开始等到所有咖啡机变干净的最短时间
 * 三个参数：int[] arr, int N, int a, int b
 *
 * 暗含要求：N个人要最快喝完，最快洗完咖啡杯
 */
public class Code03_Coffee {

	// 验证的方法
	// 彻底的暴力
	// 很慢但是绝对正确
	public static int right(int[] arr, int n, int a, int b) {
		int[] times = new int[arr.length];
		int[] drink = new int[n];
		return forceMake(arr, times, 0, drink, n, a, b);
	}

	// 每个人暴力尝试用每一个咖啡机给自己做咖啡
	public static int forceMake(int[] arr, int[] times, int kth, int[] drink, int n, int a, int b) {
		if (kth == n) {
			int[] drinkSorted = Arrays.copyOf(drink, kth);
			Arrays.sort(drinkSorted);
			return forceWash(drinkSorted, a, b, 0, 0, 0);
		}
		int time = Integer.MAX_VALUE;
		for (int i = 0; i < arr.length; i++) {
			int work = arr[i];
			int pre = times[i];
			drink[kth] = pre + work;
			times[i] = pre + work;
			time = Math.min(time, forceMake(arr, times, kth + 1, drink, n, a, b));
			drink[kth] = 0;
			times[i] = pre;
		}
		return time;
	}

	public static int forceWash(int[] drinks, int a, int b, int index, int washLine, int time) {
		if (index == drinks.length) {
			return time;
		}
		// 选择一：当前index号咖啡杯，选择用洗咖啡机刷干净
		int wash = Math.max(drinks[index], washLine) + a;
		int ans1 = forceWash(drinks, a, b, index + 1, wash, Math.max(wash, time));

		// 选择二：当前index号咖啡杯，选择自然挥发
		int dry = drinks[index] + b;
		int ans2 = forceWash(drinks, a, b, index + 1, washLine, Math.max(dry, time));
		return Math.min(ans1, ans2);
	}

	// 以下为贪心+优良暴力
	public static class Machine {
		public int timePoint;
		public int workTime;

		public Machine(int t, int w) {
			timePoint = t;
			workTime = w;
		}
	}

	public static class MachineComparator implements Comparator<Machine> {

		@Override
		public int compare(Machine o1, Machine o2) {
			return (o1.timePoint + o1.workTime) - (o2.timePoint + o2.workTime);
		}

	}

	// 优良一点的暴力尝试的方法
	public static int minTime1(int[] arr, int n, int a, int b) {
		PriorityQueue<Machine> heap = new PriorityQueue<Machine>(new MachineComparator());
		for (int i = 0; i < arr.length; i++) {
			heap.add(new Machine(0, arr[i]));
		}
		// 每个人喝完咖啡的最快时间（最优解）
		int[] drinks = new int[n];
		for (int i = 0; i < n; i++) {
			Machine cur = heap.poll();
			cur.timePoint += cur.workTime;
			drinks[i] = cur.timePoint;
			heap.add(cur);
		}
		return bestTime(drinks, a, b, 0, 0);
	}

	// drinks 所有杯子可以开始洗的时间
	// wash 单杯洗干净的时间（串行）
	// air 挥发干净的时间(并行)
	// free 洗的机器什么时候可用
	// drinks[index.....]都变干净，最早的结束时间（返回）
	public static int bestTime(int[] drinks, int wash, int air, int index, int free) {
		if (index == drinks.length) {
			return 0;
		}
		// index号杯子 决定洗
		// Math.max(drinks[index], free) 什么时候可以洗，selfClean1什么时候洗干净
		int selfClean1 = Math.max(drinks[index], free) + wash;
		// 剩下杯子变干净的时间，其中洗的机器可再用的时间为当前杯子洗碗的时间
		int restClean1 = bestTime(drinks, wash, air, index + 1, selfClean1);
		int p1 = Math.max(selfClean1, restClean1);

		// index号杯子 决定挥发
		int selfClean2 = drinks[index] + air;
		// 洗的机器可再用时间不变
		int restClean2 = bestTime(drinks, wash, air, index + 1, free);
		int p2 = Math.max(selfClean2, restClean2);
		return Math.min(p1, p2);
	}

	// 贪心+优良尝试改成动态规划
	public static int minTime2(int[] arr, int n, int a, int b) {
		PriorityQueue<Machine> heap = new PriorityQueue<Machine>(new MachineComparator());
		for (int i = 0; i < arr.length; i++) {
			heap.add(new Machine(0, arr[i]));
		}
		int[] drinks = new int[n];
		for (int i = 0; i < n; i++) {
			Machine cur = heap.poll();
			cur.timePoint += cur.workTime;
			drinks[i] = cur.timePoint;
			heap.add(cur);
		}
		return bestTimeDp(drinks, a, b);
	}

	public static int bestTimeDp(int[] drinks, int wash, int air) {
		int N = drinks.length;
		int maxFree = 0;
		// air的范围不好确定，那就取它最大能达到的值：所有杯子串型去洗
		for (int i = 0; i < drinks.length; i++) {
			maxFree = Math.max(maxFree, drinks[i]) + wash;
		}
		int[][] dp = new int[N + 1][maxFree + 1];
		for (int index = N - 1; index >= 0; index--) {
			for (int free = 0; free <= maxFree; free++) {
				int selfClean1 = Math.max(drinks[index], free) + wash;
				if (selfClean1 > maxFree) {
					break; // 因为后面的也都不用填了
				}
				// index号杯子 决定洗
				int restClean1 = dp[index + 1][selfClean1];
				int p1 = Math.max(selfClean1, restClean1);
				// index号杯子 决定挥发
				int selfClean2 = drinks[index] + air;
				int restClean2 = dp[index + 1][free];
				int p2 = Math.max(selfClean2, restClean2);
				dp[index][free] = Math.min(p1, p2);
			}
		}
		return dp[0][0];
	}

	// for test
	public static int[] randomArray(int len, int max) {
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) (Math.random() * max) + 1;
		}
		return arr;
	}

	// for test
	public static void printArray(int[] arr) {
		System.out.print("arr : ");
		for (int j = 0; j < arr.length; j++) {
			System.out.print(arr[j] + ", ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int len = 10;
		int max = 10;
		int testTime = 10;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = randomArray(len, max);
			int n = (int) (Math.random() * 7) + 1;
			int a = (int) (Math.random() * 7) + 1;
			int b = (int) (Math.random() * 10) + 1;
			int ans1 = right(arr, n, a, b);
			int ans2 = minTime1(arr, n, a, b);
			int ans3 = minTime2(arr, n, a, b);
			int ans4 = minTime3(arr, n, a, b);
			if (ans1 != ans2 || ans2 != ans3 || ans2 != ans4) {
				printArray(arr);
				System.out.println("n : " + n);
				System.out.println("a : " + a);
				System.out.println("b : " + b);
				System.out.println(ans1 + " , " + ans2 + " , " + ans3 + " , " + ans4);
				System.out.println("===============");
				break;
			}
		}
		System.out.println("测试结束");

	}

	// arr代表咖啡机造咖啡的时间，n个人来喝，a代表洗杯子的时间，b代表挥发的时间，求所有人喝完咖啡且杯子都干净的最短时间
	public static int minTime3(int[] arr, int n, int a, int b) {
		if(arr == null || arr.length == 0) {
			return -1;
		}

		PriorityQueue<MyMachine> heap = new PriorityQueue<>();
		for (int j : arr) {
			MyMachine myMachine = new MyMachine(0, j);
			heap.add(myMachine);
		}
		int[] drinks = new int[n];
		for (int i = 0; i < n; i++) {
			MyMachine poll = heap.poll();
			drinks[i] = poll.start + poll.cost;
			heap.add(new MyMachine(drinks[i], poll.cost));
		}
		//return myBestTime(drinks, a, b, 0, 0);
		return MyBestTimeDp(drinks, a, b);
	}

	private static int myBestTime(int[] drinks, int wash, int air, int index, int free) {
		if(index == drinks.length) {
			return 0;
		}
		int clean1 = Math.max(drinks[index], free) + wash;
		int rest1 = myBestTime(drinks, wash, air, index+1, clean1);

		int clean2 = drinks[index] + air;
		int rest2 = myBestTime(drinks, wash, air, index + 1, free);

		int ans1 = Math.max(clean1, rest1);
		int ans2 = Math.max(clean2, rest2);

		return Math.min(ans1, ans2);
	}

	private static int MyBestTimeDp(int[] drinks, int wash, int air) {
		int N = drinks.length;
		int maxFree = 0;
		for (int dr : drinks) {
			maxFree = Math.max(maxFree, dr) + wash;
		}
		int[][] dp = new int[N+1][maxFree+1];

		for (int i = N-1; i >=0 ; i--) {
			for (int j = 0; j <= maxFree; j++) {
				int p1 = Math.max(drinks[i],j) + wash;
				if(p1 > maxFree) {
					continue;
				}
				p1 = Math.max(p1, dp[i+1][p1]);
				int p2 = drinks[i] + air;
				p2 = Math.max(p2, dp[i+1][j]);

				dp[i][j] = Math.min(p1, p2);
			}
		}

		return dp[0][0];
	}
	private static class MyMachine implements Comparable<MyMachine>{
		private int start;
		private int cost;

		public MyMachine(int start, int cost) {
			this.start = start;
			this.cost = cost;
		}


		@Override
		public int compareTo(MyMachine o) {
			return this.start + this.cost - o.start - o.cost;
		}
	}
}
