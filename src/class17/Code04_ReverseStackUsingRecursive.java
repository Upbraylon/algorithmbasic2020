package class17;

import java.util.Stack;

/**
 * 给你一个栈，请逆序这个栈
 * 不能申请额外的数据结构，只能使用递归函数，如何实现
 */
public class Code04_ReverseStackUsingRecursive {

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
		reverse(test);
		while (!test.isEmpty()) {
			System.out.println(test.pop());
		}

	}

}
