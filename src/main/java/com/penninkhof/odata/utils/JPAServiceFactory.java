package com.penninkhof.odata.utils;

import javax.persistence.EntityManagerFactory;

import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPAServiceFactory extends ODataJPAServiceFactory {
	private static final Logger log = LoggerFactory.getLogger(JPAServiceFactory.class);
	public static final String DEFAULT_ENTITY_UNIT_NAME = "Model";
	public static final String ENTITY_MANAGER_FACTORY_ID = "entityManagerFactory";

	@Override
	public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {
		ODataJPAContext oDataJPAContext = getODataJPAContext();

		EntityManagerFactory factory = (EntityManagerFactory) SpringContextsUtil.getBean(ENTITY_MANAGER_FACTORY_ID);
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) factory.unwrap(SessionFactory.class);
		Dialect dialect = sessionFactory.getJdbcServices().getDialect();
		log.info(dialect.toString()); // <-- Here it will print the dialect name
		oDataJPAContext.setEntityManagerFactory(factory);
		oDataJPAContext.setPersistenceUnitName(DEFAULT_ENTITY_UNIT_NAME);
		oDataJPAContext.setJPAEdmExtension(new JPAEdmExtension());

		ODataContextUtil.setODataContext(oDataJPAContext.getODataContext());

		return oDataJPAContext;
	}
}