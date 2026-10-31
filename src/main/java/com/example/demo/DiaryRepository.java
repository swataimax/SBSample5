package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<DiaryEntity, Integer> {
	
	// titleカラムを引数のキーワードであいまい検索をする
	public List<DiaryEntity> findByTitleLike(String keyword);

}
