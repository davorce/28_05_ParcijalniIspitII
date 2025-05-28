package org.example.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ProgramObrazovanja")
public class ProgramObrazovanja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProgramObrazovanjaID")
    private int id;
    @Column(name = "Naziv")
    private String naziv;
    @Column(name = "CSVET")
    private int csvet;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    private List<Upis> upisi;

    public ProgramObrazovanja() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getCsvet() {
        return csvet;
    }

    public void setCsvet(int csvet) {
        this.csvet = csvet;
    }

    public List<Upis> getUpisi() {
        return upisi;
    }

    public void setUpisi(List<Upis> upisi) {
        this.upisi = upisi;
    }
}
