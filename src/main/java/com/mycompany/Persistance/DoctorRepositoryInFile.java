/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.Persistance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.Interfaces.IAuthenticableRepository;
import com.mycompany.Interfaces.IDoctorRepository;
import com.mycompany.Models.Doctor;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.User;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase concreta encargada del guardado y gestión de persistencia en archivos.
 * Utiliza una clase de repositorio en memoria para evitar reescribir código de
 * operaciones como agregar, eliminar, etc. En su lugar, introduce el guardado y
 * carga desde archivos, así como la sobrescritura de archivos cada vez que se
 * haga algún cambio o modificación.
 *
 * @author kosmo
 */
public class DoctorRepositoryInFile extends PersistenceHelper<Doctor> implements
        IDoctorRepository, IAuthenticableRepository {

    private final IDoctorRepository repo; //Repositorio antiguo en memoria RAM
    private final Gson _gson; //Gson para manejar serializazión y deserialización

    /**
     * Constructor de la clase.
     *
     * @param repo el repositorio en memoria.
     * @param fileName la ruta del archivo.
     */
    private DoctorRepositoryInFile(IDoctorRepository repo, String fileName) {
        super(fileName); //Almacena el nombre del archivo
        this.repo = repo; //Guarda como atributo un repositorio en memoria

        // Instancia el gson:
        this._gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Método estático que instancia la clase y carga los datos en memoria.
     * @param repo repositorio en memoria.
     * @param fileName ruta del archivo.
     */
    public static IDoctorRepository init(IDoctorRepository repo, String fileName) {

        DoctorRepositoryInFile repository;
        repository = new DoctorRepositoryInFile(repo, fileName);

        // Cargar datos del archivo
        List<Doctor> doctors = repository.load();

        // Poblar el repositorio en memoria con los datos cargados
        if (!doctors.isEmpty()) {
            for (Doctor doctor : doctors) {
                repo.add(doctor);
            }
        }

        return repository;

    }

    /**
     * Implementación del método abstracto de guardado de doctores en archivo.
     *
     * @param data Lista de médicos a almacenar en archivo
     * @return booleano para saber si se guradó con éxito
     */
    @Override
    protected boolean save(List<Doctor> data) {
        File file = new File(this.getFileName()); //Ruta del archivo
        File tempFile = new File(file.getAbsolutePath() + ".tmp");

        //Try-With-Resources, permite cerrar automáticamente el writer
        //Si tempFile no existe, java lo crea
        try (var writer = new FileWriter(tempFile)) {
            System.out.println("Saving List...");

            //Convertir lista en JSON
            _gson.toJson(data, writer);

            System.out.println(tempFile.getName() + " Doctors List Saved succesfully in temp file!");

        } catch (Exception e) {
            System.out.println("An error ocurred while trying to write: "
                    + tempFile.getName());

            //Si hubo error al escribir, no es necesario mantener el archivo
            //temporal
            if (tempFile.exists() && !tempFile.delete()) {
                // Si el archivo no se pudo borrar
                System.out.println("Temporary file: " + tempFile.getName() + " could not be deleted!");
            }
            return false;
        }

        //Una vez sobreescrito el archivo temporal, se copia su contenido al archivo original
        try (var reader = new FileReader(tempFile); var writer2 = new FileWriter(file)) {
            //Arreglo que va guardando los caracteres que lee reader
            char[] buffer = new char[1024];
            // Variable entera que almacena el número de caracteres léidos por reader
            int len;
            //Reader llena el buffer y retorna la cantidad de caracteres léidos
            //Retorna -1 cuando ha leído todo el archivo
            while ((len = reader.read(buffer)) > 0) {
                //Cuando reader llena el arreglo buffer, writer se encarga
                //de escribirlo en el archivo oficial
                writer2.write(buffer, 0, len);
            }

            System.out.println("File saved succesfully!: " + file.getName());

        } catch (Exception e) {
            System.out.println("Could not copy the file:\n" + e.getMessage());
            return false;
        } finally {
            //Siempre borrar el archivo temporal
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
        return true;
    }

    /**
     * Implementación del método abstracto de carga de la información en el
     * archivo.
     * @return lista de médicos o lista vacía
     */
    @Override
    protected List<Doctor> load() {
        File file = new File(this.getFileName()); //Ruta del archivo

        //Verificar existencia del archivo
        if (!file.exists()) {
            // Si el archivo no existe, devuelve una lista vacía
            System.out.println("File not found: " + file.getName());
            System.out.println("Returning an empty list");
            return List.of();
        }

        //Si el archivo existe
        //Try-With-Resources, permite cerrar automáticamente el reader
        try (var reader = new FileReader(file)) {
            System.out.println("Loading Doctor List...");

            //Convertir JSON en lista
            List<Doctor> loadedDoctors;
            loadedDoctors = _gson.fromJson(reader,
                    new TypeToken<ArrayList<Doctor>>() {
                    }.getType());

            //Verificar que loadedDoctors no sea nulo
            if (loadedDoctors == null) {
                System.out.println("File exists but it's empty!\n" + "Returning an empty List!");
                loadedDoctors = List.of();
            }

            System.out.println(file.getName() + " loaded succesfully!");
            return loadedDoctors;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("An error ocurred while trying to load: "
                    + file.getName() + "\nReturning an empty list instead");
            return List.of();
        }
    }

    @Override
    public boolean add(Doctor doctor) {
        return repo.add(doctor) && save(listAllIncludingInactiveDoctors());
    }

    @Override
    public boolean deleteById(String id) {
        return repo.deleteById(id) && save(listAllIncludingInactiveDoctors());
    }

    @Override
    public boolean update(Doctor doctor) {
        return repo.update(doctor) && save(listAllIncludingInactiveDoctors());
    }

    @Override
    public Optional<Doctor> searchById(String id) {
        return repo.searchById(id);
    }

    @Override
    public List<Doctor> searchBySpecialty(Specialty specialty) {
        return repo.searchBySpecialty(specialty);
    }

    @Override
    public List<Doctor> listAll() {
        return repo.listAll();
    }

    @Override
    public Optional<User> searchByUsername(String username) {
        return ((IAuthenticableRepository) repo).searchByUsername(username);
    }

    @Override
    public List<Doctor> listAllIncludingInactiveDoctors() {
        return repo.listAllIncludingInactiveDoctors();
    }

}
