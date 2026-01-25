package main;

import dao.db.DataSource;
import exceptions.dao.DbConnectionException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbPing {

    private static final String SQL = "SELECT COUNT(*) FROM books";

    public static void main(String[] args) {
        try (PreparedStatement ps = DataSource.getInstance()
                .getConnection()
                .prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            System.out.println("Количество книг (books): " + rs.getInt(1));
        } catch (DbConnectionException e) {
            System.out.println("Ошибка подключения к БД: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
