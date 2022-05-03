package com.ue.prakash.config;

import com.ue.prakash.pojo.entity.ActivityTracker;
import com.ue.prakash.repository.ActivityTrackerRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    private ActivityTrackerRepository activityTrackerRepository;

    @Bean
    public FlatFileItemReader<ActivityTracker> reader() {
        FlatFileItemReader<ActivityTracker> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/activities.csv"));
        flatFileItemReader.setName("activityReader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    private LineMapper<ActivityTracker> lineMapper() {
        DefaultLineMapper<ActivityTracker> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("name", "time", "duration");
        BeanWrapperFieldSetMapper<ActivityTracker> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ActivityTracker.class);
        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public ActivityTrackerProcessor processor() {
        return new ActivityTrackerProcessor();
    }

    @Bean
    public RepositoryItemWriter<ActivityTracker> writer() {
        RepositoryItemWriter<ActivityTracker> writer = new RepositoryItemWriter<>();
        writer.setRepository(activityTrackerRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1() {
       return stepBuilderFactory.get("csv-step").<ActivityTracker, ActivityTracker>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job runJob(){
        return  jobBuilderFactory.get("import-csv").flow(step1())
                .end().build();
    }
}
