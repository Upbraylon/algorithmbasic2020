package class08;

public class Solution {
    public static int titleToNumber(String columnTitle) {
        char[] chars = columnTitle.toCharArray();
        int ans = 0;
        for(int i = chars.length-1; i >=0; i--) {
            System.out.println((chars[i] - 'A' + 1));
            System.out.println(Math.pow(26, 1));
            ans += (chars[i]-'A' + 1) * Math.pow(26, 1);
        }

        return ans;
    }

    public static void main(String[] args) {

        System.out.println(titleToNumber("AB"));
    }
}
