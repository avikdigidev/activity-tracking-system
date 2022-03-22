package com.userexperior.prakash.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.userexperior.prakash.pojo.dto.request.ActivityTrackerDTO;
import com.userexperior.prakash.pojo.dto.response.ActivityReport;
import com.userexperior.prakash.service.ActivityTrackerService;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ActivityTrackerServiceImpl implements ActivityTrackerService {
    @Override
    public ActivityReport getActivityReport() {
        try {
            readJSONFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private void readJSONFiles() throws IOException {


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLongSerializationPolicy( LongSerializationPolicy.STRING );
        Gson gson = gsonBuilder.create();
        Path folder = Paths.get("D:\\Projects\\JavaProjects\\userexperior-activity-tracking-system\\ActivitiesToProcess\\");
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(folder))  {

            for (Path entry : stream) {
                String fp = folder+"\\"+entry.getFileName().toString();
                ActivityTrackerDTO activityDto = gson.fromJson(new FileReader(ResourceUtils.getFile(fp)), ActivityTrackerDTO.class);

            }
        } catch (Exception e) {
            // An I/O problem has occurred
            e.printStackTrace();
        }


    }
}
