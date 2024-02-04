package class22;

/**
 * 给定一个正数1，裂开的方法有一种，（1）
 * 给定一个正数2，裂开的方法有两种，（1，1） （2）
 * 给定一个正数3，裂开的方法有三种，（1，1，1）（1，2）（3）
 * 给定一个正数4，裂开的方法有五种，（1，1，1，1）（1，1，2）（1，3）（2，2）（4）
 * 给定一个正数n，求裂开的方法数（前面的裂开数不能比后面的大）
 */
public class Code03_SplitNumber {

    // n为正数
    public static int ways(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return process(1, n);
    }

    // 上一个拆出来的数是pre
    // 还剩rest需要去拆
    // 返回拆解的方法数
    public static int process(int pre, int rest) {
        if (rest == 0) {
            return 1;
        }
        if (pre > rest) {
            return 0;
        }
        int ways = 0;
        for (int first = pre; first <= rest; first++) {
            ways += process(first, rest - first);
        }
        return ways;
    }

    public static int dp1(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int[][] dp = new int[n + 1][n + 1];
        for (int pre = 1; pre <= n; pre++) {
            dp[pre][0] = 1;
        }
        for (int pre = n; pre >= 1; pre--) {
            for (int rest = pre; rest <= n; rest++) {
                int ways = 0;
                for (int first = pre; first <= rest; first++) {
                    ways += dp[first][rest - first];
                }
                dp[pre][rest] = ways;
            }
        }
        return dp[1][n];
    }

    /**
     * 位置依赖
     *
     * @param n
     * @return
     */
    public static int dp2(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int[][] dp = new int[n + 1][n + 1];
        for (int pre = 1; pre <= n; pre++) {
            dp[pre][0] = 1;
            // 对角线也为1
            dp[pre][pre] = 1;
        }
        for (int pre = n - 1; pre >= 1; pre--) {
            for (int rest = pre + 1; rest <= n; rest++) {
                // pre从n开始dp[pre + 1][rest]会越界，把pre=n单独结算一下，除了dp[n][n]=1,其他都是0;但一想可知，pre= rest的也都为1
                dp[pre][rest] = dp[pre + 1][rest];
                dp[pre][rest] += dp[pre][rest - pre];
            }
        }
        return dp[1][n];
    }

    public static void main(String[] args) {
        int test = 39;
        System.out.println(ways(test));
        System.out.println(dp1(test));
        System.out.println(dp2(test));
    }

}
