package edy.app.sgc.arch.domain.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author edythawne
 * @created 31/08/2026 18:00
 * @project ut_sgc
 */
@Component
@RequiredArgsConstructor
public class LangConfig {

    private final MessageSource messageSource;

    public String get(String key) {
        try {
            return messageSource.getMessage(key, null, Locale.of("es"));
        } catch (Exception e) {
            return key;
        }
    }

    public String get(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, Locale.of("es"));
        } catch (Exception e) {
            return key;
        }
    }

}

