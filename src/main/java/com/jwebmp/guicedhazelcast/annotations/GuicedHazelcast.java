package com.jwebmp.guicedhazelcast.annotations;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.*;

/**
 * Marks the instance as the default for Guiced Hazelcast
 */
@Target(
		{
				ElementType.TYPE, ElementType.TYPE_USE, ElementType.PARAMETER
		})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@BindingAnnotation
public @interface GuicedHazelcast
{}
