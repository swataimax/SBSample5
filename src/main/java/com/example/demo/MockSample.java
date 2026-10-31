package com.example.demo;

public class MockSample {
	private MockMethod method = new MockMethod();

	public int sum() {
		int sum = 0;
		// メソッド呼び出しで配列を取得
		for (int i : method.getArray()) {
			sum += i;
		}
		return sum;
	}
}
