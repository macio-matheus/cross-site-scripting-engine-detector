package system;

import utilsLib.util.*;
import utilsLib.jsp.*;
import utilsLib.util.data.*;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

import java.util.Date;

public class DBScriptDetectedRepository extends DBRepository {
    public DBScriptDetectedRepository(ConnManager connManager,
                                   ObjFactory objFactory, int classID,
                                   int connIndex) {
        super(connManager, objFactory, classID, connIndex);
    }

    public ScriptDetected search(int id) {
        Field field = (Field) getEntity().getMainFields()[0].clone();
        field.setValue(id);

        try {
            return (ScriptDetected)super.search(field);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void insert(ScriptDetected scriptDetected) {
        if (scriptDetected == null) {
            throw new IllegalArgumentException("Param: scriptDetected");
        }

        try {
            scriptDetected.setId(super.insert(scriptDetected));
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void update(ScriptDetected scriptDetected) {
        if (scriptDetected == null) {
            throw new IllegalArgumentException("Param: scriptDetected");
        }

        try {
            super.update(scriptDetected);
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

    public ScriptDetected[] getScriptDetecteds(ScriptDetectedSearchParams params) {
        try {
		return (ScriptDetected[])super.getObjs(params);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }
}