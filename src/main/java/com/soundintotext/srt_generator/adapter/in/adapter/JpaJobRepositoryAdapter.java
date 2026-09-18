package com.soundintotext.srt_generator.adapter.in.adapter;

import com.soundintotext.srt_generator.adapter.in.port.JobRepositoryPort;
import com.soundintotext.srt_generator.adapter.out.mapper.JobEntityMapper;
import com.soundintotext.srt_generator.adapter.out.persistence.SpringDataJobRepository;
import com.soundintotext.srt_generator.domain.model.Job;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaJobRepositoryAdapter implements JobRepositoryPort {

    private final SpringDataJobRepository springDataJobRepository;

    public JpaJobRepositoryAdapter(SpringDataJobRepository springDataJobRepository) {
        this.springDataJobRepository = springDataJobRepository;
    }

    @Override
    public void save(Job job) {
        springDataJobRepository.save(JobEntityMapper.toEntity(job));
    }

    @Override
    public Optional<Job> findById(String jobId) {
        return springDataJobRepository.findById(jobId).map(JobEntityMapper::toDomain);
    }
}