package edu.vt.crest.hr.services;

import java.util.List;
import java.util.Set;

import javax.ejb.NoSuchEntityException;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import edu.vt.crest.hr.entity.DepartmentEntity;
import edu.vt.crest.hr.entity.EmployeeEntity;

/**
 * Implements a DepartmentService
 */
@ApplicationScoped
public class DepartmentServiceBean implements DepartmentService {

  @PersistenceContext
  private EntityManager em;

  /**
   * {@inheritDoc}
   */
  @Override
  public DepartmentEntity createDepartment(DepartmentEntity department) {
	  if (department.getEmployees() != null) {
          Set<EmployeeEntity> employees=department.getEmployees();
          for (EmployeeEntity employeeEntity : employees) {
			em.persist(employeeEntity);
		}
      }
      em.persist(department);
      return department;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DepartmentEntity findById(Long id) {
	  Query query = em.createNamedQuery("DepartmentEntity.findById", DepartmentEntity.class);
      query.setParameter("departmentid", id);
      return (DepartmentEntity) query.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DepartmentEntity> listAll(Integer startPosition, Integer maxResult) {
	  Query query = em.createNamedQuery("DepartmentEntity.AllDepartments", DepartmentEntity.class);
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
  public DepartmentEntity update(Long id, DepartmentEntity department) throws OptimisticLockException {
	  DepartmentEntity departmentData = em.find(DepartmentEntity.class, id, LockModeType.OPTIMISTIC);
      if (departmentData == null)
    	  throw new NoSuchEntityException("Cannot find the Department Entity to Update");
	  
	  em.merge(department);
      return department;     
  }

}
