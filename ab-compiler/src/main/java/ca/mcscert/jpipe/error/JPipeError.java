package ca.mcscert.jpipe.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation definition to "tag" some exceptions as being semantically relevant for jPipe.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JPipeError {

}
