package org.example;

import org.example.model.Polaznik;
import org.example.model.ProgramObrazovanja;
import org.example.model.Upis;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Uspješno ste spojeni na bazu JavaAdvHibernate!");

        while (true) {
            System.out.println("#### IZBORNIK ####");
            System.out.println("1. Unesi novog polaznika");
            System.out.println("2. Unesi novi program obrazovanja");
            System.out.println("3. Upisi polaznika na program obrazovanja");
            System.out.println("4. Prebaci polaznika iz jednog u drugi program obrazovanja");
            System.out.println("5. Ispisi informacije o polaznicima za zadani program obrazovanja");
            System.out.println("0. Izađi iz aplikacije");
            System.out.print("Odabir: ");

            int opcija = Integer.parseInt(scan.nextLine());

            switch (opcija) {
                case 1:
                    noviPolaznik(scan);
                    break;
                case 2:
                    noviProgram(scan);
                    break;
                case 3:
                    upisiPolaznika(scan);
                    break;
                case 4:
                    prebaciPolaznika(scan);
                    break;
                case 5:
                    ispisPolaznikaPoProgramu(scan);
                    break;
                case 0:
                    System.out.println("Hvala, bok!");
                    return;
                default:
                    System.err.println("Izabrali ste nepostojecu opciju!");
            }
        }
    }

    private static void noviPolaznik(Scanner scan) {
        System.out.print("Unesi ime novog polaznika: ");
        String ime = scan.nextLine();
        System.out.print("Unesi prezime novog polaznika: ");
        String prezime = scan.nextLine();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Polaznik p = new Polaznik();
            p.setIme(ime);
            p.setPrezime(prezime);

            session.save(p);
            tx.commit();
            System.out.println("Novi polaznik je uspjesno unesen!");

        } catch (Exception e) {
            System.err.println("Doslo je do greske pri dodavanju novog korisnika, pokusaj opet!");
            e.printStackTrace();
        }
    }

    private static void noviProgram(Scanner scan) {
        System.out.print("Unesi naziv novog programa: ");
        String naziv = scan.nextLine();
        System.out.print("Unesi broj CSVET bodova novog programa: ");
        int csvet = Integer.parseInt(scan.nextLine());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            ProgramObrazovanja po = new ProgramObrazovanja();
            po.setNaziv(naziv);
            po.setCsvet(csvet);

            session.save(po);
            tx.commit();
            System.out.println("Novi program obrazovanja je uspjesno unesen!");

        } catch (Exception e) {
            System.err.println("Doslo je do greske pri dodavanju novog programa obrazovanja, pokusaj opet!");
            e.printStackTrace();
        }
    }

    private static void upisiPolaznika(Scanner scan) {
        System.out.print("Unesi ID polaznika kojeg zelis upisati u odredeni program: ");
        int idPolaznik = Integer.parseInt(scan.nextLine());
        System.out.print("Unesi ID programa u koji zelis upisati odabranog polaznika: ");
        int idProgram = Integer.parseInt(scan.nextLine());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Polaznik polaznik = session.get(Polaznik.class, idPolaznik);
            ProgramObrazovanja programObrazovanja = session.get(ProgramObrazovanja.class, idProgram);

            if (polaznik == null || programObrazovanja == null) {
                System.err.println("Program ili polaznik ne postoje u bazi!");
                return;
            }

            Transaction tx = session.beginTransaction();

            Upis u = new Upis();
            u.setPolaznik(polaznik);
            u.setProgramObrazovanja(programObrazovanja);

            session.save(u);
            tx.commit();
            System.out.println("Polaznik je uspjesno upisan u odabrani program!");

        } catch (Exception e) {
            System.err.println("Doslo je do greske pri upisivanju, pokusaj opet!");
            e.printStackTrace();
        }
    }

    private static void prebaciPolaznika(Scanner scan) {
        System.out.print("Unesi ID polaznika kojeg zelis prebaciti: ");
        int idPolaznik = Integer.parseInt(scan.nextLine());
        System.out.print("Unesi ID trenutnog programa: ");
        int currId = Integer.parseInt(scan.nextLine());
        System.out.print("Unesi ID novog programa: ");
        int newId = Integer.parseInt(scan.nextLine());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Upis u = session.createQuery("""
                            FROM Upis u 
                            WHERE u.polaznik.id = :polaznikID AND u.program.id = :programID
                            """, Upis.class)
                    .setParameter("polaznikID", idPolaznik)
                    .setParameter("programID", currId)
                    .uniqueResult();
            if (u == null) {
                System.err.println("Upis s trazenim IDom ne postoji!");
                return;
            }
            ProgramObrazovanja noviProgram = session.get(ProgramObrazovanja.class, newId);
            if (noviProgram == null) {
                System.err.println("Program s trazenim IDom ne postoji!");
                return;
            }
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                u.setProgramObrazovanja(noviProgram);
                session.update(u);
                tx.commit();
                System.out.println("Polaznik je uspjesno prebacen u novi program!");
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                System.err.println("Transakcija neuspjesna, promjene ponistene!");
                e.printStackTrace();
            }
        } catch (Exception ex) {
            System.err.println("Doslo je do greske pri prebacivanju polaznika!");
            ex.printStackTrace();
        }
    }

    private static void ispisPolaznikaPoProgramu(Scanner scan) {
        System.out.print("Unesi ID programa cije polaznike zelite ispisati: ");
        int idProgram = Integer.parseInt(scan.nextLine());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Upis> upisi = session.createQuery("FROM Upis u WHERE u.program.id = :id", Upis.class)
                    .setParameter("id", idProgram)
                    .list();

            if (upisi.isEmpty()) {
                System.err.println("U programu nema upisanih polaznika!");
            } else {
                System.out.println("Polaznici programa su:");
                for (Upis u : upisi) {
                    Polaznik p = u.getPolaznik();
                    ProgramObrazovanja po = u.getProgramObrazovanja();
                    System.out.println(p.getIme() + " " + p.getPrezime() + ", " + po.getNaziv() + ", CSVET: " + po.getCsvet());
                }
            }

        } catch (Exception ex) {
            System.err.println("Doslo je do greske pri ispisu polaznika!");
            ex.printStackTrace();
        }
    }
}
