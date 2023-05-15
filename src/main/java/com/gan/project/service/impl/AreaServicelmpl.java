package com.gan.project.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gan.project.service.AreaService;
import com.gan.project.dao.AreaDao;
import com.gan.project.entity.Area;

@Service
public class AreaServicelmpl implements AreaService {

	@Autowired
	private AreaDao areaDao;
	
	@Override
	public List<Area> getAreaList(){
		
		return areaDao.queryArea();
		
	}
}
