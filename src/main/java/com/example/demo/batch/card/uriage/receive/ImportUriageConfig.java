package com.example.demo.batch.card.uriage.receive;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import com.example.demo.common.entity.Uriage;
import com.example.demo.common.mapper.UriageMapper;
import com.example.demo.core.exception.CustomSkipPolicy;
import com.example.demo.core.listener.LogChunkListener;
import com.example.demo.core.listener.LogJobListener;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ImportUriageConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final LogChunkListener logChunkListener;
    private final LogJobListener logJobListener;
    private final ImportUriageProcessor uriageImportProcessor;
    private final ImportUriageWriter uriageImportWriter;
    private final UriageMapper uriageMapper;
    
    @Bean
    public FlatFileItemReader<ImportUriageItem> uriageReader() {
        
        FlatFileItemReader<ImportUriageItem> uriageReader = new FlatFileItemReader<>();

        // ヘッダー行をスキップ
        uriageReader.setLinesToSkip(1);
        uriageReader.setResource(new FileSystemResource("input-data/uriage.csv"));
        uriageReader.setLineMapper(new DefaultLineMapper<ImportUriageItem>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("uriId", "memberId", "createdAt");
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<ImportUriageItem>() {
                    {
                        setTargetType(ImportUriageItem.class);
                    }
                });
            }
        });
        return uriageReader;
    };

    @Bean
    public Tasklet truncateUriageTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                // uriageテーブルを全件削除する
                uriageMapper.truncate();
                // 処理が完了したことを示す
                return RepeatStatus.FINISHED;
            }
        };
    }

    /**
     * ジョブ
     */
    @Bean
    public Job importUriageJob() {
        return new JobBuilder("importUriageJob", jobRepository)
                .start(importUriageStep1())
                .next(importUriageStep2())
                .listener(logJobListener) // ログ出力（ジョブ単位）
                .build();
    }

    /**
     * ステップ１
     */
    @Bean
    public Step importUriageStep1() {
        return new StepBuilder("importUriageStep1", jobRepository)
                .tasklet(truncateUriageTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    /**
     * ステップ２
     */
    @Bean
    public Step importUriageStep2() {
        return new StepBuilder("importUriageStep2", jobRepository)
                .<ImportUriageItem, Uriage>chunk(10, platformTransactionManager)
                .reader(uriageReader())
                .processor(uriageImportProcessor)
                .writer(uriageImportWriter)
                .allowStartIfComplete(true) // true:何度でも再実行可能。false:一度だけ実行可能。
                .faultTolerant()  // 耐障害性設定。例外等が発生しても処理を継続する。
                .skipPolicy(new CustomSkipPolicy()) // カスタムスキップポリシーを設定。特定例外はスキップする。
                .listener(logChunkListener) // ログ出力（チャンク単位）
                .build();
    }
}