package com.soundintotext.srt_generator.application.usecase;

import com.soundintotext.srt_generator.domain.model.SubtitleResultCommand;

public interface ProcessSubtitleResultUseCase {
    void processResult(SubtitleResultCommand command);
}