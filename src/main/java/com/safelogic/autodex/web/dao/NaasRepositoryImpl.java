package com.safelogic.autodex.web.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("naasRepository")
@Scope(value=BeanDefinition.SCOPE_PROTOTYPE)
public class NaasRepositoryImpl<NaasEntity> implements NaasRepository<NaasEntity> {
	
	@PersistenceContext
    protected EntityManager em;
	
	protected Class<NaasEntity> type;
	
	public NaasRepositoryImpl() {
	}
	
	public NaasEntity create(NaasEntity entity) {
		 em.persist(entity);
		 em.flush(); // to ensure id is generated
		 return entity;
	}

	public NaasEntity read(long naasEntityId) {
		return (NaasEntity) em.find(type,naasEntityId);
	}

	public boolean delete(NaasEntity entity) {
		try{
			em.remove(entity);
		}catch(Exception exp){
			exp.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public NaasEntity update(NaasEntity entity) {
		return em.merge(entity);
	}
	
	
	public List<NaasEntity> findAll() {
		return em.createQuery("select naasEntity from "+type.getName()+" naasEntity", type).getResultList();

	}
	
	public List<NaasEntity> findAllByUserId(long userId) {
		return em.createQuery("select naasEntity from "+type.getName()+" naasEntity where userId = " + userId, type).getResultList();

	}

	public EntityManager getEm() {
		return em;
	}


	public void setEm(EntityManager em) {
		this.em = em;
	}


	public Class<NaasEntity> getType() {
		return type;
	}


	public void setType(Class<NaasEntity> type) {
		this.type = type;
	}

	public List<NaasEntity> findByAttribute(String attributeName, Object attributeValue) {
		TypedQuery<NaasEntity> query = em.createQuery("Select naasEntity from "+type.getName()+" naasEntity where naasEntity."+attributeName+" = :attributeValue", type);
		query.setParameter("attributeValue", attributeValue);
		return query.getResultList();
	}

	public NaasEntity findById(long id, Long userId) {
		TypedQuery<NaasEntity> query = null;
		if (userId != null) {
			query = em.createQuery("Select naasEntity from "+type.getName()+" naasEntity where naasEntity.id = :id and userId=:userId", type);
			query.setParameter("userId", userId);
		}
		else
		{
			query = em.createQuery("Select naasEntity from "+type.getName()+" naasEntity where naasEntity.id = :id", type);
		}
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	public boolean deleteById(long id, Long achId) {
		NaasEntity naasEntity = findById( id,  achId);
		return delete(naasEntity);
		
	}
	

	public NaasEntity findByName(String name) {
		TypedQuery<NaasEntity> query = em.createQuery("Select naasEntity from "+type.getName()+" naasEntity where naasEntity.name = :name", type);
		query.setParameter("name", name);
		return query.getSingleResult();
	}

	@Override
	public NaasEntity save(NaasEntity naasEntity, Long id) {
		if (id == null) {
			 em.persist(naasEntity);
		} else {
			em.merge(naasEntity);
		}
		return naasEntity;
	}

}
