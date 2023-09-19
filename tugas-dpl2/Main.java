// Tugas Dasar Pemrograman Lanjutan 2 
// program menjelaskan tentang menghubungkan database mysql dan java
// menggunakan penghubung antara keduanya yaitu jdbc yang terdapat pada 
// file mysql-connector.jar

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
// import java.sql.*;
import java.util.Scanner;

public class Main {
    // ini merupakan url jdbc, dan latihan1 merupakan nama database 
    // untuk membuat database bisa menggunakan query CREATE DATABASE latihan1;
    static final String DB_URL = "jdbc:mysql://localhost:3306/latihan1";
    // user dan pass isikan sesuai dengan data pas pertama install database
    static final String USER = "root";
    static final String PASS = "1234";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in); // scanner utama untuk pemilihan menu
        Scanner scan_case1 = new Scanner(System.in); // scanner untuk case 1 yaitu menambahkan data baru
        Scanner scan_case2 = new Scanner(System.in); // scanner untuk case 2 yaitu mengedit value dari data
        Scanner deleteScanner = new Scanner(System.in); // scanner untuk case 4 yaitu menghapus data
        
        // System.out.println("Rizki bree");
        
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) { // Mencoba menghubungkan ke database
            System.out.println("Connected to the database");

            while (true) {
                // Membuat pilihan yang terdiri 5 pilihan
                System.out.println("1. Menambahkan Menu");
                System.out.println("2. Mengedit melalui Kode_Menu");
                System.out.println("3. Menampilkan semua data pada tabel");
                System.out.println("4. Menghapus data melalui kode");
                System.out.println("5. Keluar");

                int choice;
                try {
                    System.out.print("Pilih tindakan : ");
                    choice = scan.nextInt(); // mengambil nilai yang di ketik user
                    // membersihkan layar agar lebih bersih di lihat
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } catch (Exception e) {
                    System.out.println("Masukkan pilihan yang valid.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        try {
                            // membuat query untuk memasukkan data baru ke Database
                            String sql = "INSERT INTO Menu (Kode_Menu, Nama_Menu, Harga_Menu) VALUES (?, ?, ?)";
                            // prepared statement untuk query dari sql lebih aman dan mudah dalam memasukkan variabel karena terdapat parameter
                            PreparedStatement statement = connection.prepareStatement(sql);

                            // input user untuk data baru
                            System.out.print("Kode Menu : ");
                            int kodeMenu = scan_case1.nextInt();
                            System.out.print("Nama Menu : ");
                            scan_case1.nextLine();
                            String namaMenu = scan_case1.nextLine();
                            System.out.print("Harga Menu : ");
                            int hargaMenu = scan_case1.nextInt();

                            // mengubah tanda ?, ?, ? menjadi nilai dari variabel dengan sebelumnya prepared statement
                            statement.setInt(1, kodeMenu);
                            statement.setString(2, namaMenu);
                            statement.setInt(3, hargaMenu);

                            // mengexecute hasil final query
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("Data berhasil ditambahkan!");
                            }
                            statement.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            // Menampilkan semua kode menu yang ada
                            // dibawah merupakan query untuk mendapatkan list dari Kode_Menu
                            String kodeQuery = "SELECT Kode_Menu FROM Menu";
                            // membuat statement untuk execute query
                            Statement kodeStatement = connection.createStatement();
                            // execute kodeQuery dan mereturn datanya
                            ResultSet kodeResultSet = kodeStatement.executeQuery(kodeQuery);

                            System.out.println("Kode Menu yang tersedia:");
                            while (kodeResultSet.next()) {
                                int kodeMenu = kodeResultSet.getInt("Kode_Menu");
                                System.out.println(kodeMenu);
                            }

                            // menutup statement dan resultSet
                            kodeResultSet.close();
                            kodeStatement.close();

                            System.out.print("Pilih Kode Menu yang ingin diedit: ");
                            int kodeMenuEdit = scan_case2.nextInt();
                            scan_case2.nextLine();

                            // meminta user input untuk data baru
                            System.out.print("Nama Menu baru: ");
                            String namaMenuBaru = scan_case2.nextLine();
                            System.out.print("Harga Menu baru: ");
                            int hargaMenuBaru = scan_case2.nextInt();

                            // query untuk mengupdate nilai pada database yang patokannya adalah Kode_Menu
                            String editSql = "UPDATE Menu SET Nama_Menu = ?, Harga_Menu = ? WHERE Kode_Menu = ?";
                            // sama seperti case 1 menggunakan prepared statement karena terdapat parameter
                            PreparedStatement editStatement = connection.prepareStatement(editSql);
                            editStatement.setString(1, namaMenuBaru);
                            editStatement.setInt(2, hargaMenuBaru);
                            editStatement.setInt(3, kodeMenuEdit);

                            int rowsUpdated = editStatement.executeUpdate();
                            if (rowsUpdated > 0) {
                                System.out.println("Data Menu berhasil diupdate!");
                            } else {
                                System.out.println("Data Menu dengan Kode Menu tersebut tidak ditemukan.");
                            }
                            
                            // menutup prepareStatement
                            editStatement.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        try {
                            // query untuk mendapatkan data table menu
                            String QUERY = "SELECT * FROM Menu";
                            // membuat statement untuk execute query
                            Statement statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery(QUERY);
                            
                            // Menampilkan data table ke terminal atau layar
                            System.out.println("Kode Menu\t\t\tNama Menu\t\t\t\tHarga Menu");
                            while (resultSet.next()) {
                                int kodeMenu = resultSet.getInt("Kode_Menu");
                                String namaMenu = resultSet.getString("Nama_Menu");
                                int hargaMenu = resultSet.getInt("Harga_Menu");
                                System.out.printf("%5d\t%34s\t%,34d%n", kodeMenu, namaMenu, hargaMenu);
                            }
                            System.out.println();
                            // menutup resultSet dan Statement
                            resultSet.close();
                            statement.close();
                        } catch (SQLException e) {
                            // jika terdapat kesalahan pada query dll maka tampilkan apa kode salahnya
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        try {
                            // sama seperti case 2 , ini merupakan query untuk mendapatkan list Kode_Menu
                            String kodeQuery = "SELECT Kode_Menu FROM Menu";
                            Statement kodeStatement = connection.createStatement();
                            ResultSet kodeResultSet = kodeStatement.executeQuery(kodeQuery);
                    
                            System.out.println("Kode Menu yang tersedia:");
                            boolean hasKode = false;
                            while (kodeResultSet.next()) {
                                int kodeMenu = kodeResultSet.getInt("Kode_Menu");
                                System.out.println(kodeMenu);
                                hasKode = true;
                            }
                    
                            kodeResultSet.close();
                            kodeStatement.close();
                            
                            // kondisi jika Kode Menu ada atau tidak kosong/NULL 
                            if (!hasKode) {
                                System.out.println("Tidak ada kode Menu yang tersedia.");
                            } else {
                                System.out.print("Masukkan Kode Menu yang ingin dihapus: ");
                                int kodeMenuDelete = deleteScanner.nextInt();
                    
                                // query untuk menghapus data yang patokannya adalah Kode_Menu
                                String deleteSql = "DELETE FROM Menu WHERE Kode_Menu = ?";
                                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                                deleteStatement.setInt(1, kodeMenuDelete);
                    
                                int rowsDeleted = deleteStatement.executeUpdate();
                                if (rowsDeleted > 0) {
                                    System.out.println("Data Menu dengan Kode Menu " + kodeMenuDelete + " berhasil dihapus.");
                                } else {
                                    System.out.println("Data Menu dengan Kode Menu tersebut tidak ditemukan.");
                                }
                    
                                deleteStatement.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        System.out.println("Keluar dari program.");
                        return;
                    default:
                        System.out.println("Pilihan tidak valid.");
                        break;
                }
            }
        } catch (SQLException e) {
            // jika terdapat kesalahan ketika menghubungkan kedatabase
            e.printStackTrace();
        }

        // menutup semua scanner
        scan.close();
        scan_case1.close();
        scan_case2.close();
        deleteScanner.close();
    }
}
