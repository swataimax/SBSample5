package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DiaryControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	@DisplayName("ケース1")
	void topテスト() throws Exception {

		DiaryEntity entity = new DiaryEntity();
		entity.setId(1);
		entity.setDate(LocalDate.of(2026, 1, 1));
		entity.setTitle("旅行");
		entity.setMemo("楽しかった");
		List<DiaryEntity> list = List.of(entity);
		// 想定値
		mockMvc.perform(get("/top")) 						// リクエストメソッド、リクエストパス
				.andExpect(status().isOk())					// HTTPステータスが200(正常終了)か比較検証	ステータスコードのテスト
				.andExpect(model().attribute("list", list))	// findAllメソッド取得結果値と比較検証		Modelのテスト
				.andExpect(view().name("top"));				// 遷移先テンプレート名が「top」か比較検証	Controllerが返却するビューのテスト
		
//		mockMvc.perform(get("/top")) 						// リクエストメソッド、リクエストパス
//				.andExpect(status().isOk()) 				// HTTPステータスが200(正常終了)か比較検証
//				.andExpect(model().attribute("list", getEntityList())) // findAllメソッド取得結果値と比較検証
//				.andExpect(view().name("top")); 			// 遷移先テンプレート名が「top」か比較検証
	}
	
//	private List<DiaryEntity> getEntityList() {
//		DiaryEntity entity = new DiaryEntity();
//		entity.setId(1);
//		entity.setDate(LocalDate.of(2026, 1, 1));
//		entity.setTitle("旅行");
//		entity.setMemo("楽しかった");
//		
//		DiaryEntity entity2 = new DiaryEntity();
//		entity2.setId(302);
//		entity2.setDate(LocalDate.of(2026, 6, 2));
//		entity2.setTitle("仕事");
//		entity2.setMemo("大変だった");
//		
//		return List.of(entity, entity2);
//	}

	@Test
	@DisplayName("ケース2")
	void writeテスト() throws Exception {
		mockMvc.perform(get("/write"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("form", new DiaryEntity()))	// Modelに含まれる属性値を検証
				.andExpect(view().name("write"));
	}

	@Test
	@DisplayName("ケース3")
	void insertテスト_バリデーションエラーあり_書き込み() throws Exception {
		// Entityにnullを設定
		DiaryEntity entity = new DiaryEntity();
		entity.setId(null);
		entity.setDate(null);
		mockMvc.perform(post("/insert").flashAttr("form", entity))	// オブジェクトを渡す
				.andExpect(status().isOk())									// HTTPステータスコード200：成功
				.andExpect(model().hasErrors())								// Modelにエラーがあるか検証
				.andExpect(model().attributeHasFieldErrorCode("form", "date", "NotNull"))
																			// Modelにエラーがあるか検証
				.andExpect(view().name("write"));
	}
	
	
	// @ModelAttribute	→flashAttrメソッド	を使う
	// @RequestParam	→paramメソッド		を使う
	
	
	@Test
	@DisplayName("ケース4")
	void insertテスト_バリデーションエラーあり_更新() throws Exception {
		// EntityにIDを設定（ID以外の設定は不要）
		DiaryEntity entity = new DiaryEntity();
		entity.setId(1);
		entity.setTitle(null);
		mockMvc.perform(post("/insert").flashAttr("form", entity))	// オブジェクトを渡す
				.andExpect(status().isOk())
//				.andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrorCode("form", "title", "NotBlank"))
				.andExpect(view().name("update"));
	}

	@Test
	@DisplayName("ケース5")
	void insertテスト_バリデーションエラーなし() throws Exception {
		// Entityを設定
		DiaryEntity entity = new DiaryEntity();
		entity.setDate(LocalDate.of(2026, 1, 1));
		entity.setTitle("旅行");
		entity.setMemo("楽しかった");
		mockMvc.perform(post("/insert").flashAttr("form", entity))
				.andExpect(status().isFound())								// HTTPステータスコード302：リダイレクト成功
				.andExpect(view().name("redirect:/top"));
	}

	@Test
	@DisplayName("ケース6")
	void detailテスト() throws Exception {
		// Entityを設定
		DiaryEntity entity = new DiaryEntity();
		entity.setId(1);
		entity.setDate(LocalDate.of(2026, 1, 1));
		entity.setTitle("旅行");
		entity.setMemo("楽しかった");
		// 想定値
		mockMvc.perform(get("/detail/{id}", 1)) // {id}で1を渡す
				.andExpect(status().isOk())
				.andExpect(model().attribute("form", entity))
				.andExpect(view().name("detail"));
	}

	@Test
	@DisplayName("ケース7")
	void updateテスト() throws Exception {
		// Entityを設定
		DiaryEntity entity = new DiaryEntity();
		entity.setId(1);
		entity.setDate(LocalDate.of(2026, 1, 1));
		entity.setTitle("旅行");
		entity.setMemo("楽しかった");
		// 想定値
		mockMvc.perform(get("/update/{id}", 1)) // {id}で1を渡す
				.andExpect(status().isOk())
				.andExpect(model().attribute("form", entity))
				.andExpect(view().name("update"));
	}

	@Test
	@DisplayName("ケース8")
	void deleteテスト() throws Exception {
		// Entityを設定
		DiaryEntity entity = new DiaryEntity();
		entity.setId(1);
		entity.setDate(LocalDate.of(2026, 1, 1));
		entity.setTitle("旅行");
		entity.setMemo("楽しかった");
		// 想定値
		mockMvc.perform(get("/delete/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(model().attribute("form", entity))
				.andExpect(view().name("delete"));
	}

	@Test
	@DisplayName("ケース9")
	void removeテスト() throws Exception {
		// 想定値
		mockMvc.perform(post("/remove").param("id", "1"))
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/top"));
		
//		// Entityを設定
//		DiaryEntity entity = new DiaryEntity();
//		entity.setId(1);
//		// 想定値
//		mockMvc.perform(post("/remove").flashAttr("form", entity))
//		.andExpect(status().isFound())
//		.andExpect(view().name("redirect:/top"));
	}

	@Test
	@DisplayName("ケース10")
	void searchテスト() throws Exception {
		// Entityを設定
		DiaryEntity entity = new DiaryEntity();
		entity.setId(1);
		entity.setDate(LocalDate.of(2026, 1, 1));
		entity.setTitle("旅行");
		entity.setMemo("楽しかった");
		List<DiaryEntity> list = List.of(entity);
		// 想定値
		mockMvc.perform(get("/search").param("keyword", "旅行"))	// 文字列のパラメータを渡す場合はparamメソッド
				.andExpect(status().isOk())
				.andExpect(model().attribute("list", list))
				.andExpect(view().name("top"));
	}

}