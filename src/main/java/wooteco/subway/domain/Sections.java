package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public void add(final Section section) {
        validateSection(section);

        Section existSection = getExistSection(section);
        if (existSection.isAddingEndSection(section)) {
            sections.add(section);
            return;
        }
        addStationInSection(existSection, section);
    }

    private void validateSection(final Section section) {
        List<Long> stationIds = getStationIdsInSection();
        if (hasAllStation(section, stationIds)) {
            throw new IllegalArgumentException("상행역과 하행역이 이미 지하철 노선에 존재합니다.");
        }
        if (hasNotAnyStation(section, stationIds)) {
            throw new IllegalArgumentException("추가하려는 구간이 노선에 포함되어 있지 않습니다.");
        }
    }

    private List<Long> getStationIdsInSection() {
        List<Long> allStationIds = new ArrayList<>();
        allStationIds.addAll(getUpStationIds());
        allStationIds.addAll(getDownStationIds());

        return allStationIds.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Long> getUpStationIds() {
        return sections.stream()
                    .map(Section::getUpStationId)
                    .collect(Collectors.toList());
    }

    private List<Long> getDownStationIds() {
        return sections.stream()
                .map(Section::getDownStationId)
                .collect(Collectors.toList());
    }

    private boolean hasAllStation(final Section section, final List<Long> stationIds) {
        return stationIds.contains(section.getUpStationId()) && stationIds.contains(section.getDownStationId());
    }

    private boolean hasNotAnyStation(final Section section, final List<Long> stationIds) {
        return !stationIds.contains(section.getUpStationId()) && !stationIds.contains(section.getDownStationId());
    }

    private void addStationInSection(final Section existSection, final Section section) {
        Section replacedSection = Section.replace(existSection, section);

        sections.remove(existSection);
        sections.add(section);
        sections.add(replacedSection);
    }

    private Section getExistSection(Section section) {
        return sections.stream()
                .filter(exist -> exist.existStation(section.getUpStationId())
                        || exist.existStation(section.getDownStationId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("구간 정보를 찾을 수 없습니다."));
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}