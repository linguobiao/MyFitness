package com.lgb.myfitness.audio;

public class FFT {

	public double dataR[] = new double[8];
	public double dataI[] = new double[8];
	int n, m;

	// Lookup tables. Only need to recompute when size of FFT changes.
	double[] cos;
	double[] sin;

	public FFT(int n) {
		this.n = n;
		this.m = (int) (Math.log(n) / Math.log(2));

		// Make sure n is a power of 2
		if (n != (1 << m))
			throw new RuntimeException("FFT length must be power of 2");

		// precompute tables
		cos = new double[n / 2];
		sin = new double[n / 2];

		for (int i = 0; i < n / 2; i++) {
			cos[i] = Math.cos(-2 * Math.PI * i / n);
			sin[i] = Math.sin(-2 * Math.PI * i / n);
		}

	}

	public void fft() {
		int i, j, k, n1, n2, a;
		double c, s, t1, t2;

		// Bit-reverse
		j = 0;
		n2 = n / 2;
		for (i = 1; i < n - 1; i++) {
			n1 = n2;
			while (j >= n1) {
				j = j - n1;
				n1 = n1 / 2;
			}
			j = j + n1;

			if (i < j) {
				t1 = dataR[i];
				dataR[i] = dataR[j];
				dataR[j] = t1;
				t1 = dataI[i];
				dataI[i] = dataI[j];
				dataI[j] = t1;
			}
		}

		// FFT
		n1 = 0;
		n2 = 1;

		for (i = 0; i < m; i++) {
			n1 = n2;
			n2 = n2 + n2;
			a = 0;

			for (j = 0; j < n1; j++) {
				c = cos[a];
				s = sin[a];
				a += 1 << (m - i - 1);

				for (k = j; k < n; k = k + n2) {
					t1 = c * dataR[k + n1] - s * dataI[k + n1];
					t2 = s * dataR[k + n1] + c * dataI[k + n1];
					dataR[k + n1] = dataR[k] - t1;
					dataI[k + n1] = dataI[k] - t2;
					dataR[k] = dataR[k] + t1;
					dataI[k] = dataI[k] + t2;
				}
			}
		}
	}

}
