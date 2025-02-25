package class160;

// 网络管理，java版
// 测试链接 : https://www.luogu.com.cn/problem/P4175
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code06_NetworkManagement1 {

	public static int MAXN = 80001;

	public static int MAXT = MAXN * 110;

	public static int MAXH = 18;

	public static int n, m, s;

	public static int[] arr = new int[MAXN];

	public static int[][] ques = new int[MAXN][3];

	public static int[] sorted = new int[MAXN << 1];

	// 链式前向星需要
	public static int[] head = new int[MAXN];

	public static int[] next = new int[MAXN << 1];

	public static int[] to = new int[MAXN << 1];

	public static int cntg = 0;

	// 注意这不是主席树！而是若干棵动态开点权值线段树！
	public static int[] root = new int[MAXN];

	public static int[] left = new int[MAXT];

	public static int[] right = new int[MAXT];

	public static int[] sum = new int[MAXT];

	public static int cntt = 0;

	// 树上倍增需要
	public static int[] deep = new int[MAXN];

	public static int[] size = new int[MAXN];

	public static int[] dfn = new int[MAXN];

	public static int[][] stjump = new int[MAXN][MAXH];

	public static int cntd = 0;

	// 查询信息需要，所有pos版本的信息和 - 所有pre版本的信息和
	public static int[] pos = new int[MAXN];

	public static int[] pre = new int[MAXN];

	public static int cntpos;

	public static int cntpre;

	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	public static int kth(int num) {
		int left = 1, right = s, mid;
		while (left <= right) {
			mid = (left + right) / 2;
			if (sorted[mid] == num) {
				return mid;
			} else if (sorted[mid] < num) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return -1;
	}

	public static int lowbit(int i) {
		return i & -i;
	}

	public static int add(int jobi, int jobv, int l, int r, int i) {
		if (i == 0) {
			i = ++cntt;
		}
		if (l == r) {
			sum[i] += jobv;
		} else {
			int mid = (l + r) / 2;
			if (jobi <= mid) {
				left[i] = add(jobi, jobv, l, mid, left[i]);
			} else {
				right[i] = add(jobi, jobv, mid + 1, r, right[i]);
			}
			sum[i] = sum[left[i]] + sum[right[i]];
		}
		return i;
	}

	public static void add(int i, int kth, int val) {
		for (; i <= n; i += lowbit(i)) {
			root[i] = add(kth, val, 1, s, root[i]);
		}
	}

	public static int queryNumber(int jobk, int l, int r) {
		if (l == r) {
			return l;
		}
		int mid = (l + r) / 2;
		int leftsum = 0;
		for (int i = 1; i <= cntpos; i++) {
			leftsum += sum[left[pos[i]]];
		}
		for (int i = 1; i <= cntpre; i++) {
			leftsum -= sum[left[pre[i]]];
		}
		if (jobk <= leftsum) {
			for (int i = 1; i <= cntpos; i++) {
				pos[i] = left[pos[i]];
			}
			for (int i = 1; i <= cntpre; i++) {
				pre[i] = left[pre[i]];
			}
			return queryNumber(jobk, l, mid);
		} else {
			for (int i = 1; i <= cntpos; i++) {
				pos[i] = right[pos[i]];
			}
			for (int i = 1; i <= cntpre; i++) {
				pre[i] = right[pre[i]];
			}
			return queryNumber(jobk - leftsum, mid + 1, r);
		}
	}

	// dfs1是递归版，java版本提交会爆栈，C++版本不会爆栈
	public static void dfs1(int u, int fa) {
		deep[u] = deep[fa] + 1;
		size[u] = 1;
		dfn[u] = ++cntd;
		stjump[u][0] = fa;
		for (int p = 1; p < MAXH; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		for (int e = head[u]; e > 0; e = next[e]) {
			if (to[e] != fa) {
				dfs1(to[e], u);
			}
		}
		for (int e = head[u]; e > 0; e = next[e]) {
			if (to[e] != fa) {
				size[u] += size[to[e]];
			}
		}
	}

	// dfs迭代版，都可以通过
	// 讲解118，详解了从递归版改迭代版
	public static int[][] ufe = new int[MAXN][3];

	public static int stackSize, u, f, e;

	public static void push(int u, int f, int e) {
		ufe[stackSize][0] = u;
		ufe[stackSize][1] = f;
		ufe[stackSize][2] = e;
		stackSize++;
	}

	public static void pop() {
		--stackSize;
		u = ufe[stackSize][0];
		f = ufe[stackSize][1];
		e = ufe[stackSize][2];
	}

	// dfs1的迭代版
	public static void dfs2() {
		stackSize = 0;
		push(1, 0, -1);
		while (stackSize > 0) {
			pop();
			if (e == -1) {
				deep[u] = deep[f] + 1;
				size[u] = 1;
				dfn[u] = ++cntd;
				stjump[u][0] = f;
				for (int p = 1; p < MAXH; p++) {
					stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
				}
				e = head[u];
			} else {
				e = next[e];
			}
			if (e != 0) {
				push(u, f, e);
				if (to[e] != f) {
					push(to[e], u, -1);
				}
			} else {
				for (int e = head[u]; e > 0; e = next[e]) {
					if (to[e] != f) {
						size[u] += size[to[e]];
					}
				}
			}
		}
	}

	public static int lca(int a, int b) {
		if (deep[a] < deep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		for (int p = MAXH - 1; p >= 0; p--) {
			if (deep[stjump[a][p]] >= deep[b]) {
				a = stjump[a][p];
			}
		}
		if (a == b) {
			return a;
		}
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		return stjump[a][0];
	}

	public static void prepare() {
		s = 0;
		for (int i = 1; i <= n; i++) {
			sorted[++s] = arr[i];
		}
		for (int i = 1; i <= m; i++) {
			if (ques[i][0] == 0) {
				sorted[++s] = ques[i][2];
			}
		}
		Arrays.sort(sorted, 1, s + 1);
		int len = 1;
		for (int i = 2; i <= s; i++) {
			if (sorted[len] != sorted[i]) {
				sorted[++len] = sorted[i];
			}
		}
		s = len;
		for (int i = 1; i <= n; i++) {
			arr[i] = kth(arr[i]);
		}
		dfs2();
		for (int i = 1; i <= n; i++) {
			add(dfn[i], arr[i], 1);
			add(dfn[i] + size[i], arr[i], -1);
		}
	}

	public static void change(int i, int v) {
		add(dfn[i], arr[i], -1);
		add(dfn[i] + size[i], arr[i], 1);
		arr[i] = kth(v);
		add(dfn[i], arr[i], 1);
		add(dfn[i] + size[i], arr[i], -1);
	}

	public static int query(int x, int y, int k) {
		int lca = lca(x, y);
		int lcafa = stjump[lca][0];
		int num = deep[x] + deep[y] - deep[lca] - deep[lcafa];
		if (num < k) {
			return -1;
		}
		cntpos = cntpre = 0;
		for (int i = dfn[x]; i > 0; i -= lowbit(i)) {
			pos[++cntpos] = root[i];
		}
		for (int i = dfn[y]; i > 0; i -= lowbit(i)) {
			pos[++cntpos] = root[i];
		}
		for (int i = dfn[lca]; i > 0; i -= lowbit(i)) {
			pre[++cntpre] = root[i];
		}
		for (int i = dfn[lcafa]; i > 0; i -= lowbit(i)) {
			pre[++cntpre] = root[i];
		}
		return queryNumber(num - k + 1, 1, s);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		for (int i = 1; i <= m; i++) {
			in.nextToken();
			ques[i][0] = (int) in.nval;
			in.nextToken();
			ques[i][1] = (int) in.nval;
			in.nextToken();
			ques[i][2] = (int) in.nval;
		}
		prepare();
		for (int i = 1; i <= m; i++) {
			if (ques[i][0] == 0) {
				change(ques[i][1], ques[i][2]);
			} else {
				int ans = query(ques[i][1], ques[i][2], ques[i][0]);
				if (ans == -1) {
					out.println("invalid request!");
				} else {
					out.println(sorted[ans]);
				}
			}
		}
		out.flush();
		out.close();
		br.close();
	}

}
