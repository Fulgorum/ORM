package hibernate;

import hibernate.Direccion.Direccion;
import hibernate.Profesor.Profesor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Principal {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {

        try {
            menu();
        } catch (IOException e) {
            System.out.println("Error al leer desde teclado.");
        }


        // CRUD
        //INSERTAR
        // Profesor profesor = new Profesor("Pedro", "Zarco", "García");
        // session.persist(profesor);

        // LEER
        //Profesor miProfesor = session.get(Profesor.class,1);
        //System.out.println(miProfesor);

        // ACTUALIZAR
        //session.merge(new Profesor(1,"Santiago","Segura","Salcedo"));

        // BORRAR
        //session.remove(new Profesor(1, null, null, null));

    }

    private static void menu() throws IOException {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        Session session = null;
        String respuesta;
        System.out.println("Bienvenido al programa de gestor de progresa");
        do {
            System.out.println("Selecciona la opción que deseas realizar");
            System.out.println("1. Insertar.");
            System.out.println("2. Modificar.");
            System.out.println("3. Leer.");
            System.out.println("4. Borrar.");
            System.out.println("5. Salir");
            respuesta = br.readLine();

            boolean b = respuesta.equals("1") || respuesta.equals("2") || respuesta.equals("3") || respuesta.equals("4");
            if (b) {
                session = sessionFactory.openSession();
                session.beginTransaction();
            }


            switch (respuesta) {
                case "1" -> insertarProfesor(session);
                case "2" -> modificarProfesor(session);
                case "3" -> leerProfesor(session);
                case "4" -> borrarProfesor(session);
                default -> {
                    if (!respuesta.equals("5")) System.out.println("la respuesta debe ser 1, 2, 3, 4 ó 5.");
                }

            }

            if (b) {
                session.getTransaction().commit();
                session.close();
            }


        } while (!respuesta.equals("5"));
    }

    private static void insertarProfesor(Session session) throws IOException {
        Profesor profesor = leerDatos(false);
        session.persist(profesor);
        System.out.println("Profesor insertado");
    }

    private static Profesor leerDatos(boolean modificando) throws IOException {
        System.out.println("Dime el nombre");
        String nombre = br.readLine();

        System.out.println("Dime el Primer apellido");
        String apellido1 = br.readLine();

        System.out.println("Dime el Segundo apellido");
        String apellido2 = br.readLine();

        Direccion direccion = leerDireccion();

        if (modificando) {
            System.out.println("Dime el id que quieres modificar");
            int id = Integer.parseInt(br.readLine());
            Profesor profesorActual = new Profesor(id, nombre, apellido1, apellido2);
            direccion.setId(id);
            profesorActual.setDireccion(direccion);
            direccion.setProfesor(profesorActual);
            return profesorActual;
        }

        Profesor profesor = new Profesor(nombre, apellido1, apellido2);
        profesor.setDireccion(direccion);
        direccion.setProfesor(profesor);
        return profesor;
    }

    private static Direccion leerDireccion() throws IOException {
        System.out.println("Dime la calle");
        String calle = br.readLine();

        System.out.println("Dime la poblacion");
        String poblacion = br.readLine();

        System.out.println("Dime la provincia");
        String provincia = br.readLine();

        return new Direccion(calle, poblacion, provincia);
    }

    private static void modificarProfesor(Session session) throws IOException {
        Profesor profesor = leerDatos(true);
        System.out.println("***** ANTES *****");
        System.out.println(session.get(Profesor.class, profesor.getId()));
        System.out.println("***** DESPUES *****");
        System.out.println(profesor);
        System.out.println("¿Desea realizar los cambios? Escriba si");
        String respuesta = br.readLine();
        if (respuesta.equalsIgnoreCase("si")) {
            session.merge(profesor);
        }
    }

    private static void leerProfesor(Session session) throws IOException {
        verProfesor(session);
    }

    private static int verProfesor(Session session) throws IOException {
        System.out.println("Dime el id profesor");
        Profesor profesor = session.get(Profesor.class, Integer.parseInt(br.readLine()));
        System.out.println(profesor);
        return profesor.getId();
    }

    private static void borrarProfesor(Session session) throws IOException {
        int id = verProfesor(session);
        System.out.println("¿Este es el profesor que quiere borrar? Escriba si en caso afirmativo.");
        String respuesta = br.readLine();
        if (respuesta.equalsIgnoreCase("si")) {
            Profesor profesor = session.get(Profesor.class, id);
            session.remove(profesor);
        }

    }
}
