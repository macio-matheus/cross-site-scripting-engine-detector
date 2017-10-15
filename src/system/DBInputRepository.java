package system;

import utilsLib.util.*;
import utilsLib.jsp.*;
import utilsLib.util.data.*;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

import java.util.Date;

public class DBInputRepository extends DBRepository {
    public DBInputRepository(ConnManager connManager,
                                   ObjFactory objFactory, int classID,
                                   int connIndex) {
        super(connManager, objFactory, classID, connIndex);
    }

    public Input search(int id) {
        Field field = (Field) getEntity().getMainFields()[0].clone();
        field.setValue(id);

        try {
            return (Input)super.search(field);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void insert(Input input) {
        if (input == null) {
            throw new IllegalArgumentException("Param: input");
        }

        try {
            input.setId(super.insert(input));
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void update(Input input) {
        if (input == null) {
            throw new IllegalArgumentException("Param: input");
        }

        try {
            super.update(input);
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

    public Input[] getInputs(InputSearchParams params) {
        try {
		return (Input[])super.getObjs(params);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }
}