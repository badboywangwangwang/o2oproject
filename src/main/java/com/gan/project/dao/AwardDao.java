package com.gan.project.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gan.project.entity.Award;

public interface AwardDao {
	List<Award> queryAwardList(@Param("awardCondition") Award awardCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

	int queryAwardCount(@Param("awardCondition") Award awardCondition);

	Award queryAwardByAwardId(long awardId);

	int insertAward(Award award);

	int updateAward(Award award);

	int deleteAward(long awardId);
}
