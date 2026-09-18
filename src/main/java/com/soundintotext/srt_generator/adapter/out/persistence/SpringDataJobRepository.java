package com.soundintotext.srt_generator.adapter.out.persistence;

import com.soundintotext.srt_generator.domain.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJobRepository extends JpaRepository<JobEntity, String> {}
