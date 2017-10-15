package system;

import java.sql.SQLException;

import utilsLib.util.*;
import utilsLib.util.data.Field;
public class DBAnalyseRepository extends DBRepository {
    public DBAnalyseRepository(ConnManager connManager,
                                   ObjFactory objFactory, int classID,
                                   int connIndex) {
        super(connManager, objFactory, classID, connIndex);
    }

    public Analyse search(int id) {
        Field field = (Field) getEntity().getMainFields()[0].clone();
        field.setValue(id);

        try {
            return (Analyse)super.search(field);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void insert(Analyse analyse) {
        if (analyse == null) {
            throw new IllegalArgumentException("Param: analyse");
        }

        try {
            analyse.setId(super.insert(analyse));
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void update(Analyse analyse) {
        if (analyse == null) {
            throw new IllegalArgumentException("Param: analyse");
        }

        try {
            super.update(analyse);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void delete(int id) {
        Field field = (Field) getEntity().getMainFields()[0].clone();
        field.setValue(id);

        try {
            super.delete(field);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public Analyse[] getAnalyses(AnalyseSearchParams params) {
        try {
		return (Analyse[])super.getObjs(params);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }
}