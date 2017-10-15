package system;

import utilsLib.util.*;
import java.util.*;

public class FormBusiness {
    private DBFormRepository formRepository;

    public FormBusiness(DBFormRepository formRepository) {
        if (formRepository == null) {
            throw new IllegalArgumentException("Param: " + formRepository);
        }

        this.formRepository = formRepository;
    }

    public void add(Form form) {
        formRepository.insert(form);
    }

    public void update(Form form) {
        formRepository.update(form);
    }

    public void remove(int id) {
        formRepository.delete(id);
    }

    public Form[] getForms(FormSearchParams params) {
        return formRepository.getForms(params);
    }

    public Form search(int id) {
        FormSearchParams params = new FormSearchParams();
        params.addFilter(new FilterParam("id", id, FilterParam.COMPARE_TYPE.EQUAL));

        params.setPi(new PagingInfo(1));

        return (Form)Utils.getArrayItem(this.getForms(params), 0, null);
    }
}