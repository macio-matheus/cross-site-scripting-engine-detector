package system;

import java.sql.SQLException;

import utilsLib.util.ConnManager;
import utilsLib.util.DBRepository;
import utilsLib.util.ObjFactory;
import utilsLib.util.data.Field;

public class DBUrlFoundRepository extends DBRepository {
    public DBUrlFoundRepository(ConnManager connManager,
                                   ObjFactory objFactory, int classID,
                                   int connIndex) {
        super(connManager, objFactory, classID, connIndex);
    }

    public UrlFound search(int id) {
        Field field = (Field) getEntity().getMainFields()[0].clone();
        field.setValue(id);

        try {
            return (UrlFound)super.search(field);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void insert(UrlFound UrlFound) {
        if (UrlFound == null) {
            throw new IllegalArgumentException("Param: UrlFound");
        }

        try {
            UrlFound.setId(super.insert(UrlFound));
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void update(UrlFound urlFound) {
        if (urlFound == null) {
            throw new IllegalArgumentException("Param: UrlFound");
        }

        try {
            super.update(urlFound);
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

    public UrlFound[] getUrlFounds(UrlFoundSearchParams params) {
        try {
		return (UrlFound[])super.getObjs(params);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }
}