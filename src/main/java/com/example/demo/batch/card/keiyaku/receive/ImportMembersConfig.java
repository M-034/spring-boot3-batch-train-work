package com.example.demo.batch.card.keiyaku.receive;

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
import com.example.demo.common.entity.Members;
import com.example.demo.common.mapper.MembersMapper;
import com.example.demo.core.exception.CustomSkipPolicy;
import com.example.demo.core.listener.LogChunkListener;
import com.example.demo.core.listener.LogJobListener;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ImportMembersConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final LogChunkListener logChunkListener;
    private final LogJobListener logJobListener;
    private final ImportMembersProcessor memberImportProcessor;
    private final ImportMembersWriter memberImportWriter;
    private final MembersMapper membersMapper;
    
    @Bean
    public FlatFileItemReader<ImportMembersItem> memberReader() {
        
        FlatFileItemReader<ImportMembersItem> memberReader = new FlatFileItemReader<>();

        // ヘッダー行をスキップ
        memberReader.setLinesToSkip(1);
        memberReader.setResource(new FileSystemResource("input-data/member.csv"));
        memberReader.setLineMapper(new DefaultLineMapper<ImportMembersItem>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("keiyakuId", "memberId", "merchId", "cardNum", "createdAt");
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<ImportMembersItem>() {
                    {
                        setTargetType(ImportMembersItem.class);
                    }
                });
            }
        });
        return memberReader;
    };

    @Bean
    public Tasklet truncateMemberTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                // membersテーブルを全件削除する
                membersMapper.truncate();
                // 処理が完了したことを示す
                return RepeatStatus.FINISHED;
            }
        };
    }

    /**
     * ジョブ
     */
    @Bean
    public Job importMembersJob() {
        return new JobBuilder("importMembersJob", jobRepository)
                .start(importMembersStep1())
                .next(importMembersStep2())
                .listener(logJobListener) // ログ出力（ジョブ単位）
                .build();
    }

    /**
     * ステップ１
     */
    @Bean
    public Step importMembersStep1() {
        return new StepBuilder("importMembersStep1", jobRepository)
                .tasklet(truncateMemberTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    /**
     * ステップ２
     */
    @Bean
    public Step importMembersStep2() {
        return new StepBuilder("importMembersStep2", jobRepository)
                .<ImportMembersItem, Members>chunk(10, platformTransactionManager)
                .reader(memberReader())
                .processor(memberImportProcessor)
                .writer(memberImportWriter)
                .allowStartIfComplete(true) // true:何度でも再実行可能。false:一度だけ実行可能。
                .faultTolerant()  // 耐障害性設定。例外等が発生しても処理を継続する。
                .skipPolicy(new CustomSkipPolicy()) // カスタムスキップポリシーを設定。特定例外はスキップする。
                .listener(logChunkListener) // ログ出力（チャンク単位）
                .build();
    }
}