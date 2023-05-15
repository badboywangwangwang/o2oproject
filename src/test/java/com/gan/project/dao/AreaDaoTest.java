package com.gan.project.dao;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.gan.project.BaseTest;
import com.gan.project.entity.Area;

public class AreaDaoTest extends BaseTest {
	@Autowired
	private AreaDao areaDao;



	@Test
	public void testBQueryArea() throws Exception {
		List<Area> areaList = areaDao.queryArea();
		assertEquals(3, areaList.size());
	}


}
