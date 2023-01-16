/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import entities.Location;
import entities.Match;
import entities.Role;
import entities.User;
import errorhandling.API_Exception;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate() throws API_Exception {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();


        User user = new User("user", "test123",20202020,"user@user.dk", "OK");
        User player1 = new User("player1", "test123",30303030,"player1@player1.dk", "OK");
        User player2 = new User("player2", "test123",33333333,"player2@player2.dk", "OK");
        User player3 = new User("player3", "test123",44444444,"player3@player3.dk", "OK");
        User admin = new User("admin", "test123",40404040,"admin@admin.dk", "OK");
        User playerUser = new User("player", "test123",30303030,"player@player.dk", "OK");

        Location location1 = new Location("Skinderskovvej 31", "2730 Herlev", "OK");
        Location location2 = new Location("Centrumgade 1", "2860 Ballerup", "OK");

        Match match1 = new Match("Hillerød - Herlev", user, "Squash", (byte) 1, location1);
        Match match2 = new Match("Sverige - Danmark", player1, "Squash", (byte) 0, location1);
        Match match3 = new Match("Århus - København", user, "Squash", (byte) 1, location2);


        em.getTransaction().begin();

        em.createQuery("delete from Match").executeUpdate();
        em.createQuery("delete from User").executeUpdate();
        em.createQuery("delete from Role").executeUpdate();
        em.createQuery("delete from Location").executeUpdate();

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        Role playerRole = new Role("player");

        user.addRole(userRole);
        admin.addRole(adminRole);
        player1.addRole(playerRole);
        player2.addRole(playerRole);
        player3.addRole(playerRole);
        playerUser.addRole(userRole);
        playerUser.addRole(playerRole);

        player1.addMatch(match1);
        player2.addMatch(match1);

        player2.addMatch(match2);
        player3.addMatch(match2);

        player1.addMatch(match3);
        player3.addMatch(match3);

        em.persist(userRole);
        em.persist(adminRole);
        em.persist(playerRole);

        em.persist(location1);
        em.persist(location2);

        em.persist(match1);
        em.persist(match2);
        em.persist(match3);

        em.persist(user);
        em.persist(admin);
        em.persist(player1);
        em.persist(player2);
        em.persist(player3);
        em.persist(playerUser);
        em.getTransaction().commit();

        System.out.println("PW: " + user.getUserPass());
        System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
        System.out.println("Created TEST Users");
    }
    
    public static void main(String[] args) throws API_Exception {
        populate();
    }
}
