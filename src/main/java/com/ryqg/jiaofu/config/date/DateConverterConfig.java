package com.ryqg.jiaofu.config.date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

@Configuration
@Slf4j
public class DateConverterConfig {
    private static final String INVALID_DATE_FORMAT_MSG = "无效的日期格式，预期格式: %s";

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    private static final String LOCAL_TIME_FORMAT = "HH:mm:ss";

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT+8");

    private static final Locale LOCALE = Locale.CHINA;

    // 线程安全的 SimpleDateFormat（通过 ThreadLocal）
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT = ThreadLocal.withInitial(() ->
            new SimpleDateFormat(DATE_TIME_FORMAT));
    // 线程安全的格式化器
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);
    private static final DateTimeFormatter LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT);

    // 统一异常提示模板
    private static final String INVALID_DATE_FORMAT_TEMPLATE = "无效的日期格式，预期格式: %s";

    // json格式
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        log.info("日期与字符串互转：Locale:【{}】， TimeZone:【{}】， Date:【{}】， LocalDateTime:【{}】，LocalDate:【{}】，LocalTime:【{}】",
                LOCALE, TIME_ZONE.getDisplayName(), DATE_TIME_FORMAT, DATE_TIME_FORMAT, LOCAL_DATE_FORMAT, LOCAL_TIME_FORMAT);
        return builder -> {
            // 禁用时间戳序列化
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            // 配置序列化时忽略空值
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);
            //地区、时区
            builder.locale(LOCALE).timeZone(TIME_ZONE);
            // 注册 JavaTimeModule 处理 JSR-310 时间类型
            builder.modulesToInstall(new JavaTimeModule());
            // 序列化 后－>前
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER))
                    .serializerByType(LocalDate.class, new LocalDateSerializer(LOCAL_DATE_FORMATTER))
                    .serializerByType(LocalTime.class, new LocalTimeSerializer(LOCAL_TIME_FORMATTER));
            // 反序列化 前－>后
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DATE_TIME_FORMATTER))
                    .deserializerByType(LocalDate.class, new LocalDateDeserializer(LOCAL_DATE_FORMATTER))
                    .deserializerByType(LocalTime.class, new LocalTimeDeserializer(LOCAL_TIME_FORMATTER));

            // 配置 java.util.Date 的序列化和反序列化
            builder.serializerByType(Date.class, new DateSerializer(false, SIMPLE_DATE_FORMAT.get()));
            builder.deserializerByType(Date.class,
                    new DateDeserializers.DateDeserializer(DateDeserializers.DateDeserializer.instance,
                            SIMPLE_DATE_FORMAT.get(), DATE_TIME_FORMAT)
            );
        };
    }

    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(@Nullable String source) {
                if (Objects.isNull(source) || source.trim().isEmpty()) {
                    throw new IllegalArgumentException("日期时间格式错误，预期格式: " + LOCAL_DATE_FORMAT);
                }
                try {
                    return LocalDate.parse(source, DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT));
                } catch (Exception e) {
                    throw new IllegalArgumentException("日期时间格式错误，预期格式: " + LOCAL_DATE_FORMAT, e);
                }
            }
        };
    }

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(@Nullable String source) {
                if (Objects.isNull(source) || source.trim().isEmpty()) {
                    throw new IllegalArgumentException("无效的日期格式，预期格式: " + DATE_TIME_FORMAT);
                }
                try {
                    return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
                } catch (Exception e) {
                    throw new IllegalArgumentException("日期时间格式错误，预期格式: " + DATE_TIME_FORMAT, e);
                }

            }
        };
    }

    /**
     * LocalTime转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(@Nullable String source) {
                if (Objects.isNull(source) || source.trim().isEmpty()) {
                    throw new IllegalArgumentException("无效的日期格式，预期格式: " + LOCAL_TIME_FORMAT);
                }
                try {
                    return LocalTime.parse(source, DateTimeFormatter.ofPattern(LOCAL_TIME_FORMAT));
                } catch (Exception e) {
                    throw new IllegalArgumentException("日期时间格式错误，预期格式: " + LOCAL_TIME_FORMAT, e);
                }

            }
        };
    }

    /**
     * Date转换器，用于转换RequestParam和PathVariable参数
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(@Nullable String source) {
                if (Objects.isNull(source) || source.trim().isEmpty()) {
                    throw new IllegalArgumentException("无效的日期格式，预期格式: " + DATE_TIME_FORMAT);
                }
                try {
                    return SIMPLE_DATE_FORMAT.get().parse(source);
                } catch (ParseException e) {
                    log.error("Init Converter String to Date error!", e);
                    throw new IllegalArgumentException("日期时间格式错误，预期格式: " + DATE_TIME_FORMAT, e);
                }
            }
        };
    }
}
