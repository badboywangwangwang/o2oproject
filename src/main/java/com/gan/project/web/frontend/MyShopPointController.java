package com.gan.project.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gan.project.dto.UserShopMapExecution;
import com.gan.project.entity.UserShopMap;
import com.gan.project.service.UserShopMapService;
import com.gan.project.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class MyShopPointController {
	@Autowired
	private UserShopMapService userShopMapService;

	@RequestMapping(value = "/listusershopmapsbycustomer", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserShopMapsByCustomer(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		Long userId = 1L;
		if ((pageIndex > -1) && (pageSize > -1) && (userId != null)) {
			UserShopMap userShopMapCondition = new UserShopMap();
			userShopMapCondition.setUserId(userId);
			long shopId = HttpServletRequestUtil.getLong(request, "shopId");
			if (shopId > -1) {
				userShopMapCondition.setShopId(shopId);
			}
			UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
			modelMap.put("userShopMapList", ue.getUserShopMapList());
			modelMap.put("count", ue.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
}
