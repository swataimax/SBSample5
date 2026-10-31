package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) // JUnit5でMockito使うには必要
class MockSampleTest {
	
	@Mock // モック（スタブ）に置き換えたいインスタンス
	private MockMethod method;
	
	@InjectMocks // @Mockでモックにしたインスタンスを差し込むクラス
	private MockSample sample;
	
	@Test
	public void test() {
		Mockito.when(this.method.getArray()) // モックを呼び出したとき
				.thenReturn(new int[] { 10, 20, 30, 40 }); //このデータを返す

		// 検証
		assertEquals(this.sample.sum(), 100);
	}
}