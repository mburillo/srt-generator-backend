package com.soundintotext.srt_generator.adapter.in.adapter;

import com.soundintotext.srt_generator.application.usecase.ProcessSubtitleResultUseCase;
import com.soundintotext.srt_generator.domain.exception.JobNotFoundException;
import com.soundintotext.srt_generator.domain.model.SubtitleResultCommand;
import com.soundintotext.srt_generator.domain.model.SubtitleResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SubtitleResultConsumer {

    private static final Logger log = LoggerFactory.getLogger(SubtitleResultConsumer.class);

    private final ProcessSubtitleResultUseCase processSubtitleResultUseCase;

    public SubtitleResultConsumer(ProcessSubtitleResultUseCase processSubtitleResultUseCase) {
        this.processSubtitleResultUseCase = processSubtitleResultUseCase;
    }

    @KafkaListener(topics = "srt-generation-results", groupId = "java-app-results-group")
    public void listen(SubtitleResultEvent event) {
        log.info("Resultado recibido para job {}: status={}", event.jobId(), event.status());

        try {
            processSubtitleResultUseCase.processResult(new SubtitleResultCommand(
                    event.jobId(), event.status(), event.srtObjectKey(), event.detectedLanguage(), event.error()
            ));
        } catch (JobNotFoundException e) {
            log.error("Mensaje descartado, job no encontrado en BD: {}", event.jobId(), e);
        } catch (IllegalStateException e) {
            log.warn("Mensaje duplicado o job ya en estado final, se ignora: {}", e.getMessage());
        }
    }
}
