package system;

import utilsLib.util.*;

public class AnalyseBusiness {
    private DBAnalyseRepository analyseRepository;

    public AnalyseBusiness(DBAnalyseRepository analyseRepository) {
        if (analyseRepository == null) {
            throw new IllegalArgumentException("Param: " + analyseRepository);
        }

        this.analyseRepository = analyseRepository;
    }

    public void add(Analyse analyse) {
        analyseRepository.insert(analyse);
    }

    public void update(Analyse analyse) {
        analyseRepository.update(analyse);
    }

    public void remove(int id) {
        analyseRepository.delete(id);
    }

    public Analyse[] getAnalyses(AnalyseSearchParams params) {
        return analyseRepository.getAnalyses(params);
    }

    public Analyse search(int id) {
        AnalyseSearchParams params = new AnalyseSearchParams();
        params.addFilter(new FilterParam("id", id, FilterParam.COMPARE_TYPE.EQUAL));

        params.setPi(new PagingInfo(1));

        return (Analyse)Utils.getArrayItem(this.getAnalyses(params), 0, null);
    }
}