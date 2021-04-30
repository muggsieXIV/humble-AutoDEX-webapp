/**
 * 
 */
package com.safelogic.autodex.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safelogic.autodex.web.dao.NaasRepository;
import com.safelogic.autodex.web.model.AppConfig;

@Service
@Transactional
public class AppConfigServiceImpl implements AppConfigService {
	
	private NaasRepository<AppConfig> appConfigRepo;
	
	public List<AppConfig> getAllAppConfig() {
		return appConfigRepo.findAll();
	}

	@Autowired
	@Qualifier("naasRepository")
	public void setAppConfigRepo(NaasRepository<AppConfig> appConfigRepo) {
		this.appConfigRepo = appConfigRepo;
		this.appConfigRepo.setType(AppConfig.class);
	}
}
