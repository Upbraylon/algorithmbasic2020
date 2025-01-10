package class17;

/**
 * 给定一个二维数组matrix，一个人必须从左上角出发，最后到达右下角，沿途只可以向下或者向右走，
 * 沿途的数字都累加就是距离累加和，返回最小距离累加和。
 */
public class Code01_MinPathSum {

	public static int minPathSum1(int[][] m) {
		if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
			return 0;
		}
		int row = m.length;
		int col = m[0].length;
		int[][] dp = new int[row][col];
		dp[0][0] = m[0][0];
		for (int i = 1; i < row; i++) {
			dp[i][0] = dp[i - 1][0] + m[i][0];
		}
		for (int j = 1; j < col; j++) {
			dp[0][j] = dp[0][j - 1] + m[0][j];
		}
		for (int i = 1; i < row; i++) {
			for (int j = 1; j < col; j++) {
				dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + m[i][j];
			}
		}
		return dp[row - 1][col - 1];
	}

	public static int minPathSum2(int[][] m) {
		if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
			return 0;
		}
		int row = m.length;
		int col = m[0].length;
		int[] dp = new int[col];
		dp[0] = m[0][0];
		for (int j = 1; j < col; j++) {
			dp[j] = dp[j - 1] + m[0][j];
		}
		for (int i = 1; i < row; i++) {
			dp[0] += m[i][0];
			for (int j = 1; j < col; j++) {
				dp[j] = Math.min(dp[j - 1], dp[j]) + m[i][j];
			}
		}
		return dp[col - 1];
	}

	// for test
	public static int[][] generateRandomMatrix(int rowSize, int colSize) {
		if (rowSize < 0 || colSize < 0) {
			return null;
		}
		int[][] result = new int[rowSize][colSize];
		for (int i = 0; i != result.length; i++) {
			for (int j = 0; j != result[0].length; j++) {
				result[i][j] = (int) (Math.random() * 100);
			}
		}
		return result;
	}

	// for test
	public static void printMatrix(int[][] matrix) {
		for (int i = 0; i != matrix.length; i++) {
			for (int j = 0; j != matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int rowSize = 10;
		int colSize = 10;
		int[][] m = generateRandomMatrix(rowSize, colSize);
		System.out.println(minPathSum1(m));
		System.out.println(minPathSum2(m));
		System.out.println(minStep(m));
		System.out.println(minStepByCache(m));
		System.out.println(minStepByOneDp(m));
	}

	public static int minStep(int[][] m){
		if(m==null || m.length==0){
			return 0;
		}
		return process(m, 0, 0);
	}

	/**
	 * 当前来到（i，j）,走到最右下角的最小路径和
	 * @param m
	 * @param i
	 * @param j
	 * @return
	 */
	private static int process(int[][] m, int i, int j){
		int col = m[0].length-1;
		int row = m.length-1;
		if(i==row && j==col){
			return m[i][j];
		}
		if(i==row){
			return m[i][j] + process(m, i, j+1);
		}

		if(j==col){
			return m[i][j] + process(m, i+1, j);
		}

		int p1 = process(m, i, j+1);
		int p2 = process(m, i+1, j);

		return m[i][j] + Math.min(p2, p1);
	}

	private static int processByCache(int[][] m, int i, int j, int[][] cache){
		if(cache[i][j] != -1){
			return cache[i][j];
		}
		int row = m.length-1;
		int col = m[0].length-1;
		int ans = 0;
		if(i==row && j==col){
			ans = m[i][j];
		} else if(i==row){
			ans = m[i][j] + processByCache(m, i, j+1, cache);
		} else if(j==col){
			ans = m[i][j] + processByCache(m, i+1, j, cache);
		} else {
			int p1 = processByCache(m, i, j+1, cache);
			int p2 = processByCache(m, i+1, j, cache);
			ans = m[i][j] + Math.min(p2, p1);
		}
		cache[i][j] = ans;
		return ans;
	}
	public static int minStepByCache(int[][] m){
		if(m==null || m.length==0){
			return 0;
		}
		int[][] cache = new int[m.length][m[0].length];
		for (int i=0; i<m.length; i++){
			for (int j=0; j<m[0].length;j++){
				cache[i][j] = -1;
			}
		}
		return processByCache(m, 0, 0, cache);
	}

	public static int minStepByDp(int[][] m){
		if(m==null || m.length==0){
			return 0;
		}
		int row = m.length;
		int col = m[0].length;
		int[][] dp = new int[row][col];
		dp[row-1][col-1] = m[row-1][col-1];

		for (int i=col-2; i >=0; i--){
			dp[row-1][i] = m[row-1][i] + dp[row-1][i+1];
		}
		for(int i= row-2; i>=0; i--){
			dp[i][col-1] = m[i][col-1] + dp[i+1][col-1];
		}

		for (int i = row-2; i>=0; i--){
			for (int j= col-2; j>=0; j--){
				int p1 = dp[i][j+1];
				int p2 = dp[i+1][j];
				dp[i][j] = m[i][j] + Math.min(p2,p1);
			}
		}
		return dp[0][0];
	}
	public static int minStepByOneDp(int[][] m){
		if(m == null || m.length==0){
			return 0;
		}
		int row = m.length;
		int col = m[0].length;
		int[] dp =new int[col];


		dp[col-1] = m[row-1][col-1];
		for (int i = col-2; i>=0; i--){
			dp[i] = m[row-1][i] + dp[i+1];
		}
		for (int i = row-2; i >=0; i--){
			dp[col-1] = m[i][col-1] + dp[col-1];
			for (int j=col-2; j>=0; j--){
				dp[j] = m[i][j] + Math.min(dp[j], dp[j+1]);
			}
		}
		return dp[0];
	}
}
