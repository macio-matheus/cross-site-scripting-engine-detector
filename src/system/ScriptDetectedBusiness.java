package system;

import utilsLib.util.*;
import java.util.*;

public class ScriptDetectedBusiness {
    private DBScriptDetectedRepository scriptDetectedRepository;

    public ScriptDetectedBusiness(DBScriptDetectedRepository scriptDetectedRepository) {
        if (scriptDetectedRepository == null) {
            throw new IllegalArgumentException("Param: " + scriptDetectedRepository);
        }

        this.scriptDetectedRepository = scriptDetectedRepository;
    }

    public void add(ScriptDetected scriptDetected) {
        scriptDetectedRepository.insert(scriptDetected);
    }

    public void update(ScriptDetected scriptDetected) {
        scriptDetectedRepository.update(scriptDetected);
    }

    public void remove(int id) {
        scriptDetectedRepository.delete(id);
    }

    public ScriptDetected[] getScriptDetecteds(ScriptDetectedSearchParams params) {
        return scriptDetectedRepository.getScriptDetecteds(params);
    }

    public ScriptDetected search(int id) {
        ScriptDetectedSearchParams params = new ScriptDetectedSearchParams();
        params.addFilter(new FilterParam("id", id, FilterParam.COMPARE_TYPE.EQUAL));

        params.setPi(new PagingInfo(1));

        return (ScriptDetected)Utils.getArrayItem(this.getScriptDetecteds(params), 0, null);
    }
}