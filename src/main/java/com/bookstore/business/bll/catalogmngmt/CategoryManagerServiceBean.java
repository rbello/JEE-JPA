/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bookstore.business.bll.catalogmngmt;


import com.bookstore.business.persistence.catalog.Category;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * service local de gestion des catégories exposé via une vue sans interface <br>
 * mêmes remarques que pour BookManager<br>
 * Service local ayant une granularité fine.
 * Les méthodes s'exécutent au sein d'une tsx active propagée / créée "par" le client appelant.
 * Les méthodes doivent obligatoirement s'exécuter dans une tsx active cliente.
 */
@Stateless(name="CategoryManager")//nom du stateless 
//toutes les méthodes doivent s'éxecuter dans une transaction parente
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@LocalBean
public class CategoryManagerServiceBean{

    //injection d'une instance EntityManager dans la variable em
    @PersistenceContext(unitName="bsPU")
    private EntityManager em;
    

    /**
     * sauvegarder une catégorie dans la base
     * @param category la catégorie nouvellement créée à persister dans la base
     * @return identité de la catégorie persistée
     */
    public Long saveCategory(Category category) {
     em.persist(category);
     em.flush();//la requête d'insertion s'exécute sans attendre la fin de la transaction
     em.refresh(category);//l'entité category est rafraîchie avec les données de la base. l'id générée lors de l'insertion est récupérée
     Long catId = category.getId();
     return catId;
    }
   
    /**
     * trouver une catégorie en fonction de son identité / sa clé primaire.
     * Comportement transactionnel redéfini (SUPPORTS) :Méthode pouvant s'exécuter dans le contexte transactionnel de l'appelant.
     * @param categoryId identité de la catégorie recherchée
     * @return la catégorie trouvée en base
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)//comportement transactionnel redéfini
    public Category findCategoryById(Long categoryId){
        if(categoryId ==null)return null; //retourne null si l'id passé est null
        Category category = em.find(Category.class, categoryId);//category attachée au CP(contexte de persistance)
        return category;
    }

    /**
     *
     * Lister les catégories racines
     * @return la liste de catégories n'ayant pas de parents.
     */
    public List<Category> getRootCategories() {
        TypedQuery<Category> query = em.createNamedQuery("retrieveRootCategories",Category.class);//instance de requête nommée typée
        List<Category> roots = query.getResultList();
        return roots;

    }

   /**
    *
    * Lister des catégories enfants
    * @param parentId identifiant de la catégorie pour laquelle on recherche les catégories filles
    * @return la liste des catégorie filles d'une catégorie
    * retourne null si aucune catégorie enfant n'est  retrouvée en fonction de parentId
    */
    public List<Category> getchildrenCategories(Long parentId) {
        Category parent = em.find(Category.class,parentId);
        if(parent!=null){
        //ici on utilise une requête non typée
        Query query = em.createNamedQuery("retrieveChildrenCategories");
        query.setParameter("parent", parent);//assignation de paramètres
        List children = query.getResultList();//la liste retournée n'est pas type-safe        
        return children;
        
        }else {
            return null;
        }
    }

}
