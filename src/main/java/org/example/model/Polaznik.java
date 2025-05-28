package org.example.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Polaznik")
public class Polaznik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PolaznikID")
    private int id;
    @Column(name = "Ime")
    private String ime;
    @Column(name = "Prezime")
    private String prezime;

    @OneToMany(mappedBy = "polaznik", cascade = CascadeType.ALL)
    private List<Upis> upisi;

    public Polaznik() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public List<Upis> getUpisi() {
        return upisi;
    }

    public void setUpisi(List<Upis> upisi) {
        this.upisi = upisi;
    }
}
