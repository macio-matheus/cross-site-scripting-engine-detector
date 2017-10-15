package system;

import utilsLib.util.*;
import java.util.*;

public class ResponseFormBusiness {
    private DBResponseFormRepository responseFormRepository;

    public ResponseFormBusiness(DBResponseFormRepository responseFormRepository) {
        if (responseFormRepository == null) {
            throw new IllegalArgumentException("Param: " + responseFormRepository);
        }

        this.responseFormRepository = responseFormRepository;
    }

    public void add(ResponseForm responseForm) {
        responseFormRepository.insert(responseForm);
    }

    public void update(ResponseForm responseForm) {
        responseFormRepository.update(responseForm);
    }

    public void remove(int id) {
        responseFormRepository.delete(id);
    }

    public ResponseForm[] getResponseForms(ResponseFormSearchParams params) {
        return responseFormRepository.getResponseForms(params);
    }

    public ResponseForm search(int id) {
        ResponseFormSearchParams params = new ResponseFormSearchParams();
        params.addFilter(new FilterParam("id", id, FilterParam.COMPARE_TYPE.EQUAL));

        params.setPi(new PagingInfo(1));

        return (ResponseForm)Utils.getArrayItem(this.getResponseForms(params), 0, null);
    }
}