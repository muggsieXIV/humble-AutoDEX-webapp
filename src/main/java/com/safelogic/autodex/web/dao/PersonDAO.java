package com.safelogic.autodex.web.dao;

import com.safelogic.autodex.web.model.Contact;

public interface PersonDAO extends NaasRepository<Contact>{
	/*public int getPersonCount(long groupId);
	public List<Person> getCustomersForAccountHolder(AccountHolder ach);
	public List<Person> getCustomers(long groupId);	
	public ReportRsTO getCustomers(String dynamicMetaDataSelectQuery,String sqlWhereClause);
	public long getCustomerCount(String sqlWhereClause);
	public List<Object[]> getCustomersWithNotificationCount(AccountHolder ach);
	
	public ReportRsTO getCustomersForNotification(long achId,long notificationId,String dynamicMetaDataSelectQuery);
	public List<CustomerBasicInfo> findAllCustomerByAchId(long achId);
	//boolean createList(long achId, List<CustomerBasicInfo> entityList, List<CustomerMetaData> customerMetaDataList) throws Exception;
	//int findAllCustomerCountByAchId(long achId);
	public Customer getCustomer(long customerId, long achId);
	public List<String> getAllCustomersMetaData(long achId);
	public void createList(long achId, List<Customer> customerList1) throws Exception;*/
}
