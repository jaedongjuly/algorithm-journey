package class108;

// 二维数组上范围增加、范围查询，使用树状数组的模版(java)
// 测试链接 : https://www.luogu.com.cn/problem/P4514
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

public class Code05_TwoDimensionIntervalAddIntervalQuery1 {

	public static int MAXN = 2050;

	public static int MAXM = 2050;

	// 维护信息 : d[i][j]
	public static int[][] info1 = new int[MAXN][MAXM];

	// 维护信息 : d[i][j] * i
	public static int[][] info2 = new int[MAXN][MAXM];

	// 维护信息 : d[i][j] * j
	public static int[][] info3 = new int[MAXN][MAXM];

	// 维护信息 : d[i][j] * i * j
	public static int[][] info4 = new int[MAXN][MAXM];

	public static int n, m;

	public static int lowbit(int i) {
		return i & -i;
	}

	public static void add(int x, int y, int v) {
		int v1 = v;
		int v2 = x * v;
		int v3 = y * v;
		int v4 = x * y * v;
		for (int i = x; i <= n; i += lowbit(i)) {
			for (int j = y; j <= m; j += lowbit(j)) {
				info1[i][j] += v1;
				info2[i][j] += v2;
				info3[i][j] += v3;
				info4[i][j] += v4;
			}
		}
	}

	// 以(1,1)左上角，以(x,y)右下角
	public static int sum(int x, int y) {
		int ans = 0;
		for (int i = x; i > 0; i -= lowbit(i)) {
			for (int j = y; j > 0; j -= lowbit(j)) {
				ans += (x + 1) * (y + 1) * info1[i][j] - (y + 1) * info2[i][j] - (x + 1) * info3[i][j] + info4[i][j];
			}
		}
		return ans;
	}

	public static void add(int a, int b, int c, int d, int v) {
		add(a, b, v);
		add(a, d + 1, -v);
		add(c + 1, b, -v);
		add(c + 1, d + 1, v);
	}

	public static int range(int a, int b, int c, int d) {
		return sum(c, d) - sum(a - 1, d) - sum(c, b - 1) + sum(a - 1, b - 1);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		String op;
		int a, b, c, d, v;
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			op = in.sval;
			if (op.equals("X")) {
				in.nextToken();
				n = (int) in.nval;
				in.nextToken();
				m = (int) in.nval;
			} else if (op.equals("L")) {
				in.nextToken();
				a = (int) in.nval;
				in.nextToken();
				b = (int) in.nval;
				in.nextToken();
				c = (int) in.nval;
				in.nextToken();
				d = (int) in.nval;
				in.nextToken();
				v = (int) in.nval;
				add(a, b, c, d, v);
			} else {
				in.nextToken();
				a = (int) in.nval;
				in.nextToken();
				b = (int) in.nval;
				in.nextToken();
				c = (int) in.nval;
				in.nextToken();
				d = (int) in.nval;
				System.out.println(range(a, b, c, d)); // 改用System.out.println可以通过了
			}
		}
		br.close();
	}

}
