package class10_二叉树Morris遍历;

public class Code03_IsSymmetric {
    // 翻转后，与原树相同，则为轴对称
    public boolean isSymmetric(TreeNode root) {
        return isSameTree(cloneTree(root), invertTree(root));
    }

    // 克隆二叉树
    public TreeNode cloneTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        return new TreeNode(root.val, cloneTree(root.left), cloneTree(root.right));
    }

    // 226.翻转二叉树
    public TreeNode invertTree(TreeNode root) {
        if (root == null)
            return null;

        TreeNode left = root.left;
        root.left = root.right;
        root.right = left;

        invertTree(root.left);
        invertTree(root.right);

        return root;
    }

    // 100.相同的树
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null) {
            return q == null;
        }
        return q != null && p.val == q.val
                && isSameTree(p.left, q.left)
                && isSameTree(p.right, q.right);
    }
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
