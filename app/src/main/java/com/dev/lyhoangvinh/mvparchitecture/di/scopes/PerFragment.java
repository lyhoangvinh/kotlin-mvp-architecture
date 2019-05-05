package com.dev.lyhoangvinh.mvparchitecture.di.scopes;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by air on 5/1/17.
 * Scope for instances which live as long as the fragment
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerFragment {
}
