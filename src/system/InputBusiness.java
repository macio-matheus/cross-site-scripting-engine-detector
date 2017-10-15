package system;

import utilsLib.util.*;
import java.util.*;

public class InputBusiness {
    private DBInputRepository inputRepository;

    public InputBusiness(DBInputRepository inputRepository) {
        if (inputRepository == null) {
            throw new IllegalArgumentException("Param: " + inputRepository);
        }

        this.inputRepository = inputRepository;
    }

    public void add(Input input) {
        inputRepository.insert(input);
    }

    public void update(Input input) {
        inputRepository.update(input);
    }

    public void remove(int id) {
        inputRepository.delete(id);
    }

    public Input[] getInputs(InputSearchParams params) {
        return inputRepository.getInputs(params);
    }

    public Input search(int id) {
        InputSearchParams params = new InputSearchParams();
        params.addFilter(new FilterParam("id", id, FilterParam.COMPARE_TYPE.EQUAL));

        params.setPi(new PagingInfo(1));

        return (Input)Utils.getArrayItem(this.getInputs(params), 0, null);
    }
}