package class38;

public class Code02_EatGrass {

	// 如果n份草，最终先手赢，返回"先手"
	// 如果n份草，最终后手赢，返回"后手"
	public static String whoWin(int n) {
		if (n < 5) {
			return n == 0 || n == 2 ? "后手" : "先手";
		}
		// 进到这个过程里来，当前的先手，先选
		int want = 1;
		// want从1开始，每个分支*4，如果其中一个能让先手赢，就返回先手；
		// 如果want已经大于n,先手都赢不了，返回后手
		while (want <= n) {
			// 后续过程的后手赢了，当前先手就赢了，当前先手和手续后手是一个人
			if (whoWin(n - want).equals("后手")) {
				return "先手";
			}
			// 防止可能的溢出
			if (want <= (n / 4)) {
				want *= 4;
			} else {
				break;
			}
		}
		return "后手";
	}

	public static String winner1(int n) {
		if (n < 5) {
			return (n == 0 || n == 2) ? "后手" : "先手";
		}
		int base = 1;
		while (base <= n) {
			if (winner1(n - base).equals("后手")) {
				return "先手";
			}
			if (base > n / 4) { // 防止base*4之后溢出
				break;
			}
			base *= 4;
		}
		return "后手";
	}

	public static String winner2(int n) {
		if (n % 5 == 0 || n % 5 == 2) {
			return "后手";
		} else {
			return "先手";
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i <= 50; i++) {
			System.out.println(i + " : " + whoWin(i));
		}
	}

}
