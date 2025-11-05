package com.mycompany.Persistance;

import com.google.gson.reflect.TypeToken;
import com.mycompany.Interfaces.ISpecialtyRepository;
import com.mycompany.Models.Specialty;
import com.mycompany.Models.SpecialtyName;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecialtyRepositoryJSON extends JsonRepository<Specialty> implements ISpecialtyRepository {

    private static final String FILE_PATH = "specialties.json";
    private static final Type LIST_TYPE = new TypeToken<List<Specialty>>() {}.getType();

    public SpecialtyRepositoryJSON() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    public boolean add(Specialty specialty){
        data.add(specialty);
        save();
        return true;
    }

    @Override
    public boolean update(Specialty specialty) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(specialty.getId())) {
                data.set(i, specialty);
                save();
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Specialty> searchByName(SpecialtyName name) {
        return data.stream()
                .filter(specialty -> specialty.getSpecialtyName()== name)
                .findFirst();
    }

    @Override
    public List<Specialty> listAll() {
        return new ArrayList<>(data);
    }
}
