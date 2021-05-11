package wooteco.subway.application.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.line.section.Sections;
import wooteco.subway.domain.line.value.LineColor;
import wooteco.subway.domain.line.value.LineId;
import wooteco.subway.domain.line.value.LineName;
import wooteco.subway.domain.station.StationRepository;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository; //Is it possible? it makes dependency with Station package!!!

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Line save(Line line) {
        validateLineExisting(line);
        validateStationIdExisting(line);

        return lineRepository.save(line);
    }

    private void validateLineExisting(Line line) {
        if (lineRepository.contains(line)) {
            throw new IllegalArgumentException("동알한 라인은 등록할 수 없습니다.");
        }
    }

    private void validateStationIdExisting(Line line) {
        List<Long> stations = line.getSections().stream()
                .flatMap(
                        section -> Stream.of(
                                section.getUpStationId(),
                                section.getDownStationId()
                        )
                )
                .distinct()
                .collect(toList());

        stations.forEach(stationRepository::findById);
    }

    public List<Line> allLines() {
        return lineRepository.allLines();
    }

    public Line findById(final Long id) {
        return lineRepository.findById(id);
    }

    public void update(final Line line) {
        List<Section> sections = lineRepository.findById(line.getLineId()).getSections();
        lineRepository.update(
                new Line(
                        new LineId(line.getLineId()),
                        new LineName(line.getLineName()),
                        new LineColor(line.getLineColor()),
                        new Sections(sections)
                )
        );
    }

    public void deleteById(final Long id) {
        lineRepository.deleteById(id);
    }

    public void addNewSection(Long lineId, Section section) {
        Line line = lineRepository.findById(lineId);
        line.addSection(section);

        lineRepository.update(line);
    }

    public List<Long> getStationsById(Long id) {
        return lineRepository.findById(id).getStationIds();
    }

}
