package Maas;

import jakarta.persistence.EntityManager;

import java.lang.reflect.Method;

public class MaasAuthetificationImpl implements MaasAuthentification {
    @Override
    public <T> T login(T t) {
        EntityManager em = JpaEntityManagerFactory.getEntityManager();
        try {
            Class<?> clazz = t.getClass();
            Method getEmailMethod = clazz.getMethod("getEmail");
            Method getPasswordMethod = clazz.getMethod("getPassword");
            String email = (String) getEmailMethod.invoke(t);
            String password = (String) getPasswordMethod.invoke(t);
            String hashedPassword = DigestUtils.md5Hex(password);
            em.getTransaction().begin();
            String query = "SELECT e FROM " + clazz.getSimpleName() + " e WHERE e.email = :email";
            T entity = em.createQuery(query, (Class<T>) clazz)
                    .setParameter("email", email)
                    .getSingleResult();
            em.getTransaction().commit();
            String storedPassword = (String) (getPasswordMethod).invoke(entity);
            if (storedPassword.equals(hashedPassword)) {
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        return null;
    }


    @Override
    public <T> void logout(T t) {

    }

    @Override
    public <T> Object register(T t) {
        return null;
    }
}
