package komunitas.java.repository.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import komunitas.java.model.StressTestResult;
import komunitas.java.repository.StressTestRepository;
import komunitas.java.util.XmlParser;

public class StressTestXmlRepository implements StressTestRepository {
    private static final String FILE_PATH = "src/main/resources/data/stress_tests.xml";
    private static final String ROOT_ELEMENT = "stressTests";
    private static final String ITEM_ELEMENT = "stressTest";
    
    private XmlParser<StressTestResult> xmlParser;

    public StressTestXmlRepository() {
        this.xmlParser = new XmlParser<>(StressTestResult.class);
    }

    @Override
    public void save(StressTestResult result) {
        List<StressTestResult> results = findAll();
        results.add(result);
        xmlParser.marshal(results, FILE_PATH, ROOT_ELEMENT, ITEM_ELEMENT);
    }

    @Override
    public List<StressTestResult> findByUserId(String userId) {
        return findAll().stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<StressTestResult> findAllSharedResults() {
        return findAll().stream()
                .filter(StressTestResult::isShareWithProfessionals)
                .collect(Collectors.toList());
    }

    @Override
    public void update(StressTestResult result) {
        List<StressTestResult> results = findAll();
        results.removeIf(r -> r.getId().equals(result.getId()));
        results.add(result);
        xmlParser.marshal(results, FILE_PATH, ROOT_ELEMENT, ITEM_ELEMENT);
    }

    @Override
    public void delete(String resultId) {
        List<StressTestResult> results = findAll();
        results.removeIf(r -> r.getId().equals(resultId));
        xmlParser.marshal(results, FILE_PATH, ROOT_ELEMENT, ITEM_ELEMENT);
    }

    private List<StressTestResult> findAll() {
        List<StressTestResult> results = xmlParser.unmarshal(FILE_PATH, ITEM_ELEMENT);
        return results != null ? results : new ArrayList<>();
    }
}