package com.gan.project.dao;

import java.util.List;

import com.gan.project.entity.Area;

public interface AreaDao {

	/**
	 * 列出区域列表
	 * @return
	 */
	 List<Area> queryArea();
	
	
}
