package com.tech.whale.admin.dto;

import java.util.ArrayList;

import com.tech.whale.admin.util.AdminSearchVO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSearchDto<E> {
	
	private String searchKeyword;
	private String searchType;
	private ArrayList<E> list;
	private int ultotRowcnt=0;
	private AdminSearchVO ulsearchVO;

}
