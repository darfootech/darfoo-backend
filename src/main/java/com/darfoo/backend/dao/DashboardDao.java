package com.darfoo.backend.dao;

import com.darfoo.backend.model.Dashboard;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zjh on 15-2-8.
 */

@Component
@SuppressWarnings("unchecked")
public class DashboardDao {
    @Autowired
    SessionFactory sessionFactory;

    public boolean isNoTriggerThere() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Dashboard.class);
        if (criteria.list().size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void insertTrigger(int status) {
        Session session = sessionFactory.getCurrentSession();
        Dashboard trigger = new Dashboard();
        trigger.setStatus(status);
        session.save(trigger);
    }

    public void updateTrigger(int status) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Dashboard.class);
        Dashboard dashboard = (Dashboard) criteria.list().get(0);
        dashboard.setStatus(status);
        session.save(dashboard);
    }

    public boolean isDashboardOpen() {
        if (isNoTriggerThere()) {
            return false;
        } else {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Dashboard.class);
            Dashboard dashboard = (Dashboard) criteria.list().get(0);
            if (dashboard.getStatus() == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isDashboardClose() {
        if (isNoTriggerThere()) {
            return true;
        } else {
            Session session = sessionFactory.getCurrentSession();
            Criteria criteria = session.createCriteria(Dashboard.class);
            Dashboard dashboard = (Dashboard) criteria.list().get(0);
            if (dashboard.getStatus() == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void openDashBoard() {
        if (isNoTriggerThere()) {
            insertTrigger(1);
        } else {
            updateTrigger(1);
        }
    }

    public void closeDashBoard() {
        if (isNoTriggerThere()) {
            insertTrigger(0);
        } else {
            updateTrigger(0);
        }
    }
}
