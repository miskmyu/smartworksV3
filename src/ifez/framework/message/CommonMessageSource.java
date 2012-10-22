package ifez.framework.message;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * <pre>
 * 기능 : 메세지 소스를 통한 메시지 접근을 위한 클래스
 * </pre>
 */

public class CommonMessageSource extends ReloadableResourceBundleMessageSource
        implements MessageSource {
    
    private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

    private MessageSource messageSource;

    private Locale defaultLocale;

    public CommonMessageSource() {
        this.messageSource = null;
        this.defaultLocale = null;
    }
    
    public CommonMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        defaultLocale = null;
    }

    public CommonMessageSource(MessageSource messageSource, Locale defaultLocale) {
        this.messageSource = messageSource;
        this.defaultLocale = defaultLocale;
    }

    protected Locale getDefaultLocale() {
        return defaultLocale == null ? LocaleContextHolder.getLocale()
                : defaultLocale;
    }

    /**
     * getReloadableResourceBundleMessageSource()
     * 
     * @param reloadableResourceBundleMessageSource
     *            - resource MessageSource
     * @return ReloadableResourceBundleMessageSource
     */
    public void setReloadableResourceBundleMessageSource(
            ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource) {
        this.reloadableResourceBundleMessageSource = reloadableResourceBundleMessageSource;
    }

    /**
     * getReloadableResourceBundleMessageSource()
     * 
     * @return ReloadableResourceBundleMessageSource
     */
    public ReloadableResourceBundleMessageSource getReloadableResourceBundleMessageSource() {
        return reloadableResourceBundleMessageSource;
    }

    /**
     * 정의된 메세지 조회
     * 
     * @param String code 메세지 코드
     * @return String
     */
    public String getMessage(String code) {
        return getReloadableResourceBundleMessageSource().getMessage(code,
                null, getDefaultLocale());
    }

    public String getMessage(String code, Locale locale) {
        return getReloadableResourceBundleMessageSource().getMessage(code,
                null, locale);
    }

    /**
     * 정의된 메세지 조회
     * 
     * @param String code 메세지 코드
     * @param String[] args 메세지 Arguments String array
     * @param String locale 메세지 Locale
     * @return String
     */
    public String getMessage(String code, String[] args, Locale locale) {
        return getReloadableResourceBundleMessageSource().getMessage(code,
                args, locale);
    }
}