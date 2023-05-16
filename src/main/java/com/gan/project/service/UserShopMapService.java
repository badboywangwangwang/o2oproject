package com.gan.project.service;

import com.gan.project.dto.UserShopMapExecution;
import com.gan.project.entity.UserShopMap;

public interface UserShopMapService {

	UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition,
			int pageIndex, int pageSize);

}
