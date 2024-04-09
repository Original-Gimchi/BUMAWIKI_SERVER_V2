package com.project.bumawiki.global.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Target(TYPE)
@Retention(RUNTIME)
@Transactional(readOnly = false)
@Service
public @interface ServiceWithTransactionalReadOnly {

}
