package jjuni.global.message;

import jjuni.domain.common.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ErrorMessageResolver {

    private final MessageSource messageSource;

    /**
     * ErrorCode → 사용자에게 보여줄 메시지 변환
     */
    public String resolve(ErrorCode errorCode) {
        return messageSource.getMessage(
                errorCode.getMessageKey(),
                null,
                errorCode.getMessageKey(), // fallback (절대 예외 안 터짐)
                LocaleContextHolder.getLocale()
        );
    }
}