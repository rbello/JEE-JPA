/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bookstore.business.bll.catalogmngmt;

import com.bookstore.business.persistence.catalog.Publisher;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * service local de gestion des éditeurs (vue sans interface)
 */
@Stateless(name="PublisherManager")//nom EJB du session bean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@LocalBean
public class PublisherManagerServiceBean{

    @PersistenceContext(unitName="bsPU")
    private EntityManager em;

    /**
     * 
     * Sauvegarder un éditeur
     * @param publisher éditeur nouvellement créé
     * @return éditeur managé par le contexte de persistance
     */
    public Publisher savePublisher(Publisher publisher) {
       // les opérations de persistance modificatrices sont repercutées dans la base
     //lors du commit de la transaction jointe par ce SB
     //càd que le commit s'efectue aprés que la méthode
    //appelante CatalogManager#createPublisher() a retourné.

 //l'instance( référencée par la variable ) publisher est attachée
//au contexte de persistance. publisher est managé
        em.persist(publisher) ;
        return publisher;
    }

    /**
     * 
     * Trouver un éditeur en fonction de son id
     * @param publisherId identité de l'éditeur à retrouver en base
     * @return l'éditeur recherché
     */
    public Publisher findPublisherById(Long publisherId) {
      Publisher publisher = em.find(Publisher.class, publisherId);
      return publisher;
    }
  
}
