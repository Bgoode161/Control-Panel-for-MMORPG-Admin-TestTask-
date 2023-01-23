
package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class PlayerCriteriaRepository {
    private EntityManager entityManager;


    public PlayerCriteriaRepository(LocalContainerEntityManagerFactoryBean entityManager) {
        this.entityManager = entityManager.getNativeEntityManagerFactory().createEntityManager();
    }

    public List<Player> findAllWithFilter(String name, String title,
                                          Race race, Profession profession,
                                          Long after, Long before, Boolean banned,
                                          Integer minExp, Integer maxExp,
                                          Integer minLevel, Integer maxLevel) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Player> cq = criteriaBuilder.createQuery(Player.class);
        Root<Player> root = cq.from(Player.class);


        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }
        if (title != null) {
            predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
        }
        if (race != null) {
            predicates.add(criteriaBuilder.equal(root.get("race"), race));
        }
        if (profession != null) {
            predicates.add(criteriaBuilder.equal(root.get("profession"), profession));
        }
        if (after != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after)));
        }
        if (before != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), new Date(before)));
        }
        if (banned != null) {
            predicates.add(criteriaBuilder.equal(root.get("banned"), banned));
        }
        if (minExp != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExp));
        }
        if (maxExp != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExp));
        }
        if (minLevel != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("level"), minLevel));
        }
        if (maxLevel != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLevel));
        }


        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Player> typedQuery = entityManager.createQuery(cq);
        return typedQuery.getResultList();
    }

}