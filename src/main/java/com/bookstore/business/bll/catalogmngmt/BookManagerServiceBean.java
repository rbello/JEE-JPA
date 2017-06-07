
package com.bookstore.business.bll.catalogmngmt;

import com.bookstore.business.persistence.catalog.Book;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;

/**
 * Service local de gestion des livres exposant une vue sans interface<br>
 * Composant basé sur le pattern Session facade.<br>
 * composant fine grained chargé des opérations CRUD relatives à la gestion des livres.<br>
 * accessible qu'en local - qu'au sein de la JVM où ce code s'exécute.<br>
 * Attribut transactionnel MANDATORY spécifié sur la classe : 
 * toutes les méthodes ne spécifiant pas explicitement un autre attribut transactionnel 
 * devront être invoquées dans un contexte transactionnel initié par un appelant.<br>
 */
@Stateless(name="BookManager")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@LocalBean //facultatif
public class BookManagerServiceBean {

    @PersistenceContext(unitName="bsPU")
    EntityManager em;

   @PreDestroy
   void prevent(){
       System.out.println("instance va être détruite");
   }

   /**
    *
    * @param book livre à persister
    * sauvegarder en base l'état d'un livre nouvellement créé
    * @return le livre persisté
    */
    public Book saveBook(Book book){      
       em.persist(book);
       return book; 
       
    }

/**
 * Retourner un livre en fonction d'une identité unique
 * @param bookId id livre recherché
 * @return le livre correspondant à l'id passée en argument
 */
    public Book findBookById(Long bookId) {
//l'instance retournée est attachée car find() est exécutée au sein d'une transaction
        Book book = em.find(Book.class,bookId);
        return book;
    }

 /**
 *
 * supprimer un livre
 * @param book le livre a supprimé. Si book est null, l'opération de suppression n'est pas exécutée
 */
    public void deleteBook(Book book) { 
        if(book!=null){
            
            //Cette méthode est invoquée au sein de la méthode CatalogManager#DeleteBook(long)
            //qui retrouve au préalable le livre en fonction de son id. 
            // L'instance book est donc managée
            // Il n'est donc pas obligatoire d'invoquer merge avant de supprimer le livre.
            //cependant la méthode deleteBook du composant local BookManager pourrait être
            //invoquée par un client qui passe en argument un livre détaché.
            //d'où le fait d'attacher l'argument de la méthode avant d'invoquer sa suppression
            em.remove(em.merge(book));
        }
    }

    /**
     * retourner une liste de livres dont le titre contient l'argument passé en paramètre.
     * Comportement transactionnel redéfini (SUPPORTS)
     * @param pattern motif permettant de retrouver une liste de livres
     * contenant le motif dans le titre
     * @return la liste des livres possédant le motif dans le titre
     */ 
    @TransactionAttribute(TransactionAttributeType.SUPPORTS) //méthode pouvant joindre le contexte transactionnel de l'appelant
    public List<Book> findByCriteria(String pattern) {
        pattern="%"+pattern+"%";//pour retrouver les livres qui contiennent le motif dans leur titre
        String q= "SELECT b From Book b where b.title LIKE :pattern";
        TypedQuery<Book> query = em.createQuery(q,Book.class);
        query.setParameter("pattern", pattern);
        List<Book> books = query.getResultList();
        return books;
     
    }

    /**
     * modifier un livre et synchroniser l'état modifié avec la base.
     * Le comportement transactionnel de la méthode est redéfini. l'attribut transactionnel est REQUIRED.
     * Ainsi l'opération de mise à jour nécessitant un contexte transactionnel peut être invoquée depuis un bean CDI ne s'exécutant pas dans une transaction.
     * @param book livre existant (dans la base) à modifier
     * @return le livre modifié
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Book updateBook(Book book) {
         //ATTENTION
         //l'instance référencée par b est managée . merge retourne une instance
         //managée de l'entité passée en argument . book est toujours détachée
        
        Book b = em.merge(book);// une copie de book étant managée, ses modifications seront repercutées dans la base
                                //on utilise l'attribut cascade=CascadeType.MERGE dans
                               //l'annotation @ManyToOne de Book pour attacher en cascade l'éditeur lié au livre
                               //cet éditeur étant attaché, la modification de son nom  sera répercutée dans la base
        return b;
    }

}
