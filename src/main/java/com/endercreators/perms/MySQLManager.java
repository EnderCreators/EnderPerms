package com.endercreators.perms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLManager {

    public static String host;
    public static String database;
    public static String user;
    public static String pass;
    private EnderPerms plugin;

    public MySQLManager(EnderPerms plugin) {
        host = plugin.getConfig().getString("mysql.host");
        database = plugin.getConfig().getString("mysql.database");
        user = plugin.getConfig().getString("mysql.user");
        pass = plugin.getConfig().getString("mysql.pass");
        this.plugin = plugin;
        plugin.go(new Runnable() {
            public void run() {
                fix();
            }
        });

    }


    // Connector
    public static Connection getConn() {
        Connection conn = null;
        try {
            String uri = "jdbc:mysql://" + host + "/" + database;
            conn = DriverManager.getConnection(uri, user, pass);
            fix();
            return conn;
        } catch (Exception ex) {
            System.out.println("[MySQL] Error caused in MySQL. Cannot connect to database.. Make sure that database '" + database + "' exist.");
        }




        return conn;
    }

    public void setGroup(String player, String group) {

        try {
            Connection conn = getConn();
            Statement st = conn.createStatement();
            st.execute("SELECT * FROM `" + database + "`.`groups` WHERE player='" + player + "'");
            ResultSet rs = st.getResultSet();
            if (!rs.next()) {
                st.execute("INSERT INTO `" + database + "`.`groups` VALUES('" + player
                        + "'" + group + "')");
            } else {
                st.execute("UPDATE `" + database + "`.`groups` SET group='" + group + "' WHERE player='" + player + "'");
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("[MySQL] Error caused in setting group. Player: " + player + " Error: " + e);
        }

    }

    public String getGroup(String player) {

        try {
            Connection conn = getConn();
            Statement st = conn.createStatement();
            st.execute("SELECT * FROM `" + database + "`.`groups` WHERE player='" + player + "'");
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                return rs.getString("group");
            }
            conn.close();

        } catch (Exception e) {
            System.out.println("[MySQL] Error caused in getting group. Player: " + player + " Error: " + e);
        }
        return null;
    }


    private static void fix() {
        try {
            String uri = "jdbc:mysql://" + host + "/";
            Connection conn = DriverManager.getConnection(uri, user, pass);
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS `"
                    + database
                    + "`.groups ( `player` VARCHAR(32) NOT NULL, `group` VARCHAR(32))");
        } catch (Exception e) {
            System.out.println("[MySQL] Error caused while setting up table. Make sure that the database exist. Error: " + e);
        }
    }

}
