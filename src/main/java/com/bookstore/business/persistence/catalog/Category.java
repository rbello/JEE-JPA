/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bookstore.business.persistence.catalog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 *
 * Entité Category mappée sur la table CATEGORIES<br>
 * Spécifie des requêtes nommées : <br>
 * 1 requête permet de retrouvée les catégories enfants en fonction d'une catégorie parent<br>
 * 1 requête permet de retrouver les catégories racines / sans parents
 */
@Entity
@Table(name="categories")
@NamedQueries({
@NamedQuery(name="retrieveRootCategories", query="SELECT c from Category c where c.parentCategory is null"),
@NamedQuery(name="retrieveChildrenCategories",query="SELECT c from Category c where c.parentCategory=:parent")
})
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_CATEGORIE")
    private Long id;

    @Column(name="TITRE")
    private String title;
    
    private String description;
   
    @ManyToOne
    @JoinColumn(name="CAT_ID_CATEGORIE", referencedColumnName="ID_CATEGORIE")
    private Category parentCategory;
    
    //dans le Workshop, on utilise les modes fetch par défaut
    //pour les relations plusieurs à plusieurs, LAZY est le mode par défaut.
    //ici l'attribut fetch de l'annotation est indiqué à titre d'exemple.
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="appartient",
    joinColumns=@JoinColumn(name="ID_CATEGORIE",referencedColumnName="ID_CATEGORIE"),
    inverseJoinColumns=@JoinColumn(name="ID_LIVRE", referencedColumnName="ID_LIVRE")
    )
    private List<Book> books= new ArrayList<>();
    
    
   public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
   
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book){
        books.add(book);
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.Category[id=" + id + "]";
    }

}
