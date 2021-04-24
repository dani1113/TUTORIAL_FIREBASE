package com.example.tutorialfirebase.Modelos;

import android.util.Log;

import com.example.tutorialfirebase.Clases.Moda;
import com.example.tutorialfirebase.Clases.Producto;
import com.example.tutorialfirebase.Modelos.ConfiguraciónDB.BaseDB;
import com.example.tutorialfirebase.Modelos.ConfiguraciónDB.ConfiguracionesGeneralesDB;

import java.lang.invoke.MutableCallSite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ModaDB {
    public static ArrayList<Moda> obtenerModa(int página){
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if(conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return null;
        }
        ArrayList<Moda> modaDevuelta = new ArrayList<Moda>();
        try {
            Statement sentencia = conexión.createStatement();
            int desplazamiento = página * ConfiguracionesGeneralesDB.ELEMENTOS_POR_PAGINA;
            String ordenSQL = "SELECT DISTINCT * FROM moda m INNER JOIN productos p ON (m.cod_producto = p.cod_producto) LIMIT" + desplazamiento + ", " + ConfiguracionesGeneralesDB.ELEMENTOS_POR_PAGINA;
            ResultSet resultado = sentencia.executeQuery(ordenSQL);
            while(resultado.next()) {
                //PRODUCTO
                String cod_producto = resultado.getString("cod_producto");
                String cod_QR = resultado.getString("cod_QR");
                String marca = resultado.getString("marca");
                String modelo = resultado.getString("modelo");
                String descripcion = resultado.getString("descripcion");

                //MODA
                String talla = resultado.getString("talla");
                String color = resultado.getString("color");
                String material = resultado.getString("material");
                String sexo = resultado.getString("sexo");
                String categoria_moda = resultado.getString("categoria_moda");

                Producto p = new Producto(cod_producto, cod_QR, marca, modelo, descripcion);
                Moda m = new Moda(p, talla, color, material, sexo, categoria_moda);
                modaDevuelta.add(m);
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            return modaDevuelta;
        } catch (SQLException e) {
            Log.i("SQL", "Error al mostrar los registros de moda de la base de datos");
            return null;
        }
    }

    public static ArrayList<Moda> buscarModa(String nombreM) {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return null;
        }
        ArrayList<Moda> modasEncontradas = new ArrayList<Moda>();
        try {
            ResultSet resultado = BaseDB.buscarFilas(conexión, "moda", "talla", nombreM);
            if (resultado == null) {
                return null;
            }
            while (resultado.next()) {
                //Recojo la moda
                String cod_producto = resultado.getString("cod_producto");
                String talla = resultado.getString("talla");
                String color = resultado.getString("color");
                String material = resultado.getString("material");
                String sexo = resultado.getString("sexo");
                String categoria_moda = resultado.getString("categoria_moda");
                Moda m = new Moda(cod_producto, talla, color, material, sexo, categoria_moda);
                modasEncontradas.add(m);
            }
            resultado.close();

            conexión.close();

            return modasEncontradas;
        } catch (SQLException e) {
            Log.i("SQL", "Error al buscar los registros de moda en la base de datos");
            return null;
        }
    }

    public static int obtenerCantidadModa() {
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return 0;
        }
        int cantidadModa = 0;
        try {
            Statement sentencia = conexión.createStatement();
            String ordenSQL = "SELECT count(*) as cantidad FROM moda";
            ResultSet resultado  = sentencia.executeQuery(ordenSQL);
            while (resultado.next()) {
                cantidadModa = resultado.getInt("cantidad");
            }
            resultado.close();
            sentencia.close();

            conexión.close();

            return cantidadModa;
        } catch (SQLException e) {
            Log.i("SQL", "Error al devolver el número de filas de moda de la base de datos");
            return 0;
        }
    }

    public static boolean insertarModa(Moda m){ //¿Se puede insertar una moda con un cod_producto que no esta previamente creado? De ser así habría que hacer un insert en productos y por tanto el cod_producto de moda, debería de ser un atributo de tipo Producto, para poder hacer la insercion
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return false;
        }
        try {
            String ordenSQL = "INSERT INTO moda (cod_producto, talla, color, material, sexo, categoria_moda) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement sentenciaPreparada = conexión.prepareStatement(ordenSQL);
            sentenciaPreparada.setString(1, m.getCod_producto());
            sentenciaPreparada.setString(2, m.getTalla());
            sentenciaPreparada.setString(3, m.getColor());
            sentenciaPreparada.setString(4, m.getMaterial());
            sentenciaPreparada.setString(5, m.getSexo());
            sentenciaPreparada.setString(6, m.getCategoria_moda());
            int filasAfectadas = sentenciaPreparada.executeUpdate();
            sentenciaPreparada.close();

            conexión.close();

            if (filasAfectadas > 0){
                return true;
            }else {
                return false;
            }
        } catch (SQLException e){
            Log.i("SQL", "Error al insertar la fila en la tabla moda de la base de datos");
            return false;
        }
    }

    public static boolean borrarModa(Moda m){ //Se da por hecho que para borrar una moda debe de haber un registro en la tabla productos con el mismo cod_producto que el de la fila que se quiere borrar
        Connection conexión = BaseDB.conectarConBaseDeDatos();
        if (conexión == null) {
            Log.i("SQL", "Error al establecer la conexión con la base de datos");
            return false;
        }
        try {
            String ordenSQL1 = "DELETE FROM moda WHERE cod_producto = ?;";
            PreparedStatement sentenciaPreparada1 = conexión.prepareStatement(ordenSQL1);
            sentenciaPreparada1.setString(1, m.getCod_producto());
            int filasAfectadas1 = sentenciaPreparada1.executeUpdate();
            sentenciaPreparada1.close();

            String ordenSQL2 = "DELETE FROM productos WHERE cod_producto = ?;";
            PreparedStatement sentenciaPreparada2 = conexión.prepareStatement(ordenSQL2);
            sentenciaPreparada2.setString(1, m.getCod_producto());
            int filasAfectadas2 = sentenciaPreparada2.executeUpdate();
            sentenciaPreparada2.close();

            conexión.close();

            if(filasAfectadas1 > 0 && filasAfectadas2 > 0) {
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            Log.i("SQL", "Error al borrar el registro de moda en la base de datos");
            return false;
        }

    }

}