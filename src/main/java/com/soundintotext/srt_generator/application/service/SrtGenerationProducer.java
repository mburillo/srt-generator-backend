package com.soundintotext.srt_generator.application.service;

import com.soundintotext.srt_generator.domain.model.SrtGenerationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



@Service
public class SrtGenerationProducer {

    private static final String TOPIC = "srt-generation-requests";
    private static final Logger log = LoggerFactory.getLogger(SrtGenerationProducer.class);

    private final KafkaTemplate<String, SrtGenerationRequest> kafkaTemplate;

    public SrtGenerationProducer(KafkaTemplate<String, SrtGenerationRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendGenerationRequest(SrtGenerationRequest request) {
        kafkaTemplate.send(TOPIC, request.jobId(), request)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error enviando mensaje para job {}: {}", request.jobId(), ex.getMessage(), ex);
                    } else {
                        log.info("Mensaje enviado a partición {} con offset {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}