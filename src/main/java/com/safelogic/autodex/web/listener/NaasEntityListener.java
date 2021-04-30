package com.safelogic.autodex.web.listener;

import java.util.Date;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.safelogic.autodex.web.model.NaasEntity;
import com.safelogic.autodex.web.util.NaasRestUtil;

public class NaasEntityListener {
	
	Logger fileLogger = LoggerFactory.getLogger("fileLogger");
	NaasRestUtil naasAppUtil=new NaasRestUtil();
	
	@PrePersist
	public void prePersist(NaasEntity naasEntity){
		
		String loginUserId=naasAppUtil.getCurrentUserName();
		if( ! StringUtils.isBlank(loginUserId))
		{
			naasEntity.setCreatedByUser(loginUserId);
			naasEntity.setCreateDate(new Date());
			naasEntity.setModifiedByUser(loginUserId);
			naasEntity.setModifiedDate(new Date());
		}
	}
	
	@PostPersist
	public void postPersist(NaasEntity naasEntity){
		
	}
	
	
	@PreUpdate
	public void preUpdate(NaasEntity naasEntity){
		String loginUserId=naasAppUtil.getCurrentUserName();
		if( ! StringUtils.isBlank(loginUserId))
		{
			naasEntity.setModifiedByUser(loginUserId);
			naasEntity.setModifiedDate(new Date());
		}
	}
}
