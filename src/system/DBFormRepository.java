package system;

import utilsLib.util.*;
import utilsLib.jsp.*;
import utilsLib.util.data.*;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

import java.util.Date;

public class DBFormRepository extends DBRepository {
    public DBFormRepository(ConnManager connManager,
                                   ObjFactory objFactory, int classID,
                                   int connIndex) {
        super(connManager, objFactory, classID, connIndex);
    }

    public Form search(int id) {
        Field field = (Field) getEntity().getMainFields()[0].clone();
        field.setValue(id);

        try {
            return (Form)super.search(field);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void insert(Form form) {
        if (form == null) {
            throw new IllegalArgumentException("Param: form");
        }

        try {
            form.setId(super.insert(form));
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void update(Form form) {
        if (form == null) {
            throw new IllegalArgumentException("Param: form");
        }

        try {
            super.update(form);
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

    public Form[] getForms(FormSearchParams params) {
        try {
		return (Form[])super.getObjs(params);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }
}