public class Test {

	public int[] merge(int[] ns, int start, int end) {
		int mid = (start + end) / 2;
		if (start == end) {
			return ns;
		} else {
			int[] starts = merge(ns, start, mid);
			int[] ends = merge(ns, mid + 1, end);
			return mergeSort(starts, ends);
		}
	}

	public int[] mergeSort(int[] ns1, int[] ns2) {
		int[] is = new int[ns1.length + ns2.length];
		int z = 0, i = 0, j = 0;
		while (i < ns1.length && j < ns2.length) {
			if (ns1[i] < ns2[j]) {
				is[z] = ns1[i];
				i++;
			} else {
				is[z] = ns2[j];
				j++;
			}
			z++;
		}
		for (; i < ns1.length; i++) {
			is[z] = ns1[i];
			z++;
		}
		for (; j < ns2.length; j++) {
			is[z] = ns2[j];
			z++;
		}
		return is;
	}

	public static void main(String args[]) throws Exception {
		int[] ns = new int[] { 1, 4, 5, 7, 9, 3, 12 };
		ns = new Test().merge(ns, 0, ns.length - 1);
		for (int i = 0; i < ns.length; i++) {
			System.out.print(ns[i] + " ");
		}
	}
}
