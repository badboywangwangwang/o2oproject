package com.gan.project.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gan.project.cache.JedisUtil;
import com.gan.project.dao.HeadLineDao;
import com.gan.project.dto.HeadLineExecution;
import com.gan.project.entity.HeadLine;
import com.gan.project.enums.HeadLineStateEnum;
import com.gan.project.service.HeadLineService;
import com.gan.project.util.FileUtil;
import com.gan.project.util.ImageUtil;

@Service
public class HeadLineServiceImpl implements HeadLineService {
	@Autowired
	private JedisUtil.Strings jedisStrings;
	@Autowired
	private JedisUtil.Keys jedisKeys;
	@Autowired
	private HeadLineDao headLineDao;
	private static String HLLISTKEY = "headlinelist";

	@Override
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition)
			throws IOException {
		List<HeadLine> headLineList = null;
		ObjectMapper mapper = new ObjectMapper();
		String key = HLLISTKEY;
		if (headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		if (!jedisKeys.exists(key)) {
			headLineList = headLineDao.queryHeadLine(headLineCondition);
			String jsonString = mapper.writeValueAsString(headLineList);
			jedisStrings.set(key, jsonString);
		} else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory()
					.constructParametricType(ArrayList.class, HeadLine.class);
			headLineList = mapper.readValue(jsonString, javaType);
		}
		return headLineList;
	}

	@Override
	@Transactional
	public HeadLineExecution addHeadLine(HeadLine headLine,
			CommonsMultipartFile thumbnail) {
		if (headLine != null) {
			headLine.setCreateTime(new Date());
			headLine.setLastEditTime(new Date());
			if (thumbnail != null) {
				addThumbnail(headLine, thumbnail);
			}
			try {
				int effectedNum = headLineDao.insertHeadLine(headLine);
				if (effectedNum > 0) {
					String prefix = HLLISTKEY;
					Set<String> keySet = jedisKeys.keys(prefix + "*");
					for (String key : keySet) {
						jedisKeys.del(key);
					}
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS,
							headLine);
				} else {
					return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
				}
			} catch (Exception e) {
				throw new RuntimeException("添加区域信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	public HeadLineExecution modifyHeadLine(HeadLine headLine,
			CommonsMultipartFile thumbnail) {
		if (headLine.getLineId() != null && headLine.getLineId() > 0) {
			headLine.setLastEditTime(new Date());
			if (thumbnail != null) {
				HeadLine tempHeadLine = headLineDao.queryHeadLineById(headLine
						.getLineId());
				if (tempHeadLine.getLineImg() != null) {
					FileUtil.deleteFile(tempHeadLine.getLineImg());
				}
				addThumbnail(headLine, thumbnail);
			}
			try {
				int effectedNum = headLineDao.updateHeadLine(headLine);
				if (effectedNum > 0) {
					String prefix = HLLISTKEY;
					Set<String> keySet = jedisKeys.keys(prefix + "*");
					for (String key : keySet) {
						jedisKeys.del(key);
					}
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS,
							headLine);
				} else {
					return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
				}
			} catch (Exception e) {
				throw new RuntimeException("更新头条信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	public HeadLineExecution removeHeadLine(long headLineId) {
		if (headLineId > 0) {
			try {
				HeadLine tempHeadLine = headLineDao
						.queryHeadLineById(headLineId);
				if (tempHeadLine.getLineImg() != null) {
					FileUtil.deleteFile(tempHeadLine.getLineImg());
				}
				int effectedNum = headLineDao.deleteHeadLine(headLineId);
				if (effectedNum > 0) {
					String prefix = HLLISTKEY;
					Set<String> keySet = jedisKeys.keys(prefix + "*");
					for (String key : keySet) {
						jedisKeys.del(key);
					}
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
				} else {
					return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
				}
			} catch (Exception e) {
				throw new RuntimeException("删除头条信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	public HeadLineExecution removeHeadLineList(List<Long> headLineIdList) {
		if (headLineIdList != null && headLineIdList.size() > 0) {
			try {
				List<HeadLine> headLineList = headLineDao
						.queryHeadLineByIds(headLineIdList);
				for (HeadLine headLine : headLineList) {
					if (headLine.getLineImg() != null) {
						FileUtil.deleteFile(headLine.getLineImg());
					}
				}
				int effectedNum = headLineDao
						.batchDeleteHeadLine(headLineIdList);
				if (effectedNum > 0) {
					String prefix = HLLISTKEY;
					Set<String> keySet = jedisKeys.keys(prefix + "*");
					for (String key : keySet) {
						jedisKeys.del(key);
					}
					return new HeadLineExecution(HeadLineStateEnum.SUCCESS);
				} else {
					return new HeadLineExecution(HeadLineStateEnum.INNER_ERROR);
				}
			} catch (Exception e) {
				throw new RuntimeException("删除头条信息失败:" + e.toString());
			}
		} else {
			return new HeadLineExecution(HeadLineStateEnum.EMPTY);
		}
	}

	private void addThumbnail(HeadLine headLine, CommonsMultipartFile thumbnail) {
		String dest = FileUtil.getHeadLineImagePath();
		String thumbnailAddr = ImageUtil.generateNormalImg(thumbnail, dest);
		headLine.setLineImg(thumbnailAddr);
	}

}
