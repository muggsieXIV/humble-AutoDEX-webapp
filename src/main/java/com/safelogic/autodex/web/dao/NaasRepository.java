package com.safelogic.autodex.web.dao;

import java.util.List;

public interface NaasRepository<NaasEntity>{
	public NaasEntity create(NaasEntity naasEntity);
    public NaasEntity read(long naasEntityId);
    public NaasEntity update(NaasEntity naasEntity);
    public NaasEntity save(NaasEntity naasEntity,Long id);
    public boolean delete(NaasEntity naasEntity);
    public NaasEntity findById(long id,Long userId);
    public NaasEntity findByName(String name);// these can return multiple..return type should be list ?
    public List<NaasEntity> findAll();
    public List<NaasEntity> findAllByUserId(long userId);
    public List<NaasEntity> findByAttribute(String attributeName, Object attributeValue);//return type should be list ?
    
    public void setType(Class<NaasEntity> type);
	public boolean deleteById(long naasEntityId, Long userId);
}