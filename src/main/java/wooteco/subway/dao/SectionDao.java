package wooteco.subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;

import java.util.List;

@Repository
public class SectionDao {

    private static final RowMapper<Section> SECTION_ROW_MAPPER = (resultSet, rowNum) -> {
        return new Section(
                resultSet.getLong("id"),
                resultSet.getLong("line_id"),
                resultSet.getLong("up_station_id"),
                resultSet.getLong("down_station_id"),
                resultSet.getInt("distance")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(final Long lineId, final Section section) {
        final String sql = "insert into SECTION (line_id, up_station_id, down_station_id, distance) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, lineId, section.getUpStationId(), section.getDownStationId(), section.getDistance());
    }

    public void saveAll(final Long lineId, final Sections sections) {
        for (Section section : sections.getSections()) {
            save(lineId, section);
        }
    }

    public List<Section> findAllById(final Long lineId) {
        final String sql = "select * from SECTION where line_id = ?";
        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER, lineId);
    }

    public boolean existStation(final Long lineId, final Long stationId) {
        final String sql = "select exists " +
                "(select * from SECTION where line_id = ? and (up_station_id = ? or down_station_id = ?))";
        return jdbcTemplate.queryForObject(sql, Boolean.class, lineId, stationId, stationId);
    }

    public boolean existUpStation(final Long lineId, final Long stationId) {
        final String sql = "select exists (select * from SECTION where line_id = ? and up_station_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, lineId, stationId);
    }

    public boolean existDownStation(final Long lineId, final Long stationId) {
        final String sql = "select exists (select * from SECTION where line_id = ? and down_station_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, lineId, stationId);
    }

    public void delete(final Long id) {
        final String sql = "delete from SECTION where line_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
