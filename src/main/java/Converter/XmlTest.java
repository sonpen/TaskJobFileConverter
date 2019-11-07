package Converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1109806 on 2019-10-31.
 */
public class XmlTest {

    public XmlTest() {
    }

    public void jsonFilesToXml(String directory) throws IOException {
        File dir = new File(directory);
        File[] fileList = dir.listFiles();
        for(File file : fileList) {
            if(file.isFile() && file.getName().endsWith(".json")) {
                System.out.println(file.getName());
                jsonFileToXml(file);
            }
            else if(file.isDirectory()) {
                jsonFilesToXml(file.getCanonicalPath());
            }
        }
    }

    public void jsonFileToXml(File file) throws IOException {

        // json -> map
        ObjectMapper objectmapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectmapper.readValue( String.join("", Files.readAllLines(file.toPath())), new TypeReference<Map<String,Object>>(){});
        System.out.println(jsonMap);

        // map -> xml
        XmlMapper mapper = new XmlMapper();
        String xmlFileName = file.getCanonicalPath().replaceAll("json$", "xml");
        Map<String, Object> taskJobMap = new HashMap<String, Object>();
        taskJobMap.put("TASK_LIST", jsonMap.get("TASK_LIST"));
        mapper.writerWithDefaultPrettyPrinter().withRootName("TASK_JOB").writeValue(new File(xmlFileName), taskJobMap);

        // xml -> map
        List<Map<String, String>> xmlMap = mapper.readValue(new File(xmlFileName), new TypeReference<List<Map<String, String>>>(){});

        if( jsonMap.get("TASK_LIST").toString().equals(xmlMap.toString()))
            System.out.println("Equal!!!!");
        else {
            System.out.println(xmlMap);
        }

        for(Map<String, String> m : xmlMap) {
            for(Map.Entry<String, String> entry : m.entrySet()) {
                String value = entry.getValue();
                if(value.indexOf("\n") != -1 ) {
                    StringBuilder sb = new StringBuilder("\n\t\t");
                    sb.append(value.replaceAll("\n", "\n\t\t"));
                    sb.append("\n\t");
                    entry.setValue(sb.toString());
                }
            }
        }

        // TASK_LIST map -> xml
        Map<String, Object> taskJobMap1 = new HashMap<String, Object>();
        taskJobMap1.put("TASK_LIST", xmlMap);
        mapper.writerWithDefaultPrettyPrinter().withRootName("TASK_JOB").writeValue(new File(xmlFileName), taskJobMap1);
    }

    public void testJsonToXml() {

        try {
            // json -> map
            ObjectMapper objectmapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectmapper.readValue(
                    String.join("", Files.readAllLines(Paths.get("C:\\workspace\\IdeaProjects\\batch_slave_j\\task_job\\src\\main\\resources\\task_job\\DM_TABLE_ETL\\AGE_DM_HDFS_업로드\\AGE_DM_HDFS_업로드.json"))),
                    new TypeReference<Map<String,Object>>(){});
            System.out.println(jsonMap);

            // map -> xml
            XmlMapper mapper = new XmlMapper();
            mapper.writerWithDefaultPrettyPrinter().withRootName("TASK_JOB").writeValue(new File("C:\\workspace\\IdeaProjects\\batch_slave_j\\task_job\\src\\main\\resources\\task_job\\DM_TABLE_ETL\\AGE_DM_HDFS_업로드\\AGE_DM_HDFS_업로드.xml"), jsonMap);

            // TASK_LIST map -> xml
            Map<String, Object> taskJobMap = new HashMap<String, Object>();
            taskJobMap.put("TASK_LIST", jsonMap.get("TASK_LIST"));
            mapper.writerWithDefaultPrettyPrinter().withRootName("TASK_JOB").writeValue(new File("C:\\workspace\\IdeaProjects\\batch_slave_j\\task_job\\src\\main\\resources\\task_job\\DM_TABLE_ETL\\AGE_DM_HDFS_업로드\\AGE_DM_HDFS_업로드2.xml"), taskJobMap);

            // xml -> map
            List<Map<String, String>> xmlMap = mapper.readValue(new File("C:\\workspace\\IdeaProjects\\batch_slave_j\\task_job\\src\\main\\resources\\task_job\\DM_TABLE_ETL\\AGE_DM_HDFS_업로드\\AGE_DM_HDFS_업로드2.xml"),
                    new TypeReference<List<Map<String, String>>>(){});


            if( jsonMap.get("TASK_LIST").toString().equals(xmlMap.toString()))
                System.out.println("Equal!!!!");
            else {
                System.out.println(xmlMap);
            }

            for(Map<String, String> m : xmlMap) {
                for(Map.Entry<String, String> entry : m.entrySet()) {
                    String value = entry.getValue();
                    if(value.indexOf("\n") != -1 ) {
                        StringBuilder sb = new StringBuilder("\n\t\t");
                        sb.append(value.replaceAll("\n", "\n\t\t"));
                        sb.append("\n\t");
                        entry.setValue(sb.toString());
                    }
                }
            }

            // TASK_LIST map -> xml
            Map<String, Object> taskJobMap1 = new HashMap<String, Object>();
            taskJobMap1.put("TASK_LIST", xmlMap);
            mapper.writerWithDefaultPrettyPrinter().withRootName("TASK_JOB").writeValue(new File("C:\\workspace\\IdeaProjects\\batch_slave_j\\task_job\\src\\main\\resources\\task_job\\DM_TABLE_ETL\\AGE_DM_HDFS_업로드\\AGE_DM_HDFS_업로드3.xml"), taskJobMap1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        XmlTest xmlTest = new XmlTest();

//        xmlTest.testJsonToXml();

        try {
            //String dir = "C:\\workspace\\IdeaProjects\\batch_slave_j\\task_job\\src\\main\\resources\\task_job\\DM_TABLE_ETL\\AGE_DM_HDFS_업로드\\";
            String dir = "C:/workspace/IdeaProjects/batch_slave_j/task_job/src/main/resources/task_job/";
            xmlTest.jsonFilesToXml(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
