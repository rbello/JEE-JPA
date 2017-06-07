/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bookstore.business.persistence.catalog;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * Entité publisher mappée sur la table éditeur
 */
@Entity
@Table(name="editeurs")
public class Publisher implements Serializable {
    private static final long serialVersionUID = 1L;
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_EDITEUR")
    private Long id;
    
    @Column(name="RAISON_SOCIALE")
    private String name;
    
    @Embedded
    private Address address;
    
    /**
     * l'entité Livre est propriétaire de la relation 1-plusiers bidirectionnelle  entre editeur et livre
     * le côté propriétaire de la relation correspond à la variable publisher de Book
     */
    @OneToMany(mappedBy="publisher")
    private List<Book>books;

    public Publisher() {
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public String getName() {
        return name;
    }

   
    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address adress) {
        this.address = adress;
    }
    
    
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
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
        if (!(object instanceof Publisher)) {
            return false;
        }
        Publisher other = (Publisher) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.Publisher[id=" + id + "]";
    }

}
