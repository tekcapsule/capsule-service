package com.tekcapsule.capsule.application.function;

import com.tekcapsule.capsule.domain.service.CapsuleService;
import in.devstream.core.domain.Origin;
import in.devstream.core.utils.HeaderUtil;
import com.tekcapsule.capsule.application.function.input.CreateInput;
import com.tekcapsule.capsule.application.mapper.InputOutputMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.tekcapsule.capsule.application.config.AppConstants.HTTP_STATUS_CODE_HEADER;

@Component
@Slf4j
public class CreateFunction implements Function<Message<CreateInput>, Message<Mentor>> {

    private final CapsuleService capsuleService;

    public CreateFunction(final CapsuleService capsuleService) {
        this.capsuleService = capsuleService;
    }

    @Override
    public Message<Mentor> apply(Message<CreateInput> createInputMessage) {

        CreateInput createInput = createInputMessage.getPayload();

        log.info(String.format("Entering create mentor Function - Tenant Id:{0}, Name:{1}", createInput.getTenantId(), createInput.getName().toString()));

        Origin origin = HeaderUtil.buildOriginFromHeaders(createInputMessage.getHeaders());

        CreateCommand createCommand = InputOutputMapper.buildCreateCommandFromCreateInput.apply(createInput, origin);
        Mentor mentor = mentorService.create(createCommand);
        Map<String, Object> responseHeader = new HashMap();
        responseHeader.put(HTTP_STATUS_CODE_HEADER, HttpStatus.OK.value());

        return new GenericMessage(mentor, responseHeader);
    }
}