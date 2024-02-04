package class00;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PrintTable {

    private Table table;
    //最大列宽：sql查询结果某列内容可能过大，不想完全显示，因此限制最大列宽
    private Integer maxWidth;
    //最大条数:sql查询结果可能有非常多，通常不必完全显示，因此限制最大条数
    private Integer maxLength;

    public PrintTable(List<List<String>> content, Integer maxWidth, Integer maxLength) {
        this.table = buildTable(content);
        this.maxLength = maxLength;
        this.maxWidth = maxWidth;
    }


    public PrintTable(List<List<String>> content) {
        this.table = buildTable(content);
        this.maxLength = 10;
        this.maxWidth = 40;
    }

    public PrintTable(int[][] dp) {
        int row = dp.length;
        int line = dp[0].length;
        List<List<String>> content = new ArrayList<>();
        for (int i=0; i<row; i++) {
            List<String> tmp = new ArrayList<>();
            for (int j = 0; j < line; j++) {
                tmp.add(String.valueOf(dp[i][j]));
            }
            content.add(tmp);
        }
        this.table = buildTable(content);
        this.maxLength = row;
        this.maxWidth = line;
    }

    /**
     * 创建Table实例
     *
     * @param content
     * @return
     */

    private Table buildTable(List<List<String>> content) {
        return new Table(content);
    }

    /**
     * 打印表格
     */
    public void printTable(String... symbols) {
        String symbol = symbols.length == 0 ? "|" : symbols[0];
        //按照最大列宽、最大数据量过滤后的表格
        Table limitTable = getLimitTable();
        //设置表格的最大宽度：得到每列宽度，再求和
        List<Integer> originMaxWidthList = getMaxWidthLenList(limitTable);
        limitTable.setMaxWidthList(originMaxWidthList);

        //得到格式化后的表格数据
        Table formatTable = getFormatTable(limitTable, symbol);
        Integer totalColSize = formatTable.getTotalColSize();
        //打印首行分割符号
        System.out.println(StringUtils.getRepeatChar("-", totalColSize));
        formatTable.getContent()
                .forEach(row -> {
                    row.forEach(System.out::print);
                    System.out.println();
                    //打印每行分割符号
                    System.out.println(StringUtils.getRepeatChar("-", totalColSize));
                });
    }


    /**
     * 格式化表格
     *
     * @param symbol 定义每列间隔符号
     * @return
     */
    private Table getFormatTable(Table table, String symbol) {
        //获取原表每列最大宽度
        List<Integer> originMaxWidthList = table.getMaxWidthList();
        //除了间隔符号外，固定在每个单元格前后加两个空格
        int symbolLen = symbol.length() + 2;
        //遍历原table，将每个单元格填充到该列最大长度
        List<List<String>> formatList = table.getContent().stream().map(
                row -> {
                    //用于流在遍历每行的过程中，获取列序号
                    AtomicInteger atomicInteger = new AtomicInteger(0);
                    return row.stream().map(cell -> {
                        //当前遍历的列序号
                        int j = atomicInteger.getAndIncrement();
                        //原表该列的最大宽度+间隔符号宽度-双字节出现的次数
                        int cellSize = originMaxWidthList.get(j) + symbolLen - StringUtils.getZHCharCount(cell);
                        //如果是首行，还需要再前面加一个分割符号|，故长度加1
                        cellSize = j == 0 ? cellSize + 1 : cellSize;
                        //返回原始字符串按照指定symbol填充到指定长度cellSize，并居中对齐的字符
                        return StringUtils.getPadString(cell, cellSize, symbol, j);
                    }).collect(Collectors.toList());
                }
        ).collect(Collectors.toList());
        //存储格式化后的表格数据
        Table formatTable = buildTable(formatList);
        //设置格式化表格的总宽度：原始宽度+自定义分割符号的总宽度（列数*符号宽度）+首列前面的符号宽度
        int totalColSize = table.getTotalColSize() + table.getColCount() * symbolLen + 1;
        formatTable.setTotalColSize(totalColSize);
        return formatTable;
    }

    /**
     * @return 获取经过条件过滤的表格
     */
    private Table getLimitTable() {
        List<List<String>> limitContent = table.getContent().stream()
                .limit(maxLength)
                .map(row -> row.stream()
                        //去除内容中含制表符时对结果展示的影响
                        .map(cell -> cell == null ? null : cell.replaceAll("\t", " "))
                        .map(cell -> cell != null && cell.length() > maxWidth ? cell.substring(0, maxWidth) : cell)
                        .collect(Collectors.toList())
                ).collect(Collectors.toList());
        return buildTable(limitContent);
    }

    /**
     * 计算table每行的最大宽度
     * 要使列宽相等，就需要将每个单元格宽度设置为该列最大宽度，二计算每行最大宽度相对容易些
     * 故将content转置后得到的每行最大宽度即为所求
     * 需要考虑单双字节的情况，比如有数组arr:{"aabb","sql表格","编程学习"},
     * 按照String.length计算，arr[1]最长，但是实际上arr[2]看起来才是最宽的
     * 因此计算宽度时，将双字节字符看做2个单位长度，即：每出现一个双字节字符，长度+1
     *
     * @return
     */
    private List<Integer> getMaxWidthLenList(Table table) {
        //得到转置数组每个元素的长度,一个中文算两个长度
        return Arrays.stream(table.transpose())
                .map(rows -> Arrays.stream(rows)
                        .mapToInt(s -> {
                            //sql查询结果如果为null，则认为长度为4
                            if (s == null) {
                                return 4;
                            } else {
                                //加上双字节字符出现的次数，最短为null，四个字符
                                return s.length() + StringUtils.getZHCharCount(s);
                            }
                        }).max().orElse(0)
                ).collect(Collectors.toList());
    }

    private class Table {
        /**
         * 表格内容（含表头）
         */
        private List<List<String>> content = new ArrayList<>();

        /**
         * 表格列总字符长度：便于打印行分割符号
         */
        private Integer totalColSize;
        /**
         * 每列最大宽度
         */
        private List<Integer> maxWidthList;


        Integer getTotalColSize() {
            if (totalColSize == null && maxWidthList != null && maxWidthList.size() != 0) {
                this.totalColSize = maxWidthList.stream().reduce(Integer::sum).get();
            }
            return totalColSize;
        }

        //private限制只能通过外部类构造
        private Table(List<List<String>> content) {
            this.content = content;
        }

        //获取表格行数
        int getRowCount() {
            return content.size();
        }

        //获取表格列数，0行代表表头，默认认为content中至少含有表头
        int getColCount() {
            return content.get(0).size();
        }

        /**
         * 转置二维数组
         *
         * @return
         */
        private String[][] transpose() {
            int rowCount = getRowCount();
            int colCount = getColCount();
            String[][] result = new String[colCount][rowCount];

            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    result[j][i] = content.get(i).get(j);
                }
            }
            return result;
        }

        public List<List<String>> getContent() {
            return content;
        }

        public void setContent(List<List<String>> content) {
            this.content = content;
        }

        public void setTotalColSize(Integer totalColSize) {
            this.totalColSize = totalColSize;
        }

        public List<Integer> getMaxWidthList() {
            return maxWidthList;
        }

        public void setMaxWidthList(List<Integer> maxWidthList) {
            this.maxWidthList = maxWidthList;
        }
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public static void main(String[] args) {
        System.out.println("======对角线左上角到右下角========");
        // 行从0开始，列最终到N-1，限制因素是列
        int[][] dp0 = new int[10][10];
        for (int i = 0; i < 10; i++) {
            int row = 0;
            int line = i;
            while (line <10){
                dp0[row][line] = i+1;
                row++;
                line++;
            }
        }
        new PrintTable(dp0).printTable();

        System.out.println("======对角线右下角到左上角========");
        int[][] dp1 = new int[10][10];
        for (int i = 9; i >= 0 ; i--) {
            int row = i;
            int line = 9;
            while (row >= 0) {
                dp1[row][line] = i;
                row--;
                line--;
            }
        }
        new PrintTable(dp1).printTable();

        System.out.println("======对角线========");
        int[][] dp11 = new int[10][10];
        int N = dp11.length;
        dp11[N-1][N-1] = 1;
        for (int i = 0; i < N - 1; i++) {
            dp11[i][i] = 1;
            dp11[i][i+1] = 2;
        }
        for (int i = N-3 ; i >= 0; i--) {
            for (int j = i+2; j < N ; j++) {
                dp11[i][j] = dp11[i][j-1] + dp11[i+1][j];
            }
        }
        new PrintTable(dp11).printTable();

        System.out.println("======从上往下从左往右========");
        int[][] dp2 = new int[8][10];
        int m = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++) {
                dp2[i][j] = m++;
            }
        }
        new PrintTable(dp2).printTable();

        System.out.println("======从下往上从左往右========");
        int[][] dp3 = new int[8][10];
        int n = 0;
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 10; j++) {
                dp3[i][j] = n++;
            }
        }
        new PrintTable(dp3).printTable();

        System.out.println("======从下往上从右往左========");
        int[][] dp4 = new int[8][10];
        int h = 0;
        for (int i = 7; i >= 0; i--) {
            for (int j = 9; j >= 0; j--) {
                dp4[i][j] = h++;
            }
        }
        new PrintTable(dp4).printTable();

        System.out.println("======从上往下从右往左========");
        int[][] dp5 = new int[8][10];
        int k = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 9; j >= 0; j--) {
                dp5[i][j] = k++;
            }
        }
        new PrintTable(dp5).printTable();
    }
}


