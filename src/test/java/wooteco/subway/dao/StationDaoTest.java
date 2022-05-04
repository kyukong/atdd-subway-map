package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Station;

@JdbcTest
class StationDaoTest {

    private StationDao stationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void save() {
        stationDao.save(new Station("강남역"));

        Integer count = jdbcTemplate.queryForObject("select count(*) from STATION", Integer.class);

        assertThat(count).isEqualTo(1);
    }

    @DisplayName("중복된 이름의 지하철역이 있다면 true를 반환한다.")
    @Test
    void existStationByName() {
        stationDao.save(new Station("강남역"));

        assertThat(stationDao.existStationByName("강남역")).isTrue();
    }

    @DisplayName("지하철역의 전체 목록을 조회한다.")
    @Test
    void findAll() {
        stationDao.save(new Station("강남역"));
        stationDao.save(new Station("선릉역"));

        List<String> stations = stationDao.findAll()
                .stream().map(Station::getName)
                .collect(Collectors.toList());

        assertThat(stations).containsExactly("강남역", "선릉역");
    }

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void delete() {
        long stationId = stationDao.save(new Station("강남역"));

        assertThat(stationDao.delete(stationId)).isEqualTo(1);
    }
}
