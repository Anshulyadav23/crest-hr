package edu.vt.crest.hr.services;

import java.util.List;
import javax.ejb.NoSuchEntityException;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import edu.vt.crest.hr.entity.EmployeeEntity;

/**
 * Implements an EmployeeService
 */
@ApplicationScoped
public class EmployeeServiceBean implements EmployeeService {

  @PersistenceContext
  private EntityManager em;

  /**
   * {@inheritDoc}
   */
  @Override
  public EmployeeEntity createEmployee(EmployeeEntity employee) {
	  if(employee!=null)
	  {
		  em.persist(employee);
		  return employee;
	  }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmployeeEntity findById(Long id) {
	  Query query = em.createNamedQuery("EmployeeEntity.findById", EmployeeEntity.class);
      query.setParameter("employeeid", id);
      return (EmployeeEntity) query.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EmployeeEntity> listAll(Integer startPosition, Integer maxResult) {
	  Query query = em.createNamedQuery("EmployeeEntity.AllEmployees", EmployeeEntity.class);
	  if (startPosition != null) {
          query.setFirstResult(startPosition);
      }
	  else if (maxResult != null) {
          query.setMaxResults(maxResult);
      }
      
      return query.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmployeeEntity update(Long id, EmployeeEntity employee) throws OptimisticLockException {
	  EmployeeEntity employeeData = em.find(EmployeeEntity.class, id, LockModeType.OPTIMISTIC);
      if (employeeData == null)
    	  throw new NoSuchEntityException("Cannot find the Employee Entity to Update");
	  
	  em.merge(employee);
      return employee;     
      
     

  }
}
