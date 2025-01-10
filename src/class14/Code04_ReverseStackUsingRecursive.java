package class14;

import java.util.Stack;

/**
 * 给你一个栈，请逆序这个栈
 * 不能申请额外的数据结构，只能使用递归函数，如何实现
 */
public class Code04_ReverseStackUsingRecursive {

	public static void reverseee(Stack<Integer> stack) {
		if(stack.isEmpty()) {
			return;
		}
		// 逆序要把最低层的放到最高层,先把最底层的数据拿走，减少问题规模
		int bottom = ff(stack);
		// 把剩下的逆序
		reverseee(stack);
		// 把拿出来的放回去
		stack.push(bottom);
	}

	private static int ff(Stack<Integer> stack) {
		if(stack.size() == 1) {
			return stack.pop();
		}
		// 我先把最上层的拿住，给你减少规模，你去把剩余的最低数据拿出来
		int top = stack.pop();
		int bottom = ff(stack);
		stack.push(top);
		return bottom;
	}



	public static void reverse(Stack<Integer> stack) {
		if (stack.isEmpty()) {
			return;
		}
		int i = f(stack);
		reverse(stack);
		stack.push(i);
	}


	/**
	 * 栈底元素移除掉,上面的元素盖下来,返回移除掉的栈底元素
	 * 比如入栈 3 2 1，3在栈底，函数跑完之后，栈中元素 1 2，2在栈底，返回3
	 *
	 * @param stack
	 * @return
	 */
	public static int f(Stack<Integer> stack) {
		int result = stack.pop();
		if (stack.isEmpty()) {
			return result;
		} else {
			int last = f(stack);
			stack.push(result);
			return last;
		}
	}

	public static void main(String[] args) {
		Stack<Integer> test = new Stack<Integer>();
		test.push(1);
		test.push(2);
		test.push(3);
		test.push(4);
		test.push(5);
		reverseee(test);
		while (!test.isEmpty()) {
			System.out.println(test.pop());
		}
	}

	/**
	 * 如果最后一个数上面的都已经反转，那我把最后一个数put进stack即可
	 * 如何反转最简单--只剩栈顶元素或栈空最简单，那就从底向上清空栈
	 *
	 * 栈如何从底向上一步一步为空？
	 *
	 * @param stack
	 */
	private static void fanzhuanstack(Stack<Integer> stack){
		if(stack.isEmpty()){
			return;
		}
		// I will get and grep the bottom num,
		int bottom = getBottom(stack);
		// then you help me to reverse the rest of numbers
		fanzhuanstack(stack);
		// Finally， I will put the bottom num to top
		stack.push(bottom);
	}

	// 子问题须与原始问题为同样的事，且更为简单；

	/**
	 * 如何降低问题规模？
	 * 从哪个方向降低问题规模？--让子问题更简单的方向
	 *
	 * 怎么样使得拿栈底元素更简单？ -- 只有一个数，递归前进段那就以拿掉栈顶数为任务，递归返回段是把拿掉的数放回去
	 *
	 * @param stack
	 * @return
	 */
	private static int getBottom(Stack<Integer> stack) {
		// 我只能先帮你把栈顶的拿掉，减轻你负担，你去继续吧
		int result = stack.pop();
		if(stack.isEmpty()){
			return result;
		}
		int last = getBottom(stack);
		stack.push(result);
		return last;
	}

}
