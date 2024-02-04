package class18;

public class CardLine1 {

    public static int whoWillWin(int[] arr){
        if(arr==null || arr.length ==0){
            return 0;
        }
        int p1 = f(arr, 0, arr.length-1);
        int p2 = h(arr, 0, arr.length-1);
        return  Math.max(p1, p2);
    }

    private static int f(int[] arr, int L, int R){
        if(L==R){
            return arr[L];
        }
        int p1 = arr[L] + h(arr, L+1, R);
        int p2 = h(arr, L, R-1) + arr[R];

        return Math.max(p1, p2);
    }

    private static int h(int[] arr, int L, int R){
        if(L==R){
            return 0;
        }
        int p1 = f(arr, L+1, R);
        int p2 = f(arr, L, R-1);

        return Math.min(p1, p2);
    }


    public static int win(int[] arr){
        if(arr == null || arr.length==0){
            return 0;
        }

        int N = arr.length;
        int[][] fdp = new int[N][N];
        int[][] hdp = new int[N][N];
        for(int i=0; i<N; i++){
            fdp[i][i] = arr[i];
        }

        for(int startcow = 1; startcow < N; startcow ++){
            int L = 0;
            int R = startcow;
            while (R<N){
                fdp[L][R] = Math.max((arr[L]+ hdp[L+1][R]), hdp[L][R-1] + arr[R]);
                hdp[L][R] = Math.min(fdp[L+1][R], fdp[L][R-1]);
                L++;
                R++;
            }
        }
        return Math.max(fdp[0][N-1], hdp[0][N-1]);
    }
    public static int win3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int N = arr.length;
        int[][] fmap = new int[N][N];
        int[][] gmap = new int[N][N];
        for (int i = 0; i < N; i++) {
            fmap[i][i] = arr[i];
        }
        for (int startCol = 1; startCol < N; startCol++) {
            int L = 0;
            int R = startCol;
            // 对角线往下，行不会越界，列会先越界
            while (R < N) {
                fmap[L][R] = Math.max(arr[L] + gmap[L + 1][R], arr[R] + gmap[L][R - 1]);
                gmap[L][R] = Math.min(fmap[L + 1][R], fmap[L][R - 1]);
                L++;
                R++;
            }
        }
        return Math.max(fmap[0][N - 1], gmap[0][N - 1]);
    }

    public static void main(String[] args) {
        int[] arr = { 5, 7, 4, 5, 8, 1, 6, 0, 3, 4, 6, 1, 7 };
        System.out.println(whoWillWin(arr));
        System.out.println(win(arr));
        System.out.println(win3(arr));

    }

}
