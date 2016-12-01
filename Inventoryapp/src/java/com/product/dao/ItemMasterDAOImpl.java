package com.product.dao;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import com.product.model.ItemMaster;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Transactional
public class ItemMasterDAOImpl implements ItemMasterDAO{
       @Autowired
       private SessionFactory sessionFactory;
       private static final ThreadLocal<Session> threadLocal = new ThreadLocal();
	 
    public ItemMasterDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

@Override
public void add(ItemMaster item) {
    Session session = sessionFactory.getCurrentSession();
    try {
        session.beginTransaction();
        session.save(item);
      } catch (HibernateException e) {
          e.printStackTrace();
          session.getTransaction().rollback();
    }
        session.getTransaction().commit();
}

@Override
public void update(ItemMaster item) {

    Session session = sessionFactory.getCurrentSession();
    try {
        System.out.println("IN Update");
        session.beginTransaction();
        session.saveOrUpdate(item);
        } catch (HibernateException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    session.getTransaction().commit();
}

@Override
public ItemMaster getItem(Long id) {
    Session session = sessionFactory.getCurrentSession();
    ItemMaster item=null;
    try {
        System.out.println("IN GetIteam");
        session.beginTransaction();
        item = (ItemMaster) session.get(ItemMaster.class, id);
    } catch (HibernateException e) {
        e.printStackTrace();
        session.getTransaction().rollback();
    }
    session.getTransaction().commit();
    return item;
}

@Override
public void delete(Long id) {
    Session session = sessionFactory.getCurrentSession();
    ItemMaster item=null;
    try {
    session.beginTransaction();
    item = (ItemMaster) session.get(ItemMaster.class, id);
    if(null != item) {
        session.delete(item);
    }
    session.getTransaction().commit();
    }finally{
        System.out.println("closed abnomally");
    }
    
}

@Override
public List<ItemMaster> list() {

    Session session = sessionFactory.openSession();
    System.out.println("IN List");
    List<ItemMaster> items = null;
    try {
    final Transaction t=session.getTransaction();
    t.begin();
    
    //session.beginTransaction();    
    try {
        System.out.println("IN LIST");
        items = (List<ItemMaster>)session.createQuery("select * from com.product.model.ItemMaster").list();

    } catch (HibernateException e) {
        e.printStackTrace();
        session.getTransaction().rollback();
    }
    session.getTransaction().commit();
    }finally{
        closeSession();
        System.out.println("closed abnomally");
    }
    return items;
}

 public static void closeSession() throws HibernateException {
    Session session = (Session) threadLocal.get();
    threadLocal.set(null);

    if (session != null) {
      session.close();
    }
  }

}
