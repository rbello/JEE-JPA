/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bookstore.business.persistence.catalog;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;


/**
 *
 * Entité Book mappée sur LIVRES<br>
 * la date de parution utilise l'annotation @Temporal(javax.persistence.TemporalType.DATE)
 */
@Entity
@Table(name="livres")
@NamedQuery(name="retrieveAllBooks", query="SELECT b FROM Book b")//non utilsée dans le WS
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_LIVRE")
    private Long id;                        
                                           
    @Column(name="TITRE")
    private String title;

    @Column(name="DATE_PARUTION")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    
    @Column(name="RESUME_LIVRE")
    private String summary;
    
//    @JoinColumn :
//    l'attribut name mappe le nom de la clé étrangère de la table livres sous-jacente
//    à l'entity.
//    l'attribut referencedColumnName correspond au nom de colonne référencée par la colonne
//    clé étrangère. Comme c'est la colonne clé primaire qui est référencée, referencedColumnName est optionnel.
    
    @ManyToOne(cascade=CascadeType.MERGE)
    @JoinColumn(name="ID_EDITEUR", referencedColumnName="ID_EDITEUR")
    private Publisher publisher;

    //la variable books de Category est propriétaire de la relation
    @ManyToMany(mappedBy="books")//cette annotation ne possède pas l'attribut optional
    private List<Category> categories = new ArrayList<>();

    public Book() {
    }
    
    public Long getId() {
        return id;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addCategory(Category category){
        categories.add(category);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        //2 livres sont considérés comme égaux si leur id est identique
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.Book[id=" + id + "]";
    }

}
