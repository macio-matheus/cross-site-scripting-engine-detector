package system;

import utilsLib.util.*;
import java.util.*;

public class CodeXSSBusiness {
    private DBCodeXSSRepository codeXSSRepository;

    public CodeXSSBusiness(DBCodeXSSRepository codeXSSRepository) {
        if (codeXSSRepository == null) {
            throw new IllegalArgumentException("Param: " + codeXSSRepository);
        }

        this.codeXSSRepository = codeXSSRepository;
    }

    public void add(CodeXSS codeXSS) {
        codeXSSRepository.insert(codeXSS);
    }

    public void update(CodeXSS codeXSS) {
        codeXSSRepository.update(codeXSS);
    }

    public void remove(int id) {
        codeXSSRepository.delete(id);
    }

    public CodeXSS[] getCodeXSSs(CodeXSSSearchParams params) {
        return codeXSSRepository.getCodeXSSs(params);
    }

    public CodeXSS search(int id) {
        CodeXSSSearchParams params = new CodeXSSSearchParams();
        params.addFilter(new FilterParam("id", id, FilterParam.COMPARE_TYPE.EQUAL));

        params.setPi(new PagingInfo(1));

        return (CodeXSS)Utils.getArrayItem(this.getCodeXSSs(params), 0, null);
    }
}