package system;

import utilsLib.util.*;
import utilsLib.jsp.*;
import utilsLib.util.data.*;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;

import java.util.Date;

public class DBCodeXSSRepository extends DBRepository {
    public DBCodeXSSRepository(ConnManager connManager,
                                   ObjFactory objFactory, int classID,
                                   int connIndex) {
        super(connManager, objFactory, classID, connIndex);
    }

    public CodeXSS search(int id) {
        Field field = (Field) getEntity().getMainFields()[0].clone();
        field.setValue(id);

        try {
            return (CodeXSS)super.search(field);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void insert(CodeXSS codeXSS) {
        if (codeXSS == null) {
            throw new IllegalArgumentException("Param: codeXSS");
        }

        try {
            codeXSS.setId(super.insert(codeXSS));
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    public void update(CodeXSS codeXSS) {
        if (codeXSS == null) {
            throw new IllegalArgumentException("Param: codeXSS");
        }

        try {
            super.update(codeXSS);
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

    public CodeXSS[] getCodeXSSs(CodeXSSSearchParams params) {
        try {
		return (CodeXSS[])super.getObjs(params);
        } catch (SQLException error) {
            throw new RuntimeException(error.getMessage());
        }
    }
}