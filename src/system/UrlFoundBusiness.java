package system;

import utilsLib.util.*;
import java.util.*;

public class UrlFoundBusiness {
    private DBUrlFoundRepository urlFoundRepository;

    public UrlFoundBusiness(DBUrlFoundRepository urlFoundRepository) {
        if (urlFoundRepository == null) {
            throw new IllegalArgumentException("Param: " + urlFoundRepository);
        }

        this.urlFoundRepository = urlFoundRepository;
    }

    public void add(UrlFound urlFound) {
        urlFoundRepository.insert(urlFound);
    }

    public void update(UrlFound urlFound) {
        urlFoundRepository.update(urlFound);
    }

    public void remove(int id) {
        urlFoundRepository.delete(id);
    }

    public UrlFound[] getUrlFounds(UrlFoundSearchParams params) {
        return urlFoundRepository.getUrlFounds(params);
    }

    public UrlFound search(int id) {
        UrlFoundSearchParams params = new UrlFoundSearchParams();
        params.addFilter(new FilterParam("id", id, FilterParam.COMPARE_TYPE.EQUAL));

        params.setPi(new PagingInfo(1));

        return (UrlFound)Utils.getArrayItem(this.getUrlFounds(params), 0, null);
    }
}