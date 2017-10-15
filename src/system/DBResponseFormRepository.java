package system;

import utilsLib.util.*;
import utilsLib.jsp.*;
import utilsLib.util.data.*;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

import java.util.Date;

public class DBResponseFormRepository extends DBRepository {
    public DBResponseFormRepository(ConnManager connManager,
                                   ObjFactory objFactory, int classID,
                                   int connIndex) {
        super(connManager, objFactory, classID, connIndex);
    }

    public ResponseForm search(int id) {
        Field field = (Field) getEntity().getMainFields()[0].clone();
        field.setValue(id);

        try {
            return (ResponseForm)super.search(field);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void insert(ResponseForm responseForm) {
        if (responseForm == null) {
            throw new IllegalArgumentException("Param: responseForm");
        }

        try {
            responseForm.setId(super.insert(responseForm));
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void update(ResponseForm responseForm) {
        if (responseForm == null) {
            throw new IllegalArgumentException("Param: responseForm");
        }

        try {
            super.update(responseForm);
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

    public ResponseForm[] getResponseForms(ResponseFormSearchParams params) {
        try {
		return (ResponseForm[])super.getObjs(params);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }
}