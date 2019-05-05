package com.dev.lyhoangvinh.mvparchitecture.di.scopes;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * scope for instances which live as long as the activity
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {}
