package com.gan.project.web.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gan.project.entity.HeadLine;
import com.gan.project.entity.ShopCategory;
import com.gan.project.enums.HeadLineStateEnum;
import com.gan.project.enums.ShopCategoryStateEnum;
import com.gan.project.service.HeadLineService;
import com.gan.project.service.ShopCategoryService;

@Controller
@RequestMapping("/frontend")
public class MainPageController {
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private HeadLineService headLineService;

	@RequestMapping(value = "/listmainpageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> list1stShopCategory() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		try {
			shopCategoryList = shopCategoryService
					.getFirstLevelShopCategoryList();
			modelMap.put("shopCategoryList", shopCategoryList);
		} catch (Exception e) {
			e.printStackTrace();
			ShopCategoryStateEnum s = ShopCategoryStateEnum.INNER_ERROR;
			modelMap.put("success", false);
			modelMap.put("errMsg", s.getStateInfo());
			return modelMap;
		}
		List<HeadLine> headLineList = new ArrayList<HeadLine>();
		try {
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		} catch (Exception e) {
			e.printStackTrace();
			HeadLineStateEnum s = HeadLineStateEnum.INNER_ERROR;
			modelMap.put("success", false);
			modelMap.put("errMsg", s.getStateInfo());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}

}
