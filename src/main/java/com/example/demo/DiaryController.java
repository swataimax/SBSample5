package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DiaryController {
	
	@Autowired
	DiaryRepository repository;
	
	@GetMapping("/top")
	public String top(Model model) {
		System.out.println("/top");
		List<DiaryEntity> list = repository.findAll();
		model.addAttribute("list", list);
		return "top";
	}
	
	@PostMapping("/top")
	public String top2(Model model) {
		System.out.println("/top post");
		List<DiaryEntity> list = repository.findAll();
		model.addAttribute("list", list);
		return "top";
	}
	
	
	@GetMapping("/write")
	public String write(Model model) {
		model.addAttribute("form", new DiaryEntity());
		return "write";
	}
	
	@PostMapping("/insert")
	@Transactional(readOnly = false)
	public String insert(
			@ModelAttribute("form") @Validated DiaryEntity entity,
			BindingResult result, 
			Model model) {
		if (result.hasErrors()) {
			if (entity.getId() == null) { // IDが未設定の場合
				return "write";
			} else { // IDが設定されている場合
				return "update";
			}
		}
		repository.saveAndFlush(entity);
		return "redirect:/top";
	}
	
	
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Integer id, Model model) {
		Optional<DiaryEntity> data = repository.findById(id);
		model.addAttribute("form", data.get()); // Optionalから取得
		return "detail";
	}
	
	@GetMapping("/update/{id}")
	public String update(
			@PathVariable Integer id, Model model) {
		Optional<DiaryEntity> data = repository.findById(id);
		model.addAttribute("form", data.get());
		return "update";  
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, Model model) {
		Optional<DiaryEntity> data = repository.findById(id);
		model.addAttribute("form", data.get());
		return "delete";  
	}
	
	
	@PostMapping("/remove")
//	@GetMapping("/remove")
	@Transactional(readOnly = false)
	public String remove(
			@ModelAttribute("form") DiaryEntity entity) {
		int id = entity.getId();
		repository.deleteById(id);
//		return "redirect:/top";		// トップ画面のURLは、http://localhost:8080/top となる
		return "forward:/top";		// トップ画面のURLは、http://localhost:8080/remove となる
	}
	
	
	@GetMapping("/search")
	public String search(
			@RequestParam("keyword") String key, Model model) {
		List<DiaryEntity> list = repository.findByTitleLike("%" + key + "%");
		model.addAttribute("list", list);
		return "top";
	}
	
	
}
