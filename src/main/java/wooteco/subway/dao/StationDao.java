package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.Station;

public interface StationDao {

    long save(Station station);

    boolean existStationByName(String name);

    List<Station> findAll();

    int delete(Long id);
}
