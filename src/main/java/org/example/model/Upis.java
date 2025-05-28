package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Upis")
public class Upis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UpisID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "IDProgramObrazovanja", referencedColumnName = "ProgramObrazovanjaID")
    private ProgramObrazovanja program;

    @ManyToOne
    @JoinColumn(name = "IDPolaznik", referencedColumnName = "PolaznikID")
    private Polaznik polaznik;

    public Upis() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProgramObrazovanja getProgramObrazovanja() {
        return program;
    }

    public void setProgramObrazovanja(ProgramObrazovanja programObrazovanja) {
        this.program = programObrazovanja;
    }

    public Polaznik getPolaznik() {
        return polaznik;
    }

    public void setPolaznik(Polaznik polaznik) {
        this.polaznik = polaznik;
    }
}
