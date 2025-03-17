package com.tech.whale.admin.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.tech.whale.admin.dao.AdminIDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMyImgUrl {
	
	private final AdminIDao adminIDao;
	
	// 세션에 저장된 아이디 이미지 유알엘
	public String myImgSty(Model model) {
		Map<String, Object> map = model.asMap();
		String myId = (String)map.get("user_id");
		String myImgSty = adminIDao.myImg(myId);
		return myImgSty;
	}
}
