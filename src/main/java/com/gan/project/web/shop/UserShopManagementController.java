package com.gan.project.web.shop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gan.project.dto.UserShopMapExecution;
import com.gan.project.entity.Shop;
import com.gan.project.entity.UserShopMap;
import com.gan.project.service.UserShopMapService;
import com.gan.project.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shop")
public class UserShopManagementController {
	@Autowired
	private UserShopMapService userShopMapService;

	@RequestMapping(value = "/listusershopmapsbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listUserShopMapsByShop(
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		Shop currentShop = (Shop) request.getSession().getAttribute(
				"currentShop");
		if ((pageIndex > -1) && (pageSize > -1) && (currentShop != null)
				&& (currentShop.getShopId() != null)) {
			UserShopMap userShopMapCondition = new UserShopMap();
			userShopMapCondition.setShopId(currentShop.getShopId());
			String userName = HttpServletRequestUtil.getString(request,
					"userName");
			if (userName != null) {
				userShopMapCondition.setUserName(userName);
			}
			UserShopMapExecution ue = userShopMapService.listUserShopMap(
					userShopMapCondition, pageIndex, pageSize);
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
